package com.tcx.mathgame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	
	private Button[] buttons = new Button[11];
	private TextView entry;
	private TextView prob;
	private TextView right;
	private TextView wrong;
	private TextView time;
	private int answer;
	private int LENGTH = 10;
	private int time_left = LENGTH;
	private Runnable runnable;
	private Handler handler;
	private boolean gameOn;
	private int group1Id = 1;
	private SharedPreferences prefs;
	private int[] rVs = new int[8];
	private int subR, addR, multR, divR;
	private int[] probTypes = new int[4];
	private int numbTypes;
	private Random rn = new Random();
	private Game game;
	private DatabaseHandler db;
	private MyScheduleReciver reciever;
	
	int newGameId = Menu.FIRST;
	int settingsId = Menu.FIRST +1;
	int endGameId = Menu.FIRST + 2;
	int histoyId = Menu.FIRST + 3;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		prefs = getSharedPreferences("MyPrefsFile",0);
		
		entry = (TextView) findViewById(R.id.entry);
		prob = (TextView) findViewById(R.id.problem);
		right = (TextView) findViewById(R.id.right);
		wrong = (TextView) findViewById(R.id.wrong);
		time= (TextView) findViewById(R.id.timer);
		
		for( int i=0; i < buttons.length; i++ ) {
			String a = "b" + i;
			buttons[i] = (Button) findViewById(getResources().getIdentifier(a, "id", getPackageName()));
			buttons[i].setOnClickListener( this );
		}
		
		db = new DatabaseHandler(this);
		
		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				time_left--;
				time.setText( time_left + "" );
				if( time_left == 0 ){
					endGame();
				} else {
					handler.postDelayed(this, 1000);
				}
			}

			
		
		};
		
		
    }
	

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	    menu.add(group1Id, newGameId, newGameId, "New Game");
	    menu.add(group1Id, settingsId, settingsId, "Settings");
	    menu.add( group1Id, endGameId, endGameId, "End Game");
	    menu.add( group1Id, histoyId, histoyId, "History");
	   
	
	    return super.onCreateOptionsMenu(menu); 
    }
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {
	
		case 1:
	    	newGame();
	    	return true;
	
		case 2:
			endGame();
	    	Intent intent = new Intent( this , Settings.class );
			startActivity( intent );
	    	return true;
	    	
		case 3:
			endGame();
			return true;
			
		case 4:
			endGame();
	    	Intent intent1 = new Intent( this , GameHistory.class );
			startActivity( intent1 );
			
		default:
	    	break;
	
	       }
	    	return super.onOptionsItemSelected(item);
		}

	
    
	
	

	@Override
	public void onClick(View p1)
	{
		Button clicked = (Button) p1;
		if ( gameOn ) {
		if(clicked.getText().equals("Delete") ) {
			if(entry.getText().length() != 0 ) 
				entry.setText(entry.getText().subSequence(0, entry.getText().length()-1));
		} else {
			entry.setText( entry.getText() + "" + clicked.getText());
			
			if(entry.getText().toString().length() == (answer + "").length() ) {
				check();
				}
		}
		}
	}



	private void check()
	{
		if( entry.getText().equals( answer + "" ) ) {
			int correct = Integer.parseInt( right.getText().toString() ) + 1;
			right.setText( correct + "" );
		} else {
			int incorrect = Integer.parseInt( wrong.getText().toString() ) + 1;
			wrong.setText( incorrect + "" );
		}
		
		entry.setText("");
		probGen();
		
	}
	
	
	
	@Override
	protected void onResume() {
		for( int i = 0; i < rVs.length; i++)
			if( i % 2 == 0)
				rVs[i] = Integer.parseInt( prefs.getString("range" + i, "0"));
			else
				rVs[i] = Integer.parseInt( prefs.getString("range" + i, "12"));
		
		addR = rVs[1] - rVs[0];
		subR = rVs[3] - rVs[2];
		multR = rVs[5] - rVs[4];
		divR = rVs[7] - rVs[6];
		
		numbTypes = 0;
		boolean[] checked = { prefs.getBoolean("0Checked", false), prefs.getBoolean("1Checked", false) , prefs.getBoolean("2Checked", false),prefs.getBoolean("3Checked", false)};
		for( int i = 0; i < checked.length; i++) {
			if( checked[i] ) {
				probTypes[numbTypes] = i;
				numbTypes++;
			}
		}
		
		// In case none is selected
		if ( numbTypes == 0 ) {
			probTypes[0] = 0;
			numbTypes = 1;
		}
		super.onResume();
		
		if( reciever == null) {
				Log.d("A", "Reciver was null");
				reciever = new MyScheduleReciver();
		}
		
		boolean isNew = false;
		if(! reciever.isStarted()) {
			isNew = true;
			Intent i = new Intent("tcx.YOLO");
			Bundle extras = new Bundle();  
	        extras.putBoolean("start_now", prefs.getBoolean("restrictedMode", false) );  
	        i.putExtras(extras);  
			sendBroadcast(i);
			Log.d("A", "Main Started Reciver");
			
			
		}
			
		
		if (prefs.getBoolean("restrictedMode", false)) {
			startService(new Intent(MainActivity.this,AppCheckerService.class));
			if(!isNew) reciever.resumeReciver();
			
			
		}else {
			stopService(new Intent(MainActivity.this,AppCheckerService.class));
			if(!isNew) reciever.stopReciver();
		}
	}
	
	@Override
	  protected void onPause() {
	    super.onPause();
	    
	  }


	private void probGen() {
		
		int gType = probTypes[ rn.nextInt(numbTypes)];
		
		
		if( gType == 1 ){ // Subtraction
			int first = rn.nextInt(subR + 1) + rVs[2];
			int second = rn.nextInt(first - rVs[2] + 1) + rVs[2];
			answer = first - second;
			prob.setText(  first + " - " + second );
		} else if ( gType == 0 ){ // Addition
			int first = rn.nextInt(addR) + rVs[0] + 1;
			int second = rn.nextInt(addR)+ rVs[0] + 1;
			answer = first + second;
			prob.setText(  first + " + " + second );
		} else if ( gType == 2 ){ // Multiplication
			int first = rn.nextInt(multR) + rVs[4] + 1;
			int second = rn.nextInt(multR) + rVs[4] +1;
			answer = first * second;
			prob.setText(  first + " x " + second );
		 }else if ( gType == 3 ){ // Divisions
			 int second = rn.nextInt(divR) + rVs[6] + 1;
			 int first = second *( rn.nextInt(divR + 1) + rVs[6]);
			 answer = first/second;
			 prob.setText(  first + " ÷ " + second );
		}
	}
	
	private void newGame() {
		endGame();
		
		game = new Game();
		game.setDate(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(new Date()));
		
		// Get Types
		String types = "";
		for( int i = 0; i < numbTypes; i++) {
			if( i != 0 ) {
				types = types + ", ";
			}
			
			switch ( probTypes[i]) {
				case 0:
					types = types + "Addition";
					break;
				case 1:
					types = types + "Subtraction";
					break;
				case 2:
					types = types + "Multipication";
					break;
				case 3:
					types = types + "Division";
					break;
			}
			
		}
		
		game.setType( types );
		
		gameOn = true;
		probGen();
		right.setText("0");
		wrong.setText("0");
		time_left = Integer.parseInt( prefs.getString("gameLength", "80"));
		time.setText( time_left + "" );
		handler.postDelayed(runnable,1000);
		
		}
		
		

	private void endGame()
	{
		if( gameOn ) {
			
			handler.removeCallbacks( runnable );
			gameOn = false;
			
			game.setRight( right.getText().toString());
			game.setWrong( wrong.getText().toString());
			game.setPercent(Math.round( Integer.parseInt(right.getText().toString()) * 100.0/ (Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString()))) + "%");
			
			Log.d("Game:", game.getDate() + " " + game.getType() + " " + game.getRight() + " " + game.getWrong() + " " + game.getPercent());
			Toast.makeText( this.getApplicationContext() , "You got " + right.getText() + " right! That's " + game.getPercent() , Toast.LENGTH_LONG).show();
			
			db.addGame( game );
			
			int numNeed = Integer.parseInt(prefs.getString("correctNeeded", "25"));
			String timeE = prefs.getString("earnedTime","15");

			if ( Integer.parseInt( right.getText().toString() ) >= numNeed && prefs.getBoolean("restrictedMode", true)) {
//				TaskerIntent i = new TaskerIntent("SET_VARS");
//				i.addAction(ActionCodes.SET_VARIABLE).addArg("%GAME").addArg(timeE).addArg(false).addArg(false);
//
//				sendBroadcast(i);
				reciever.pauseReciever(Integer.parseInt(timeE));
				}
		}
		
		
	}

	
}
