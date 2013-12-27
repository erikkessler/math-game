package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;
import java.util.*;
import org.apache.commons.logging.*;
import android.content.*;

public class MainActivity extends Activity implements OnClickListener
{
	
	private Button[] buttons = new Button[11];
	private TextView entry;
	private TextView prob;
	private TextView right;
	private TextView wrong;
	private TextView time;
	private int answer;
	private String type = "Subtraction";
	private int LENGTH = 10;
	private int time_left = LENGTH;
	private Runnable runnable;
	private Handler handler;
	private boolean gameOn;
	private int group1Id = 1;
	private SharedPreferences prefs;

	int homeId = Menu.FIRST;
	int profileId = Menu.FIRST +1;
	

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    menu.add(group1Id, homeId, homeId, "New Game");
    menu.add(group1Id, profileId, profileId, "Settings");
   

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
		
	default:
    	break;

       }
    	return super.onOptionsItemSelected(item);
	}

	
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
	
	private void probGen() {
		String gType = prefs.getString("gameType","Subtraction");
		if( gType.equals("Subtraction") ){
			Random rn = new Random();
			int first = rn.nextInt(20) + 1;
			int second = rn.nextInt(first + 1);
			answer = first - second;
			prob.setText(  first + " - " + second );
		} else if ( gType.equals("Addition") ){
			Random rn = new Random();
			int first = rn.nextInt(20) + 1;
			int second = rn.nextInt(20)+ 1;
			answer = first + second;
			prob.setText(  first + " + " + second );
		} else if ( gType.equals("Multiplication") ){
			Random rn = new Random();
			int first = rn.nextInt(12) + 1;
			int second = rn.nextInt(12) + 1;
			answer = first * second;
			prob.setText(  first + " × " + second );
		 }else if ( gType.equals("Division") ){
			 Random rn = new Random();
			 int first = rn.nextInt(12) + 1;
			 int second = rn.nextInt(12) + 1;
			 answer = first / second;
			 prob.setText(  first + " ÷ " + second );
		}
	}
	
	private void newGame() {
		endGame();
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
			Toast.makeText( this.getApplicationContext() , "You got " + right.getText() + " right!", Toast.LENGTH_LONG).show();
			int numNeed = Integer.parseInt(prefs.getString("correctNeeded", "25"));
			String timeE = prefs.getString("earnedTime","15");

			if ( Integer.parseInt( right.getText().toString() ) >= numNeed && prefs.getBoolean("restrictedMode", true)) {
				TaskerIntent i = new TaskerIntent("SET_VARS");
				i.addAction(ActionCodes.SET_VARIABLE).addArg("%GAME").addArg(timeE).addArg(false).addArg(false);

				sendBroadcast(i);
				}
		}
		
		
	}

	
}
