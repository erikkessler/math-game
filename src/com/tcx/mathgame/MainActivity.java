package com.tcx.mathgame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button[] buttons = new Button[11];
	private TextView entry, prob;
	private TextView right, wrong;
	private TextView time;
	private int answer;
	private int time_left;
	private Runnable runnable;
	private Handler handler;
	private boolean gameOn;
	private SharedPreferences prefs;
	private String subR, addR, multR, divR;
	private Object[] probTypes;
	private int numbTypes;
	private Random rn;
	private String mistakes;
	private Boolean screenChanged;
	private int first, second;
	private String types, date;
	private String[] oldMistakes;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		rn = new Random();

		entry = (TextView) findViewById(R.id.entry);
		prob = (TextView) findViewById(R.id.problem);
		right = (TextView) findViewById(R.id.right);
		wrong = (TextView) findViewById(R.id.wrong);
		time = (TextView) findViewById(R.id.timer);

		for (int i = 0; i < buttons.length; i++) {
			String a = "b" + i;
			buttons[i] = (Button) findViewById(getResources().getIdentifier(a,
					"id", getPackageName()));
			buttons[i].setOnClickListener(this);
		}

		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				time_left--;
				setTime();
				if (time_left == 0) {
					endGame();
				} else {
					handler.postDelayed(this, 1000);
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, 0, 0, "New Game");
		menu.add(1, 1, 1, "End Game");
		menu.add(1, 2, 2, "Main Screen");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:
			newGame();
			return true;

		case 1:
			endGame();
			return true;

		case 2:
			onBackPressed();
			finish();
			return true;
		case android.R.id.home:
			endGame();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View p1) {
		Button clicked = (Button) p1;
		if (gameOn) {
			if (clicked.getId() == R.id.b10) {
				if (entry.getText().length() != 0)
					entry.setText(entry.getText().subSequence(0,
							entry.getText().length() - 1));
			} else {
				entry.setText(entry.getText() + "" + clicked.getText());

				if (entry.getText().toString().length() == (answer + "")
						.length()) {
					check();
				}
			}
		}
	}

	private void check() {
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				entry.setTextColor(Color.BLACK);
				entry.setText("");

			}

		};

		if (entry.getText().equals(answer + "")) {
			int correct = Integer.parseInt(right.getText().toString()) + 1;
			right.setText(correct + "");
			// background.setBackgroundColor(Color.GREEN);
			entry.setTextColor(Color.parseColor("#50C900"));
			handler.postDelayed(runable, 400);
		} else {
			if (mistakes.equals(""))
				mistakes = prob.getText() + "";
			else
				mistakes = mistakes + ", " + prob.getText();
			int incorrect = Integer.parseInt(wrong.getText().toString()) + 1;
			wrong.setText(incorrect + "");
			entry.setTextColor(Color.parseColor("#FF1607"));
			handler.postDelayed(runable, 400);

		}

		probGen();

	}

	@Override
	protected void onResume() {
		super.onResume();

		addR = prefs.getString("pref_key_add_diff", "Easy");
		subR = prefs.getString("pref_key_sub_diff", "Easy");
		multR = prefs.getString("pref_key_mult_diff", "Easy");
		divR = prefs.getString("pref_key_div_diff", "Easy");

		Set<String> set = new HashSet<String>();
		set.add("Addition");
		probTypes = prefs.getStringSet("pref_key_game_type", set).toArray();

		getMistakes();

		if (!screenChanged) {
			newGame();
		}
	}

	private void getMistakes() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		ArrayList<Game> gameList = db.getAllGames();
		db.close();
		String mMistakes = "";
		for (Game game : gameList) {
			if (!game.getProbsWrong().equals("")) {
				if (mMistakes.equals(""))
					mMistakes = game.getProbsWrong();
				else
					mMistakes = mMistakes + ", " + game.getProbsWrong();
			}
		}
		oldMistakes = mMistakes.split(", ");
		Log.d("YOLO", oldMistakes.length + "");

	}

	private void probGen() {

		String gType = (String) probTypes[rn.nextInt(probTypes.length)];

		if (gType.equals("Mistakes") && (oldMistakes[0].length() == 0)) {

			if (probTypes.length == 1) {
				Log.d("YOLO", gType);
				gType = "Addition";
			} else {
				probGen();
				return;
			}
		}

		if (gType.equals("Subtraction")) { // Subtraction
			if (subR.equals("Easy")) {
				first = rn.nextInt(9) + 1;
				second = rn.nextInt(first + 1);
				answer = first - second;
				prob.setText(first + " - " + second);

			} else if (subR.equals("Medium")) {
				first = rn.nextInt(18) + 3;
				if (first == 4)
					if (rn.nextBoolean())
						first = 16;
					else if (first == 5)
						if (rn.nextBoolean())
							first = 17;
						else if (first == 6)
							if (rn.nextBoolean())
								first = 18;
							else if (first == 7)
								if (rn.nextBoolean())
									first = 15;
								else if (first == 8)
									if (rn.nextBoolean())
										first = 14;
									else if (first == 10)
										if (rn.nextBoolean())
											first = 20;

				second = rn.nextInt(first - 1) + 2;
				if (first - second == 0 || first - second == 2
						|| first - second == 1)
					if (rn.nextInt(3) <= 3)
						probGen();
					else {
						answer = first - second;
						prob.setText(first + " - " + second);
					}
				else {
					answer = first - second;
					prob.setText(first + " - " + second);
				}

			} else if (subR.equals("Hard")) {
				first = rn.nextInt(36) + 15;
				second = rn.nextInt(first - 6) + 5;
				if (first - second == 0 || first - second == 1) {
					probGen();

				} else if (first - second == 10) {
					if (rn.nextBoolean())
						probGen();
					else {
						answer = first - second;
						prob.setText(first + " - " + second);
					}

				} else {
					answer = first - second;
					prob.setText(first + " - " + second);
				}

			} else if (subR.equals("Expert")) {
				first = rn.nextInt(51) + 40;
				second = rn.nextInt(first - 39) + 40;
				if (first - second == 0 || first - second == 1
						|| first - second == 2) {
					probGen();

				} else if (first - second == 10) {
					if (rn.nextBoolean())
						probGen();
					else {
						answer = first - second;
						prob.setText(first + " - " + second);
					}

				} else {
					answer = first - second;
					prob.setText(first + " - " + second);
				}

			}

		} else if (gType.equals("Addition")) { // Addition
			if (addR.equals("Easy")) {
				first = rn.nextInt(9) + 1;
				second = rn.nextInt(10);
				answer = first + second;
				prob.setText(first + " + " + second);
			} else if (addR.equals("Medium")) {
				first = rn.nextInt(19) + 2;
				second = rn.nextInt(19) + 2;
				if (first + second == 4 || first + second == 5
						|| first + second == 6 || first + second == 7) {
					probGen();

				} else {
					answer = first + second;
					prob.setText(first + " + " + second);
				}
			} else if (addR.equals("Hard")) {
				first = rn.nextInt(46) + 5;
				second = rn.nextInt(45) + 6;
				if (first + second < 20) {
					probGen();

				} else if (first + second <= 30) {
					if (rn.nextBoolean())
						probGen();
					else {
						answer = first + second;
						prob.setText(first + " + " + second);
					}

				} else {
					answer = first + second;
					prob.setText(first + " + " + second);
				}
			} else if (addR.equals("Expert")) {
				first = rn.nextInt(50) + 40;
				second = rn.nextInt(50) + 40;
				if (first + second < 100) {
					probGen();

				} else if (first == second) {
					if (rn.nextBoolean())
						probGen();
					else {
						answer = first + second;
						prob.setText(first + " + " + second);
					}

				} else {
					answer = first + second;
					prob.setText(first + " + " + second);
				}
			}

		} else if (gType.equals("Multiplication")) { // Multiplication
			if (multR.equals("Easy")) {
				first = rn.nextInt(4) + 1;
				second = rn.nextInt(4) + 1;
				answer = first * second;
				prob.setText(first + " x " + second);
			} else if (multR.equals("Medium")) {
				first = rn.nextInt(7) + 2;
				if (first == 2)
					if (rn.nextBoolean())
						first = 7;
					else if (first == 3)
						if (rn.nextBoolean())
							first = 8;
				second = rn.nextInt(7) + 2;
				if (second == 2)
					if (rn.nextBoolean())
						second = 7;
					else if (second == 3)
						if (rn.nextBoolean())
							second = 8;
				if (first * second == 4 || first * second == 6)
					probGen();

				else {
					answer = first * second;
					prob.setText(first + " x " + second);
				}
			} else if (multR.equals("Hard")) {
				first = rn.nextInt(11) + 2;
				if (first == 10)
					if (rn.nextBoolean())
						first = 12;
				second = rn.nextInt(11) + 2;
				if (second == 10)
					if (rn.nextBoolean())
						second = 9;

				if (first * second < 10)
					probGen();

				else {
					answer = first * second;
					prob.setText(first + " x " + second);
				}
			} else if (multR.equals("Expert")) {
				first = rn.nextInt(6) + 10;
				second = rn.nextInt(6) + 10;
				answer = first * second;
				prob.setText(first + " x " + second);
			}

		} else if (gType.equals("Division")) { // Divisions
			if (divR.equals("Easy")) {
				second = rn.nextInt(3) + 1;
				first = second * (rn.nextInt(3) + 1);
				answer = first / second;
				prob.setText(first + " ÷ " + second);
			} else if (divR.equals("Medium")) {
				second = rn.nextInt(7) + 2;
				first = second * (rn.nextInt(7) + 2);

				if (first / second == 1)
					probGen();

				else {
					answer = first / second;
					prob.setText(first + " ÷ " + second);
				}
			} else if (divR.equals("Hard")) {
				second = rn.nextInt(9) + 4;
				if (second == 10)
					if (rn.nextBoolean())
						second = 12;
				first = second * (rn.nextInt(9) + 4);

				if (first / second == 10) {
					if (rn.nextBoolean())
						probGen();
					else {
						answer = first / second;
						prob.setText(first + " ÷ " + second);
					}

				} else {
					answer = first / second;
					prob.setText(first + " ÷ " + second);
				}

			} else if (divR.equals("Expert")) {
				second = rn.nextInt(9) + 7;
				if (second == 10)
					if (rn.nextBoolean())
						second = 12;
				first = second * (rn.nextInt(9) + 7);

				if (first / second == 10) {
					if (rn.nextBoolean())
						probGen();
					else {
						answer = first / second;
						prob.setText(first + " ÷ " + second);
					}

				} else {
					answer = first / second;
					prob.setText(first + " ÷ " + second);
				}
			}
		} else if (gType.equals("Mistakes")) {
			String mProb = oldMistakes[rn.nextInt(oldMistakes.length)];
			prob.setText(mProb);
			setAnswer(mProb);
		}
	}

	private void setAnswer(String mProb) {
		int mFirst = Integer.parseInt(mProb.substring(0, mProb.indexOf(" ")));
		int mSecond = Integer.parseInt(mProb.substring(mProb.indexOf(" ",
				mProb.indexOf(" ") + 1) + 1));

		if (mProb.contains("+"))
			answer = mFirst + mSecond;
		else if (mProb.contains("-"))
			answer = mFirst - mSecond;
		else if (mProb.contains("x"))
			answer = mFirst * mSecond;
		else if (mProb.contains("÷"))
			answer = mFirst / mSecond;
	}

	private void newGame() {
		endGame();
		mistakes = "";

		date = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.US)
				.format(new Date());

		// Get Types
		types = "";
		for (int i = 0; i < probTypes.length; i++) {
			if (i != 0) {
				types = types + ", ";
			}

			if (((String) probTypes[i]).equals("Addition")) {
				types = types + "Addition " + "(" + addR.substring(0, 2) + ")";

			} else if (((String) probTypes[i]).equals("Subtraction")) {
				types = types + "Subtraction " + "(" + subR.substring(0, 2)
						+ ")";
			} else if (((String) probTypes[i]).equals("Multiplication")) {
				types = types + "Multipication " + "(" + multR.substring(0, 2)
						+ ")";
			} else if (((String) probTypes[i]).equals("Division")) {
				types = types + "Division " + "(" + divR.substring(0, 2) + ")";
			} else if (((String) probTypes[i]).equals("Mistakes")) {
				types = types + "Mistakes";
			}

		}

		gameOn = true;
		probGen();
		right.setText("0");
		wrong.setText("0");
		time_left = prefs.getInt("pref_key_game_length", 80);
		setTime();
		handler.postDelayed(runnable, 1000);

	}

	private void setTime() {
		String min = time_left / 60 + "";
		String sec = time_left % 60 + "";
		if (sec.length() == 1)
			sec = "0" + sec;

		time.setText(min + ":" + sec);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("YOLO", "Save stuff");
		outState.putString("right", right.getText().toString());
		outState.putString("wrong", wrong.getText().toString());
		outState.putString("prob", prob.getText().toString());
		outState.putString("enty", entry.getText().toString());
		outState.putString("time", time.getText().toString());
		outState.putInt("time_left", time_left);
		outState.putString("mistakes", mistakes);
		outState.putString("types", types);
		outState.putBoolean("gameon", gameOn);
		outState.putString("date", date);
		outState.putInt("answer", answer);
		handler.removeCallbacks(runnable);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d("YOLO", "Restore stuff");
		right.setText(savedInstanceState.getString("right"));
		wrong.setText(savedInstanceState.getString("wrong"));
		prob.setText(savedInstanceState.getString("prob"));
		entry.setText(savedInstanceState.getString("entry"));
		time.setText(savedInstanceState.getString("time"));
		screenChanged = true;
		mistakes = savedInstanceState.getString("mistakes");
		types = savedInstanceState.getString("types");
		time_left = savedInstanceState.getInt("time_left");
		gameOn = savedInstanceState.getBoolean("gameon");
		date = savedInstanceState.getString("date");
		answer = savedInstanceState.getInt("answer");
		if (gameOn)
			handler.postDelayed(runnable, 1000);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("YOLO", "Back Pressed");
		endGame();
	}

	private void endGame() {
		if (gameOn) {

			prob.setText("Game Over");

			handler.removeCallbacks(runnable);
			gameOn = false;

			Game game = new Game();
			game.setType(types);
			game.setDate(date);
			game.setRight(right.getText().toString());
			game.setWrong(wrong.getText().toString());
			game.setPercent(Math.round(Integer.parseInt(right.getText()
					.toString())
					* 100.0
					/ (Integer.parseInt(right.getText().toString()) + Integer
							.parseInt(wrong.getText().toString())))
					+ "%");
			game.setProbsWrong(mistakes);

			Log.d("Game:",
					game.getDate() + " " + game.getType() + " "
							+ game.getRight() + " " + game.getWrong() + " "
							+ game.getPercent());

			if (!right.getText().toString().equals("0")
					|| !wrong.getText().toString().equals("0")) {
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				db.addGame(game);
				db.close();
			}

			int numNeed = prefs.getInt("pref_key_correct_needed", 25);
			String timeE = prefs.getInt("pref_key_earned_time", 15) + "";

			if (Integer.parseInt(right.getText().toString()) >= numNeed
					&& prefs.getBoolean("pref_key_restricted_mode", false)) {
				MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
						R.raw.ronniecall);
				mp.start();
				Intent i = new Intent("tcx.PAUSE");
				Bundle extras = new Bundle();
				extras.putInt("time", Integer.parseInt(timeE));
				i.putExtras(extras);
				sendBroadcast(i);
				Toast.makeText(
						this.getApplicationContext(),
						"You got " + right.getText() + " right! That's "
								+ game.getPercent() + "\nYou get " + timeE
								+ " minutes to play!", Toast.LENGTH_LONG)
						.show();

			} else {
				if (!right.getText().toString().equals("0")
						|| !wrong.getText().toString().equals("0"))
					Toast.makeText(
							this.getApplicationContext(),
							"You got " + right.getText() + " right! That's "
									+ game.getPercent(), Toast.LENGTH_LONG)
							.show();

			}
		}

	}

}
