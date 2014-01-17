package com.tcx.mathgame;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GameListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Game> gameList;
	private int mGamesPerRow;

	public GameListAdapter(Context context, ArrayList<Game> list) {
		this.context = context;
		gameList = list;
		mGamesPerRow = context.getResources().getInteger(
				R.integer.games_per_row);

	}

	@Override
	public int getCount() {
		if (mGamesPerRow == 1)
			return gameList.size();
		else
			return (gameList.size() + 1) / 2;
	}

	@Override
	public Object getItem(int position) {
		return gameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// TextView date = (TextView) convertView.findViewById(R.id.tv_date);
		// date.setText(game.getDate());
		//
		// TextView type = (TextView) convertView.findViewById(R.id.tv_type);
		// type.setText(game.getType());
		//
		// TextView right = (TextView) convertView.findViewById(R.id.tv_right);
		// right.setText(game.getRight());
		//
		// TextView wrong = (TextView) convertView.findViewById(R.id.tv_wrong);
		// wrong.setText(game.getWrong());
		//
		// TextView percent = (TextView)
		// convertView.findViewById(R.id.tv_percent);
		// percent.setText(game.getPercent());
		//
		// TextView probsWrong = (TextView)
		// convertView.findViewById(R.id.tv_mistakes);
		// probsWrong.setText(game.getProbsWrong());

		int index = position * mGamesPerRow;
		if (index < 0 || index >= gameList.size()) {
			return null;
		}

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// convertView = inflater.inflate(R.layout.list_row, null);
			convertView = inflater.inflate(R.layout.history_list_row, null);

		}

		View secondGame = convertView.findViewById(R.id.game_two);
		View firstGame = convertView.findViewById(R.id.game_one);
		updateView(firstGame, gameList.get(index));
		GameLayout firstGameLayout = (GameLayout) firstGame;
		firstGameLayout.setIndex(index);
		if (secondGame != null) {
			if (index + 1 < gameList.size()) {
				secondGame.setVisibility(View.VISIBLE);
				updateView(secondGame, gameList.get(index + 1));
				GameLayout secondGameLayout = (GameLayout) secondGame;
				secondGameLayout.setIndex(index + 1);
			} else {
				secondGame.setVisibility(View.INVISIBLE);
			}
		}

		return convertView;
	}

	private void updateView(View view, Game game) {
		TextView right = (TextView) (view
				.findViewById(R.id.history_right_number));
		TextView wrong = (TextView) (view
				.findViewById(R.id.history_wrong_number));
		TextView percent = (TextView) (view
				.findViewById(R.id.history_percent_number));
		TextView date = (TextView) (view.findViewById(R.id.history_date));
		TextView mistakes = (TextView) (view
				.findViewById(R.id.history_mistakes_string));
		TextView types = (TextView) (view.findViewById(R.id.history_types_string));

		right.setText(game.getRight());
		wrong.setText(game.getWrong());
		percent.setText(game.getPercent());
		date.setText(game.getDate());
		String noSpace = game.getProbsWrong().replace(" ", "");
		mistakes.setText(noSpace.replace("," , ", "));
		types.setText(game.getType());

	}

}
