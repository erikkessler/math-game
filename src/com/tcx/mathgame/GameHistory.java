package com.tcx.mathgame;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameHistory extends Activity {
	
	private ListView list;
	GameListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		list = (ListView) findViewById(R.id.gameList);
		showList();
		
	}
	

	private void showList() {
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<Game> gameList = db.getAllGames();
		db.close();
		Collections.reverse(gameList);
		adapter = new GameListAdapter(GameHistory.this, gameList);
		list.setAdapter( adapter);
		
		
		
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {

		    menu.add(1, 0, 0, "Delete History");
		
		    return super.onCreateOptionsMenu(menu); 
	    }
		
		 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			 if ( item.getItemId() == 0)
				 deleteAll();
			 else if(item.getItemId() == android.R.id.home) {
				 NavUtils.navigateUpFromSameTask(this);
			 }
		    return super.onOptionsItemSelected(item);
			}

		


		private void deleteAll() {
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			ArrayList<Game> gameList = db.getAllGames();
			for( Game game : gameList ) 
				db.deleteGame(game);
			
			gameList = db.getAllGames();
			adapter = new GameListAdapter(GameHistory.this, gameList);
			list.setAdapter( adapter);
			db.close();
			
			
		}
	
	public void showWrong(View v) {
		GameLayout gameLayout = (GameLayout) v;
		RelativeLayout historyMain = (RelativeLayout) v.findViewById(R.id.history_main);
		RelativeLayout historyExtra = (RelativeLayout) v.findViewById(R.id.history_extra);
		TextView types = (TextView) (historyExtra.findViewById(R.id.history_types_string));
		if( historyMain.getVisibility() == View.VISIBLE ) {
			historyExtra.setVisibility(View.VISIBLE);
			historyMain.setVisibility(View.INVISIBLE);
			types.setSelected(true);
			
		} else{
			historyExtra.setVisibility(View.INVISIBLE);
			historyMain.setVisibility(View.VISIBLE);
		}
		
		
	}
	
	

}

	
	