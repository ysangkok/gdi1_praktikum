package tests.adapters;

import java.util.Date;
import java.util.LinkedList;

import testpackage.highscore.HighscoreManager;
import testpackage.highscore.Score;



/**
 * This is the test adapter for the third extended stage of completion.
 * Implement all method stubs in order for the tests to work.
 * <br><br>
 * <i>Note:</i> This test adapter inherits from the second extended test adapter
 * 
 * @see BattleshipTestAdapterMinimal
 * @see BattleshipTestAdapterExtended1
 * @see BattleshipTestAdapterExtended2
 * 
 * @author Jonas Marczona
 * @author Fabian Vogt
 */
public class BattleshipTestAdapterExtended3 extends BattleshipTestAdapterExtended2 {

	private HighscoreManager hm;

	/**
	 * Use this constructor to initialize everything you need.
	 */
	public BattleshipTestAdapterExtended3() {
		super();
		
		hm = new HighscoreManager();
		hm.loadScoreFile();
	}
	
	/**
	 * Add a highscore entry to the highscore with the given playername, time, timestamp and points.
	 * 
	 * @param playername
	 *            the name which shall appear in the highscore
	 * @param needed_time
	 *            time the player has need to win
	 * @param creation_date
	 *            timestamp of when the game was finished
	 * @param shots
	 *            the number of shots the player has needed.
	 * 
	 * @see #getDateAtHighscorePosition(int)
	 */
	public void addHighscoreEntry(String playername, double needed_time, Date creation_date, int shots ) {

		hm.addScore(playername, shots ,(int)needed_time, creation_date);
		//hm.sort("name", false);
		
		System.err.format("Adding: shots: %d, neededtime: %f\n", shots, needed_time);
		
		System.err.println("===");
		int i = 0;
		for (Score s : hm.getScores()) System.err.println(i++ + ": " + s.toString());
	}
	
	
	
	
	/** 
	 * Return the number of highscore entries in your highscore store.
	 *  
	 * @return the number of highscore entries
	 */
	public int getHighscoreCount() {
		return hm.getScores().size();

	}
	
	/** 
	 * Clear the highscore store and delete all entries.
	 */
	public void resetHighscore() {
		hm.setScores(new LinkedList<Score>());
	}
	
	/** 
	 * Get the playername of a highscore entry at a given position.
	 * <strong>Note:</strong> The position counting starts at zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * @param position the position of the highscore entry in the highscore 
	 * store 
	 * @return the playername of the highscore entry at the specified position
	 * or null if the position is invalid 
	 */
	public String getPlayernameAtHighscorePosition(int position) {
	  try {
		  return hm.getScores().get(position).getName();
	  } catch (IndexOutOfBoundsException e) {
		  return null;
	  }
	}
	
	
	/** 
	 * Get the needed time of a highscore entry at a given position.
	 * <strong>Note:</strong> The position counting starts at zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * @param position the position of the highscore entry in the highscore 
	 * store 
	 * @return the time of the highscore entry at the specified position
	 * or -1 if the position is invalid 
	 */
	public double getTimeAtHighscorePosition(int position) {
		try {
			return  hm.getScores().get(position).getNeededTime();
		} catch (IndexOutOfBoundsException e) {
			  return -1;
		}
	}

	/**
	 * Get the number of needed shots of a highscore entry at a given position.
	 * <strong>Note:</strong> The position counting starts at zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * @param position the position of the highscore entry in the highscore 
	 * store 
	 * @return the points of the highscore entry at the specified position
	 * or -1 if the position is invalid 
	 */
	public int getShotsAtHighscorePosition(int position) {
		try {
			return  hm.getScores().get(position).getScore();
		} catch (IndexOutOfBoundsException e) {
			  return -1;
		}
	}
	/**
	 * Get the date of a highscore entry at a given position. <strong>Note:</strong> The position counting starts at
	 * zero. The first entry should contain the <i>best</i> result.
	 * <p>
	 * See the specification in the task assignment for a definition of 'best' in the highscore.<br>
	 * </p>
	 * 
	 * @param position
	 *            the position of the highscore entry in the highscore store
	 * @return the date of the highscore entry at the specified position or null if the position is invalid
	 */
	public Date getDateAtHighscorePosition(int position) {
		try {
			return  hm.getScores().get(position).getDate();
		} catch (IndexOutOfBoundsException e) {
			  return null;
		}
	}

}