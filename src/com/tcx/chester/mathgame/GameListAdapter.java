package com.tcx.chester.mathgame;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class GameListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Game> gameList;
	private int mGamesPerRow;
	private OnClickListener listener;

	public GameListAdapter(Context context, ArrayList<Game> list) {
		this.context = context;
		gameList = list;
		mGamesPerRow = context.getResources().getInteger(
				R.integer.games_per_row);

		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout historyMain = (RelativeLayout) v
						.findViewById(R.id.history_main);
				RelativeLayout historyExtra = (RelativeLayout) v
						.findViewById(R.id.history_extra);
				TextView types = (TextView) (historyExtra
						.findViewById(R.id.history_types_string));

				if (historyMain.getVisibility() == View.VISIBLE) {
					historyExtra.setVisibility(View.VISIBLE);
					historyMain.setVisibility(View.INVISIBLE);
					types.setSelected(true);

				} else {
					historyExtra.setVisibility(View.INVISIBLE);
					historyMain.setVisibility(View.VISIBLE);
				}

			}
		};

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
		firstGameLayout.setOnClickListener(listener);
		resestView(firstGame);
		if (secondGame != null) {
			if (index + 1 < gameList.size()) {
				secondGame.setVisibility(View.VISIBLE);
				updateView(secondGame, gameList.get(index + 1));
				GameLayout secondGameLayout = (GameLayout) secondGame;
				secondGameLayout.setIndex(index + 1);
				secondGameLayout.setOnClickListener(listener);
				resestView(secondGame);
			} else {
				secondGame.setVisibility(View.INVISIBLE);
			}
		}

		return convertView;
	}

	private void updateView(View view, final Game game) {
		TextView right = (TextView) (view
				.findViewById(R.id.history_right_number));
		TextView wrong = (TextView) (view
				.findViewById(R.id.history_wrong_number));
		TextView percent = (TextView) (view
				.findViewById(R.id.history_percent_number));
		TextView date = (TextView) (view.findViewById(R.id.history_date));
		TextView mistakes = (TextView) (view
				.findViewById(R.id.history_mistakes_string));
		TextView types = (TextView) (view
				.findViewById(R.id.history_types_string));

		CheckBox incluMistakes = (CheckBox) (view
				.findViewById(R.id.history_mistakes_cb));
		
		incluMistakes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					game.setInclude("1");
				else
					game.setInclude("0");
				DatabaseHandler db = new DatabaseHandler(context);
				db.updateGame(game);
				db.close();
				
			}
		});

		if (game.getInclude().equals("0"))
			incluMistakes.setChecked(false);
		else
			incluMistakes.setChecked(true);

		right.setText(game.getRight());
		wrong.setText(game.getWrong());
		percent.setText(game.getPercent());
		date.setText(game.getDate());
		String noSpace = game.getProbsWrong().replace(" ", "");
		mistakes.setText(noSpace.replace(",", ", "));
		types.setText(game.getType());

	}

	private void resestView(View v) {
		RelativeLayout historyMain = (RelativeLayout) v
				.findViewById(R.id.history_main);
		RelativeLayout historyExtra = (RelativeLayout) v
				.findViewById(R.id.history_extra);

		historyExtra.setVisibility(View.INVISIBLE);
		historyMain.setVisibility(View.VISIBLE);

	}

}
