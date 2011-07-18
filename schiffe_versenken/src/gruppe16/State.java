package gruppe16;

import java.io.Serializable;

import gruppe16.exceptions.InvalidLevelException;

/**
 * class for storing game state, including level and fog.
 */
public class State implements Cloneable, Serializable { //  Cloneable for undoing. Serializable for saving
	private static final long serialVersionUID = 1L;
	
	private Level level;
	private boolean[][][] fog;
	private int turn;
	private boolean finished;
	
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
	 * returns an independent State containing all the current values
	 */
	@Override public State clone() {
		State newState;
		try {
			//System.out.println(level.toString());
			
			newState = new State(new Level(level.toString()));
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
		
		newState.setFog(newFog);
		newState.turn = turn;
		return newState;
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
			level = new Level(text);
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
}
