package gruppe16;

import java.util.*;
import java.text.SimpleDateFormat;

class HighScoreEntry {

	private long neededTime;
	// defines the name of player
	private String name;
	// defines the score reached
	private int score;
	// time of one game
	private String endOfGame;
	// Start time of single game
	private long startTime;
	// end time of single game
	private long endTime;

	/**
	 * Constructor
	 */

	public HighScoreEntry(String playerName, String endTimeStamp,String startTimeStamp, int scoreEntry) {

		this.name = playerName;
		this.score = scoreEntry;
		this.startTime = startTimeStamp;
		this.endTime = endTimeStamp;

		// TODO: get starting date from timestamp

		// TODO: get starting time from timestamp for saving in file

		// this.startDate = sDate; //this.startTime = sTime;
	}

	public String getStartingTime() {
		return current().get(startTime);

	}

	public String getneededTime( String endTime, String startTime){
		
		return (getStartTimeStamp(endTime) - new startTime);
		
	}


	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append(name).append(" ").append(endTime).append(" ")
				.append(neededTime).append(" ").append(score);

		return sb.toString();

	}

	/*
	 * 
	 * this method returns a name
	 */
	public final String getName() {
		return name;

	}

	/*
	 * 
	 * this method returns a score
	 */
	public final double getScore() {

		return score;
	}

	/*
	 * 
	 * this method returns a start date
	 */
	public final String getStartTimeStamp() {

		return this.startTime;
	}

	/*
	 * 
	 * this method returns a end date
	 */
	public final String getEndDate() {

		return endTime;
	}

	public int setHighScore(final int score) {

		return score;
	}

	/**
	 * 
	 * 
	 * this class represents the format in which a time and date are represented.
	 * 
	 * 
	 * @author yanai
	 * @return : date and time
	 */

	public void dateUtils () {
		String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";

		
	}
	/**
	 * 
	 * 

	}

}
