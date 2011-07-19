package gruppe16;


import java.util.*;
import  java.util.List;
import  java.util.Comparator;
import java.util.Calendar;
import java.text.SimpleDateFormat;

class highScoreEntry {

	private long neededTime;
	// defines the name of player
	private String name;
	// defines the score reached
	private double score;
	// the start time the game started
	public long startTime;
	// defines the end date of game 
	public String endDate;
	//time of one game
	public long timeOfGame;
	//starting date
	public String startDate;
	// the start time the game started
		public long endTime;
	/**
	 * 
	 * 
	 * Constructor
	 */

	public highScoreEntry(long startTimeStamp, long endTimeStamp, String playerName, int scoreEntry) {
 
		this.name = playerName;
		this.score = scoreEntry;
        //this.timeOfGame= (sDate-eDate);
        
		//TODO: get starting date from timestamp
		//TODO: get starting time from timestamp for saving in file
		
		
		//this.startDate = sDate;
        //this.startTime = sTime;
	}
	
	
	/**
	 * 
	 * 
	 * Constructor defult
	 */
	public highScoreEntry() {
		// TODO Auto-generated constructor stub
	}

	public String toString(){
	  	
		StringBuffer sb = new StringBuffer();
		
         
		
		sb.append(name).append(" ").append(time).append(" ").append(date).append(" ").append(timeOfGame).append(" ").append(score);
		
		return sb.toString();
			
	}
	
	/*
	 * 
	 * this method returns a name
	 */
	public final String getName(){
		return name;
		
	}
	/*
	 * 
	 * this method returns a score
	 */
	public final double getScore(){
		
		return score;
	}
	
	/*
	 * 
	 * this method returns a start date 
	 */
	public final String getStartDate(){
		
		return startDate;
	}
	/*
	 * 
	 * this method returns a start time 
	 */
	
	public final long getStartTime(){
		
		return startTime;
	}
	
	
	/*
	 * 
	 * this method returns a end date 
	 */
	public final String getEndDate(){
		
		return endDate;
	}
	/*
	 * 
	 * this method returns a time of game
	 */
	public final long gettimeOfGame(){
		
		
		return timeOfGame;
	}
	
	
	
	public void setHighScore(final int score){
		
		
		this.score= score;
	}
	
	
	/**
	 * 
	 * 
	 * this class gives the time and date a single game started 
	 * @author yanai
	 *@return : date and time
	 */
	
	public class DateUtils {
		  public static final String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";

		  public  String current() {
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		    return sdf.format(cal.getTime());

		  }


	//sort by name
		public String sortNameList(String name){
			
	   
	         highScoreEntry high= new highScoreEntry();
			
			TreeMap<String,String> sortingNameList = new TreeMap<String,String>();
			
			
			
			return high.name;
			
			
		}
	
	
	//sort by required time
	public String sortRtimeList(String name){
		
   
         highScoreEntry high= new highScoreEntry();
         
         
		
	//TreeMap<String,String> sortingRtimeList = new TreeMap<String,String>();
		
		
		
		//return high.time;
		
		
	}

	// sort by date and time
	public String sortDateList(String date, long timeOfgame){
		
		   
        highScoreEntry high= new highScoreEntry();
		
		for(int i; i<highScoreEntry(); i++){
			
			
			
		}
		
		
		
		return high.get( date, timeOfGame);
		
		
	}
	
	
	// sort by score
		public double sortDateList(double score){
			
			   
	        highScoreEntry high= new highScoreEntry();
			
			TreeMap<double,double> sortingScoreList = new TreeMap<double,double>();
			
			
			
			return high.score;
			
			
		}

	
	/*
	public  sortList(){
		final int MAX= 10;
		int counter= 0;
		
		
		
		List<highScoreEntry> playerList= new LinkedList<highScoreEntry>();
		
		if(gameTable.contains(name)){
			gameTable.get(highScoreEntry)remove(score);
			gameTable.get(highScoreEntry)remove(time);
			gameTable.get(highScoreEntry)remove(timeOfGame);
			gameTable.get(name)remove(date);
			
			gameTable.get(name).set(new highScoreEntry(score));
			
		}
		
		
		
		
	}*/
}


public class highScore {
	
	
	//represents the table of scores
	
	private LinkedList<highScoreEntry> gameTable = new LinkedList<highScoreEntry>();

	
	
	
	
	
	
	

	private void newHighScoreListing(String realDate, long NeededTime, long gameTime, String playerName, double scoreEntry ) {
		
		// limited the list of 10 entries
		
		boolean haveTen = gameTable.size()==10;
	
	if(haveTen){
		
		gameTable.removeLast();
		gameTable.add(new highScoreEntry(realDate, NeededTime, gameTime, playerName, scoreEntry));
		
		
	
	}else gameTable.add(new highScoreEntry(realDate, NeededTime, gameTime, playerName, scoreEntry));
	
	
	
	//TreeMap<> tm= new TreeMap<>();
	
	
		
		

	


	
	}
	
	// sorting the entries in a list of entries
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (highScoreEntry hse : gameTable) {
			sb.append(i++ + ": " + hse.toString() + "\n");
		}
		return sb.toString();
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
		//highScoreEntry hse = new highScoreEntry("janus", 543543, 32173921, "dsadsa", 60.0);
		//System.out.println(hse.toString());
		
		
		highScore hs = new highScore();
		hs.newHighScoreListing("janus", 543543, 32173921, "dsadsa", 60.0);
		hs.newHighScoreListing("janus", 543543, 32173921, "dsadsa", 60.0);
		System.out.println(hs.toString());
		
	}	
		
		
}

