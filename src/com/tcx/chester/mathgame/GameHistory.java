package com.tcx.chester.mathgame;

import java.util.ArrayList;
import java.util.Collections;

import com.tcx.chester.mathgame.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		list.setAdapter(adapter);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Include All Mistakes");
		menu.add(0, 1, 1, "Exclude All Mistakes");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
		} else if(item.getItemId() == 0) {
			checkAll("1");
		} else if(item.getItemId() == 1) {
			checkAll("0");
		}
		return super.onOptionsItemSelected(item);
	}

	private void checkAll(String include) {
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<Game> gameList = db.getAllGames();
		for (Game game : gameList) {
			game.setInclude(include);
			db.updateGame(game);
		}
		db.close();
		showList();
		
	}


}
