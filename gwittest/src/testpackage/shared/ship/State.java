package testpackage.shared.ship;

import java.io.Serializable;
import java.util.Arrays;

import testpackage.shared.ship.exceptions.InvalidLevelException;

/**
 * class for storing game state, including level and fog.
 */
public class State implements Cloneable, Serializable { //  Cloneable for undoing. Serializable for saving
	private static final long serialVersionUID = 1L;
	
	public String toString() {
		return "State: has level: " + level.toString();
	}
	
	/**
	 * shots per ship disabled by default
	 */
	public boolean shotspershipenabled = false;
	private int lastammocount;
	public int getLastAmmoCount() { return lastammocount; }
	/**
	 * same dimensions as with boards
	 */
	Integer[][][] remainingshots;
	/**
	 * index designates player number
	 */
	int chosenFiringX[] = new int[] {-1, -1};
	/**
	 * index designates player number
	 */
	int chosenFiringY[] = new int[] {-1, -1};

	public int getFiringX(int player) { return chosenFiringX[player]; }
	public int getFiringY(int player) { return chosenFiringY[player]; }
	
	/**
	 * hit counter for usage in deciding who won when both players don't have available ships to shoot from. see checkWin
	 */
	private int hits[] = {0, 0};
	
	public int[] getHits() {
		return hits;
	}

	private Level level;
	private boolean[][][] fog;
	private int turn;
	private boolean finished;
	
	public int getTurn() { return turn; }
	
	/**
	 * used by engine to mark state as finished
	 * @param finished true for finished, false for not finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	/**
	 * @return true if finished
	 */
	public boolean getFinished() {
		return finished;
	}

	/**
	 * gets 2d fog array for given player
	 * @param player player number
	 * @return boolean[][] with true where it's foggy and false where it's not
	 */
	boolean[][] getFog(int player) {
		return fog[player];
	}

	/**
	 * update fog table
	 * @param fog 1st index: player number, 2nd index: line number, 3rd index: field in line number
	 */
	public void setFog(boolean[][][] fog) {
		this.fog = fog;
	}

	/**
	 * constructor takes a current level. also initializes fog map to all foggy.
	 * @param level current level progress
	 */
	public State(Level level) {
		this.turn = 0; // player starts
		this.level = level;
		fog = new boolean[2][level.getPlayerBoard(0).length][level.getPlayerBoard(0)[0].length];
		for (int i : new int[] {0, 1}) {
			for (int j = 0; j < fog[i].length; j++) {
				for (int k = 0; k < fog[i][j].length; k++) {
					fog[i][j][k] = true;
				}
			}
		}
	}
	
	/**
	 * @return current level
	 */
	public Level getLevel() {
		return this.level;
	}

	/**
	 * DANGEROUS. set level
	 *
	 */
	public void setLevel(Level level, Integer ammocount) {
		this.level = level;
		if (shotspershipenabled) initRemainingShots(ammocount);
	}

	public State clone() {
		return clone(true);
	}

	/**
	 * returns an independent State containing all the current values
	 */
	//@Override
	public State clone(boolean check) {
		try {
			super.clone();
		} catch (CloneNotSupportedException e1) {
			throw new RuntimeException(e1);
		}
		
		State newState;
		try {
			//System.out.println(level.toString());
			
			newState = new State(new Level(level.toString(), check, level.ruleset));
		} catch (InvalidLevelException e) {
			//System.err.println("We generated an invalid level!" + e.getMessage());
			e.printStackTrace();
			e.printBoard();
			return null;
		}
		
		boolean[][][] newFog = new boolean[2][fog[0].length][fog[0][0].length];
		for (int i : new int[] {0, 1}) { // both players
			for (int j = 0; j < fog[i].length; j++) {
				for (int k = 0; k < fog[i][j].length; k++) {
					newFog[i][j][k] = fog[i][j][k];
				}
			}
		}
		
		newState.shotspershipenabled = shotspershipenabled;
		newState.chosenFiringX = clone(chosenFiringX);
		newState.chosenFiringY = clone(chosenFiringY);
		if (shotspershipenabled) {
			newState.remainingshots = new Integer[2][remainingshots[0].length][remainingshots[0][0].length];
			for (int i : new int[] {0, 1}) { // both players
				for (int j = 0; j < remainingshots[i].length; j++) {
					newState.remainingshots[i][j] = clone(remainingshots[i][j]);
				}
			}
		}
		
		newState.hits = clone(hits);
		
		newState.setFog(newFog);
		newState.turn = turn;
		return newState;
	}

    void initRemainingShots(int ammocount) {
		this.lastammocount = ammocount;

		remainingshots = new Integer[2][level.getXWidth()][level.getYWidth()];
		shotspershipenabled = true;
		
		//state.chosenFiringX = new int[] {-1, -1};
		//state.chosenFiringY = new int[] {-1, -1};
		
		for ( int i : new int[] {0, 1})
			for ( int j=0; j<remainingshots[0].length; j++)
				for ( int k=0; k<remainingshots[0][0].length; k++)
					if (level.isShipAt(Engine.otherPlayer(i),j,k))
						remainingshots[Engine.otherPlayer(i)][j][k] = ammocount;
					else 
						remainingshots[Engine.otherPlayer(i)][j][k] = 0;

    }

    @SuppressWarnings("unchecked")
    public static <T> T[] clone(T[] array) {
        return (T[])Arrays.asList(array).toArray();
    }
    
    public static int[] clone(int[] array) {
        int[] newarr = new int[array.length];
        for (int i = 0; i < array.length; i++)
        	newarr[i] = array[i];
        
        return newarr;
    }
	
	/**
	 * main procedure for testing
	 * @param args not used
	 */
	public static void main(String[] args) {
		String text = 
			"lr--t---lr|lr-------t\n"+
			"----b-----|---lhr---b\n"+
			"----------|----------\n"+
			"--t--lhhr-|------lhr-\n"+
			"--v-------|--lhr-----\n"+
			"--v-lhhr--|t---------\n"+
			"--v------t|v-t--lhhr-\n"+
			"--b-lhr--b|v-v-------\n"+
			"----------|v-v--t----\n"+
			"lhr----lhr|b-b--b--lr\n";
		Level level;
		try {
			level = new Level(text, Rules.ships);
		} catch (Exception e) { return; }
		State st1 = new State(level);
		st1.clone();
	}

	/**
	 * @return true if it is the players turn to play
	 */
	public boolean isPlayerTurn() {
		return turn == 0;
	}

	/**
	 * turn change. of course used by attack
	 */
	public void changeTurn() {
		turn = Engine.otherPlayer(turn);
	}

	public void incrHits(int player) {
		hits[player]++;
		
	}
}
