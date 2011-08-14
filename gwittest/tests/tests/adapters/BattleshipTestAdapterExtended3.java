package tests.adapters;

import java.util.Date;



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

	private static final String HighscoreManager = null;

	/**
	 * Use this constructor to initialize everything you need.
	 */
	public BattleshipTestAdapterExtended3() {
		super();
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
        
		
	}
	
	/** 
	 * Return the number of highscore entries in your highscore store.
	 *  
	 * @return the number of highscore entries
	 */
	public int getHighscoreCount() {
		//TODO implement this stub
		return -1;
	}
	
	/** 
	 * Clear the highscore store and delete all entries.
	 */
	public void resetHighscore() {
		//TODO implement this stub
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
		//TODO implement this stub
		return null;
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
		//TODO implement this stub
		return -1;
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
		//TODO implement this stub
		return -1;
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
		//TODO implement this stub
		return null;
	}

}