package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;
import java.util.*;
import org.apache.commons.logging.*;

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
	private final int LENGTH = 80;
	private int time_left = LENGTH;
	private Runnable runnable;
	private Handler handler;
	private boolean gameOn;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		
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
		
		probGen();
		
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
		
		newGame();
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
		if( type.equals("Subtraction") ){
			Random rn = new Random();
			int first = rn.nextInt(20) + 1;
			int second = rn.nextInt(first + 1);
			answer = first - second;
			prob.setText(  first + " - " + second );
		}
		
	}
	
	private void newGame() {
		gameOn = true;
		time_left = LENGTH;
		handler.postDelayed(runnable,1000);
		
		}
		
		

	private void endGame()
	{
		gameOn = false;
		Toast.makeText( this.getApplicationContext() , "You got " + right.getText() + " right!", Toast.LENGTH_LONG).show();
	}

	
}
