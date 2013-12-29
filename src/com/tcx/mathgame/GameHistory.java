package com.tcx.mathgame;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class GameHistory extends Activity {
	
	private ListView list;
	private DatabaseHandler db;
	GameListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		
		list = (ListView) findViewById(R.id.gameList);
		db = new DatabaseHandler(this);
		showList();
	}

	private void showList() {
		
		ArrayList<Game> gameList = db.getAllGames();
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

		   deleteAll();
		    	return super.onOptionsItemSelected(item);
			}

		private void deleteAll() {
			ArrayList<Game> gameList = db.getAllGames();
			for( Game game : gameList ) 
				db.deleteGame(game);
			
			gameList = db.getAllGames();
			adapter = new GameListAdapter(GameHistory.this, gameList);
			list.setAdapter( adapter);
			
			
		}
	
	
	
	

}

	
	