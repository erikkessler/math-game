package com.tcx.chester.mathgame;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "gameManager";

	// Contacts table name
	private static final String TABLE_GAMES = "games";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_TYPE = "type";
	private static final String KEY_RIGHT = "right";
	private static final String KEY_WRONG = "wrong";
	private static final String KEY_PERCENT = "percent";
	private static final String KEY_PROBS_WRONG = "probs";
	private static final String KEY_INCLUDE = "include";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
				+ KEY_TYPE + " TEXT," + KEY_RIGHT + " TEXT," + KEY_WRONG
				+ " TEXT," + KEY_PERCENT + " TEXT," + KEY_PROBS_WRONG
				+ " TEXT," + KEY_INCLUDE + " TEXT" + ")";
		db.execSQL(CREATE_GAMES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*
		 * // Drop older table if existed db.execSQL("DROP TABLE IF EXISTS " +
		 * TABLE_GAMES);
		 * 
		 * // Create tables again onCreate(db);
		 */

		// If you need to add a column
		if (newVersion > oldVersion) {
			db.execSQL("ALTER TABLE games ADD COLUMN " + KEY_INCLUDE
					+ " INTEGER DEFAULT 1");
		}
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, game.getDate());
		values.put(KEY_TYPE, game.getType());
		values.put(KEY_RIGHT, game.getRight());
		values.put(KEY_WRONG, game.getWrong());
		values.put(KEY_PERCENT, game.getPercent());
		values.put(KEY_PROBS_WRONG, game.getProbsWrong());
		values.put(KEY_INCLUDE, game.getInclude());

		// Inserting Row
		db.insert(TABLE_GAMES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Game getGame(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_GAMES, new String[] { KEY_ID, KEY_DATE,
				KEY_TYPE, KEY_RIGHT, KEY_WRONG, KEY_PERCENT, KEY_PROBS_WRONG, KEY_INCLUDE },
				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Game game = new Game();
		game.setId(Integer.parseInt(cursor.getString(0)));
		game.setDate(cursor.getString(1));
		game.setType(cursor.getString(2));
		game.setRight(cursor.getString(3));
		game.setWrong(cursor.getString(4));
		game.setPercent(cursor.getString(5));
		game.setProbsWrong(cursor.getString(6));
		game.setInclude(cursor.getString(7));

		// return contact
		return game;
	}

	// Getting All Contacts
	public ArrayList<Game> getAllGames() {
		ArrayList<Game> gameList = new ArrayList<Game>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_GAMES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Game game = new Game();
				game.setId(Integer.parseInt(cursor.getString(0)));
				game.setDate(cursor.getString(1));
				game.setType(cursor.getString(2));
				game.setRight(cursor.getString(3));
				game.setWrong(cursor.getString(4));
				game.setPercent(cursor.getString(5));
				game.setProbsWrong(cursor.getString(6));
				game.setInclude(cursor.getString(7));

				// Adding contact to list
				gameList.add(game);
			} while (cursor.moveToNext());
		}

		// return contact list
		return gameList;
	}

	// Updating single contact
	public int updateGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, game.getDate());
		values.put(KEY_TYPE, game.getType());
		values.put(KEY_RIGHT, game.getRight());
		values.put(KEY_WRONG, game.getWrong());
		values.put(KEY_PERCENT, game.getPercent());
		values.put(KEY_PROBS_WRONG, game.getProbsWrong());
		values.put(KEY_INCLUDE, game.getInclude());

		// updating row
		return db.update(TABLE_GAMES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(game.getId()) });
	}

	// Deleting single contact
	public void deleteGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GAMES, KEY_ID + " = ?",
				new String[] { String.valueOf(game.getId()) });
		db.close();
	}

	// Getting contacts Count
	public int getGamesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_GAMES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
