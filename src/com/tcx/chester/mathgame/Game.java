package com.tcx.chester.mathgame;

public class Game {

	int mId;
	String mDate;
	String mType;
	String mRight;
	String mWrong;
	String mPercent;
	String mProbsWrong;
	String mInclude;
	
	public Game() {
		
	}
	
	public int getId() {
		return mId;
	}
	
	public void setId( int id ) {
		mId = id;
	}
	
	public String getDate() {
		return mDate;
	}
	
	public void setDate( String date ) {
		mDate = date;
	}
	public String getType() {
		return mType;
	}
	
	public void setType( String type ) {
		mType = type;
	}
	
	public String getRight() {
		return mRight;
	}
	
	public void setRight( String right ) {
		mRight = right;
	}
	
	public String getWrong() {
		return mWrong;
	}
	
	public void setWrong( String wrong ) {
		mWrong = wrong;
	}
	
	public String getPercent() {
		return mPercent;
	}
	
	public void setPercent( String percent ) {
		mPercent = percent;
	}
	
	public String getProbsWrong() {
		return mProbsWrong;
	}
	
	public void setProbsWrong( String wrong ) {
		mProbsWrong = wrong;
	}
	
	public String getInclude() {
		return mInclude;
	}
	
	public void setInclude( String include ) {
		mInclude = include;
	}
	
}
