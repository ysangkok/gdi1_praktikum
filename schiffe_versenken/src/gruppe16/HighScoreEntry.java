package gruppe16;

import java.util.*;
import java.util.List;
import java.util.Comparator;
import java.util.Calendar;
import java.text.SimpleDateFormat;

class highScoreEntry extends HighScore {

	private String neededTime;
	// defines the name of player
	private String name;
	// defines the score reached
	private int score;
	// time of one game
	private String endOfGame;
	// Start time of single game
	private String startTime;
	// end time of single game
	private String endTime;

	/**
	 * 
	 * 
	 * Constructor
	 */

	public highScoreEntry(String playerName, String endTimeStamp,String startTimeStamp, int scoreEntry) {

		this.name = playerName;
		this.score = scoreEntry;
		this.startTime = startTimeStamp;
		this.endTime = endTimeStamp;

		// TODO: get starting date from timestamp

		// TODO: get starting time from timestamp for saving in file

		// this.startDate = sDate; //this.startTime = sTime;
	}

	public String getStartingTime() {
		return current.get(startTime);

	}

	public String getneededTime( String endTime, String startTime){
		
		return (getStartTimeStamp(endTime) - new startTime);
		
	}

	/**
	 * 
	 * 
	 * Constructor defult
	 */
	public highScoreEntry() {
		// TODO Auto-generated constructor stub
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
	 * this class gives the time and date a single game started
	 * 
	 * @author yanai
	 * @return : date and time
	 */

	public class DateUtils {
		public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";

		public String current() {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			return sdf.format(cal.getTime());

		}

		// sort by name
		public String sortNameList(String name) {

			highScoreEntry high = new highScoreEntry();

			TreeMap<String, String> sortingNameList = new TreeMap<String, String>();

			return high.name;

		}

		// sort by required time
		public String sortRtimeList(String name) {

			highScoreEntry high = new highScoreEntry();

			// TreeMap<String,String> sortingRtimeList = new
			// TreeMap<String,String>();

			// return high.time;

		}

		// sort by date and time
		public String sortDateList(String date, long timeOfgame) {

			highScoreEntry high = new highScoreEntry();

			for (int i; i < highScoreEntry(); i++) {

			}

			return high.get(date, timeOfGame);

		}

		// sort by score
		public double sortDateList(double score){
			
			   
	        highScoreEntry high= new highScoreEntry();
			
			TreeMap<double,double> sortingScoreList = new TreeMap<double,double>();
			
			
			
			return high.score;
			
			
			
			
			
		}
		
		
		
		
		
	}
	
	
	

}
