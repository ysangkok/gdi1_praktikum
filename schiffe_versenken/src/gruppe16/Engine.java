package gruppe16;

import gruppe16.exceptions.InvalidInstruction;

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

	public Engine(Level level) {
		state = new State(level);
		updateWidth(level);
		
		undoLog = new LinkedList<State>(); // list for storing all states since last new game
		undoLog.add(state);
	}
	
	private void updateWidth(Level level) {
		xWidth = level.getPlayerBoard(0).length;
		yWidth = level.getPlayerBoard(0)[0].length;
	}

	public State getState() {
		return state;
	}

	public Engine() {
		Level initialLevel;
		initialLevel = new LevelGenerator(Rules.defaultHeight,Rules.defaultWidth).getLevel();
		
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
		char hit = '\0';
		
		if (player == -1) {
			int[] coords = Level.parseTestInterfaceCoords(y, x, yWidth);
			player = otherPlayer(coords[0]);
			x = coords[1];
			y = coords[2];
		}
		
		if ( (getState().isPlayerTurn() && player != 0) || (!getState().isPlayerTurn() && player != 1) ) throw new InvalidInstruction(InvalidInstruction.Reason.NOTYOURTURN);
		
		State newState = state.clone(); // clone state so that we don't destroy previous game state
		undoLog.add(newState); 
		
		hit = newState.getLevel().attack(player, x, y);
		
		this.state = newState;
		//System.err.println(getLevel().toString());
		
		if (!Level.isShip(hit)) state.changeTurn();
		
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
	public static int otherPlayer(int i) {
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
		Character[][][] boards = getCurrentBoards(i);
		
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		//return helper.getBoardString(new Character[][] {{'-'}, {'-'}});
		return "Your board:\n" + helper.getBoardString(boards[0]) + "Their board:\n" + helper.getBoardString(boards[1]);
		
		//return state.getLevel().toString();
	}


	public Character[][] getPlayerArray() {
		return getCurrentBoards(0)[0];
	}

	public Character[][] getVisibleOpponentArray() {
		return getCurrentBoards(0)[1];
	}
	
	private Character[][][] getCurrentBoards(int i) {
		Character[][] opponent	= state.getLevel().getPlayerBoard(otherPlayer(i)	);
		Character[][] our		= state.getLevel().getPlayerBoard(i					);
		
		boolean[][] ourFog = state.getFog(i);
		
		for (int j = 0; j < ourFog.length; j++) {
			for (int k = 0; k < ourFog[j].length; k++) {
				if (ourFog[j][k])
					opponent[j][k] = '#';
			}
		}
		
		return new Character[][][] {our, opponent};

	}

	
	public void restartLevel() {
		state = undoLog.get(0);
	}

	public void setState(State state2) {
		state = state2;
		updateWidth(state.getLevel());
	}

}
