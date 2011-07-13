package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidLevelException;

import java.util.LinkedList;
import java.util.List;

/**
 * class for game core logic
 */
public class Engine {

	private List<State> undoLog;
	private State state;
	
	private boolean finished = false;
	private int xWidth;
	private int yWidth;

	public int getxWidth() {
		return xWidth;
	}

	public int getyWidth() {
		return yWidth;
	}

	public Engine() {
		Level initialLevel;
		initialLevel = new LevelGenerator(8,8).getLevel();
		
		State initialState = new State(initialLevel);
		
		undoLog = new LinkedList<State>(); // list for storing all states since last new game
		undoLog.add(initialState);
		
		this.state = initialState;
		this.xWidth = initialLevel.getPlayerBoard(0).length;
		this.yWidth = initialLevel.getPlayerBoard(0)[0].length;
	}
	
	/**
	 * tries to attack at the given coordinates. throws exception 
	 * @param player the player who is shooting
	 * @param x first array index (actually the line number)
	 * @param y second array index (actually the character at N pos in line)
	 * @return the character at the given position after shooting. '*' or one of the uppercase letters
	 * @throws InvalidInstruction if you shoot at field that was already hit. also throws when out of range.
	 */
	public char attack(int player, int x, int y) throws InvalidInstruction {
		char hit;
		
		State newState = state.clone(); // clone state so that we don't destroy previous game state
		undoLog.add(newState); 
		
		hit = newState.getLevel().attack(player, x, y);
		
		this.state = newState;
		//System.err.println(getLevel().toString());
		
		checkWin(); // update isFinished, just in case we dont explicitly check from GUI or CLI
		
		boolean[][] fog = state.getFog(player);
		fog[x][y] = false; // this field is not visible
		
		return hit;
	}
	
	/**
	 * check if someone won
	 * @return -1 if no one won so far. returns the winning player if somebody lost
	 */
	public int checkWin() {
		for (int i : new int[] {0, 1}) {
			//System.err.println("Checking player " + i);
			if (state.getLevel().isPlayerLoser(i)) {
				this.finished = true;
				return otherPlayer(i);
			}
		}
		return -1;
	}
	
	/**
	 * gets the number of the other player
	 * @param i the player number NOT to return
	 * @return the other player number
	 */
	private int otherPlayer(int i) {
		if (i == 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * is game over?
	 * @return true when game over, false when not
	 */
	public boolean isFinished() {
		return this.finished;
	}

	/**
	 * generates string that only shows our board and the visible fields in opponents board
	 * @param i player number playing
	 * @return String which is mostly usable in CLI interface
	 */
	public String getLevelStringForPlayer(int i) {
		Character[][] opponent	= state.getLevel().getPlayerBoard(otherPlayer(i)	);
		Character[][] our		= state.getLevel().getPlayerBoard(i					);
		
		boolean[][] ourFog = state.getFog(i);
		
		for (int j = 0; j < ourFog.length; j++) {
			for (int k = 0; k < ourFog[j].length; k++) {
				if (ourFog[j][k])
					opponent[j][k] = '#';
			}
		}
		
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		//return helper.getBoardString(new Character[][] {{'-'}, {'-'}});
		return "Your board:\n" + helper.getBoardString(our) + "Their board:\n" + helper.getBoardString(opponent);
		
		//return state.getLevel().toString();
	}

}
