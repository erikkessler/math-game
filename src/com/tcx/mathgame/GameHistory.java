package com.tcx.mathgame;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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
	
	
	
	

}

	
	