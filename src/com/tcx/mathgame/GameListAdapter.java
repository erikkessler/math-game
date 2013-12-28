package com.tcx.mathgame;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GameListAdapter extends BaseAdapter{

	Context context;
	ArrayList<Game> gameList;
	
	public GameListAdapter( Context context, ArrayList<Game> list) {
		this.context = context;
		gameList = list;
		
	}

	@Override
	public int getCount() {
		return gameList.size();
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
		Game game = gameList.get(position);
		
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);
            
		}
		
		TextView date = (TextView) convertView.findViewById(R.id.tv_date);
		date.setText(game.getDate());
		
		TextView type = (TextView) convertView.findViewById(R.id.tv_type);
		type.setText(game.getType());
		
		TextView right = (TextView) convertView.findViewById(R.id.tv_right);
		right.setText(game.getRight());
		
		TextView wrong = (TextView) convertView.findViewById(R.id.tv_wrong);
		wrong.setText(game.getWrong());
		
		TextView percent = (TextView) convertView.findViewById(R.id.tv_percent);
		percent.setText(game.getPercent());
		
		
		return convertView;
	}
	
}
