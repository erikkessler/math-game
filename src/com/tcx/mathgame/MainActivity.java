package com.tcx.mathgame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
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
	private Random rn;
	private Game game;
	private DatabaseHandler db;
	private MyScheduleReciver reciever;
	private String mistakes;
	private int first, second;
	
	int newGameId = Menu.FIRST;
	int endGameId = Menu.FIRST + 1;
	int mainId = Menu.FIRST +2;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		prefs = getSharedPreferences("MyPrefsFile",0);
		rn = new Random();
		
		entry = (TextView) findViewById(R.id.entry);
		prob = (TextView) findViewById(R.id.problem);
		right = (TextView) findViewById(R.id.right);
		wrong = (TextView) findViewById(R.id.wrong);
		time = (TextView) findViewById(R.id.timer);
		
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
	    menu.add( group1Id, endGameId, endGameId, "End Game");
	    menu.add( group1Id, mainId, mainId, "Main Screen");
	   
	
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
			return true;
	    	
		case 3:
	    	Intent intent1 = new Intent( this , HomeScreen.class );
			startActivity( intent1 );
			return true;
			
			
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
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				//background.setBackgroundColor(color.background_light);
				entry.setTextColor(Color.BLACK);
				entry.setText("");	
				
			}
			
		};
		if( entry.getText().equals( answer + "" ) ) {
			int correct = Integer.parseInt( right.getText().toString() ) + 1;
			right.setText( correct + "" );
			//background.setBackgroundColor(Color.GREEN);
			entry.setTextColor(Color.parseColor("#50C900"));
			handler.postDelayed(runable, 400);
		} else {
			mistakes = mistakes + prob.getText() + "\n";
			int incorrect = Integer.parseInt( wrong.getText().toString() ) + 1;
			wrong.setText( incorrect + "" );
			entry.setTextColor(Color.parseColor("#FF1607"));
			handler.postDelayed(runable, 400);
			
		}
		
		
		probGen();
		
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
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
		
		
			
		
//		if (prefs.getBoolean("restrictedMode", false)) {
//			startService(new Intent(MainActivity.this,AppCheckerService.class));
//			HomeScreen.reciever.resumeReciver();
//			
//			
//		}else {
//			stopService(new Intent(MainActivity.this,AppCheckerService.class));
//			HomeScreen.reciever.stopReciver();
//		}
		
		newGame();
	}
	
	@Override
	  protected void onPause() {
		endGame();
	    super.onPause();
	    
	  }


	private void probGen() {
		
		int gType = probTypes[ rn.nextInt(numbTypes)];
		
		
		if( gType == 1 ){ // Subtraction
			first = rn.nextInt(subR) + rVs[2] + 1;
			second = rn.nextInt(first - rVs[2] + 1) + rVs[2];
			answer = first - second; 
			prob.setText(  first + " - " + second );
			
		} else if ( gType == 0 ){ // Addition
			first = rn.nextInt(addR) + rVs[0] + 1;
			second = rn.nextInt(addR)+ rVs[0] + 1;
			answer = first + second;
			prob.setText(  first + " + " + second );
			
		} else if ( gType == 2 ){ // Multiplication
			first = rn.nextInt(multR) + rVs[4] + 1;
			second = rn.nextInt(multR) + rVs[4] +1;
			answer = first * second;
			prob.setText(  first + " x " + second );
			
		 }else if ( gType == 3 ){ // Divisions
			 second = rn.nextInt(divR) + rVs[6] + 1;
			 first = second *( rn.nextInt(divR + 1) + rVs[6]);
			 answer = first/second;
			 prob.setText(  first + " ÷ " + second );

		}
	}
	
	private void newGame() {
		endGame();
		mistakes = "";
		
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
			
			prob.setText("Game Over");
			
			handler.removeCallbacks( runnable );
			gameOn = false;
			
			game.setRight( right.getText().toString());
			game.setWrong( wrong.getText().toString());
			game.setPercent(Math.round( Integer.parseInt(right.getText().toString()) * 100.0/ (Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString()))) + "%");
			game.setProbsWrong(mistakes);
			
			Log.d("Game:", game.getDate() + " " + game.getType() + " " + game.getRight() + " " + game.getWrong() + " " + game.getPercent());
			
			db.addGame( game );
			
			int numNeed = Integer.parseInt(prefs.getString("correctNeeded", "25"));
			String timeE = prefs.getString("earnedTime","15");

			if ( Integer.parseInt( right.getText().toString() ) >= numNeed && prefs.getBoolean("restrictedMode", true)) {
					MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ronniecall);
				    mp.start();
				    HomeScreen.reciever.pauseReciever(Integer.parseInt(timeE));
				    Toast.makeText( this.getApplicationContext() , "You got " + right.getText() + " right! That's " + game.getPercent() +"\nYou get " + timeE + " minutes to play!" , Toast.LENGTH_LONG).show();

				} else{
					Toast.makeText( this.getApplicationContext() , "You got " + right.getText() + " right! That's " + game.getPercent() , Toast.LENGTH_LONG).show();

				}
		}
		
		
	}

	
}
