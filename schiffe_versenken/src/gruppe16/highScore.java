package gruppe16;




import java.util.*;
import java.text.SimpleDateFormat;



public class HighScore {
	
	
	//represents the table of scores
	
	private LinkedList<highScoreEntry> gameTable = new LinkedList<highScoreEntry>();

	
	/**
	 * Constructor Default 
	 * 
	 * 
	 */
	
	public HighScore (){
		
		
		
		
	}
	
	
	

	private void newHighScoreListing(String realDate, long NeededTime, long gameTime, String playerName, double scoreEntry ) {
		
		// limited the list of 10 entries
		
		boolean haveTen = gameTable.size()==10;
	
	if(haveTen){
		
		gameTable.removeLast();
		gameTable.add(new highScoreEntry( playerName, endTimeStamp, startTimeStamp, scoreEntry));
		
		
	
	}else gameTable.add(new highScoreEntry(playerName, endTimeStamp, startTimeStamp, scoreEntry));
	
	
	
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

