package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidInstruction.Reason;

import java.util.LinkedList;
import java.util.List;

/**
 * class for game core logic
 */
public class Engine {
	private boolean allowMultipleShotsPerTurn = Rules.defaultAllowMultipleShotsPerTurn;

	private List<State> undoLog;
	private State state;
	
	private int xWidth;
	private int yWidth;
	
	private int whoMadeLastShot = -1;
	
	/**
	 * @return height of map, since x (i.e. first coordinates) are actually lines
	 */
	public int getxWidth() {
		return xWidth;
	}

	/**
	 * @return width of map, since y (i.e. second coordinates) are actually character in line
	 */
	public int getyWidth() {
		return yWidth;
	}

	/**
	 * @param level level to use when constructing engine
	 */
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

	/**
	 * @return the current state
	 */
	public State getState() {
		return state;
	}

	/**
	 * constructor that automatically generates a level
	 */
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
	
	// shots per ship
	

	void chooseFiringXY(int player, int x, int y) throws InvalidInstruction {
		if (x > xWidth || y > yWidth || x < 0 | y < 0) throw new InvalidInstruction(Reason.INVALIDSHOOTER);
		state.chosenFiringX[player] = x;
		state.chosenFiringY[player] = y;
	}
	
	void enableShotsPerShip() {
		state.remainingshots = new Integer[2][xWidth][yWidth];
		state.shotspershipenabled = true;
		
		//state.chosenFiringX = new int[] {-1, -1};
		//state.chosenFiringY = new int[] {-1, -1};
		
		for ( int i : new int[] {0, 1})
			for ( int j=0; j<state.remainingshots[0].length; j++)
				for ( int k=0; k<state.remainingshots[0][0].length; k++)
					if (state.getLevel().isShipAt(Engine.otherPlayer(i),j,k))
						state.remainingshots[Engine.otherPlayer(i)][j][k] = Rules.shotsPerShipPart;
					else 
						state.remainingshots[Engine.otherPlayer(i)][j][k] = 0;
	}
	
	void setInfiniteAmmoFor(int player, boolean enable) {
		for ( int j=0; j<state.remainingshots[0].length; j++)
			for ( int k=0; k<state.remainingshots[0][0].length; k++)
				state.remainingshots[player][j][k] = Integer.MAX_VALUE;
		// TODO rigtig disable
	}
	
	// shots per ship ende
	
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
		
		this.state = newState;
		
		if (state.shotspershipenabled) {
			
			if (state.chosenFiringX[player] == -1 || state.chosenFiringY[player] == -1) throw new InvalidInstruction(Reason.NOSHOOTERDESIGNATED);
			Integer field = state.remainingshots[player][state.chosenFiringX[player]][state.chosenFiringY[player]];
			if (field == 0) {
				throw new InvalidInstruction(Reason.NOMOREAMMO);
			}
			field -= 1;
			state.remainingshots[player][state.chosenFiringX[player]][state.chosenFiringY[player]] = field;
			
			//Map2DHelper<Integer> helper = new Map2DHelper<Integer>();
			//System.err.println(helper.getBoardString(remainingshots[player]));
			
			if (Level.isShip(hit)) state.remainingshots[Engine.otherPlayer(player)][x][y] = 0;
		}
		
		hit = newState.getLevel().attack(player, x, y);
		whoMadeLastShot = player;
		
		//System.err.println(getLevel().toString());
		
		if (allowMultipleShotsPerTurn) {
			if (!Level.isShip(hit)) state.changeTurn();
		} else {
			state.changeTurn();
		}
		
		if (Level.isShip(hit)) { state.hits[player]++; }
		
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
			if (state.shotspershipenabled) {
				if (!hasRemainingShots(i) && hasRemainingShots(Engine.otherPlayer(i))) {
					this.state.setFinished(true);
					return otherPlayer(i);
				}
				if (!hasRemainingShots(i) && !hasRemainingShots(Engine.otherPlayer(i))) {
					if (state.hits[Engine.otherPlayer(i)] > state.hits[i]) {
						this.state.setFinished(true);
						return otherPlayer(i);
					} else if (state.hits[Engine.otherPlayer(i)] < state.hits[i]) {
						this.state.setFinished(true);
						return i;
					} else {
						if (whoMadeLastShot != -1) {
							this.state.setFinished(true);
							return whoMadeLastShot;
						}
					}
				}
			}
			if (state.getLevel().isPlayerLoser(i)) {
				this.state.setFinished(true);
				return otherPlayer(i);
			}
					
		}
		return -1;
	}
	
	boolean hasRemainingShots(int i) {
		boolean hasShooter = false;
		for (int j = 0; j < xWidth; j++) {
			for (int k = 0; k < yWidth; k++) {
				if (state.remainingshots[i][j][k] != 0) {
					hasShooter = true;
					break;
				}
			}
		}
		return hasShooter;
	}
	
	/**
	 * gets the number of the other player
	 * @param i the player number NOT to return
	 * @return the other player number
	 */
	static int otherPlayer(int i) {
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
		return this.state.getFinished();
	}

	/**
	 * generates string that only shows our board and the visible fields in opponents board
	 * @param i player number playing
	 * @return String which is mostly usable in CLI interface
	 */
	String getLevelStringForPlayer(int i) {
		Character[][][] boards = getCurrentBoards(i, true);
		
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		//return helper.getBoardString(new Character[][] {{'-'}, {'-'}});
		return "Your board:\n" + helper.getBoardString(boards[0]) + "Their board:\n" + helper.getBoardString(boards[1]);
		
		//return state.getLevel().toString();
	}
	
	String getAmmoStringForPlayer(int i) {
		Integer[][] shots = state.remainingshots[i];
		Map2DHelper<Integer> helper = new Map2DHelper<Integer>();
		return "Remaining ammo:\n" + helper.getBoardString(shots);
	}


	/**
	 * @return get array for players board
	 */
	public Character[][] getPlayerArray() {
		return getCurrentBoards(0,true)[0];
	}

	/**
	 * @return get opponents board, with fog
	 */
	public Character[][] getVisibleOpponentArray() {
		return getCurrentBoards(0,true)[1];
	}
	
	/**
	 * @return get opponents board, without fog
	 */
	public Character[][] getOpponentArrayWithoutFog() {
		return getCurrentBoards(0,false)[1];
	}
	
	private Character[][][] getCurrentBoards(int i, boolean fogboard) {
		Character[][] opponent	= state.getLevel().getPlayerBoard(otherPlayer(i)	);
		Character[][] our		= state.getLevel().getPlayerBoard(i					);
		
		if (fogboard) {
			boolean[][] ourFog = state.getFog(i);
		
			for (int j = 0; j < ourFog.length; j++) {
				for (int k = 0; k < ourFog[j].length; k++) {
					if (ourFog[j][k])
						opponent[j][k] = '#';
				}
			}
		}
		
		return new Character[][][] {our, opponent};

	}

	
	/**
	 * restart level by reverting to first undo position
	 */
	public void restartLevel() {
		state = undoLog.get(0);
	}

	/**
	 * set state to new state. dangerous since the undo log could now have states with levels of different widths
	 * @param state2 new state
	 */
	public void setState(State state2) {
		state = state2;
		undoLog.add(state);
		updateWidth(state.getLevel());
	}

	/**
	 * @param i shooting player
	 * @param y y coord shot destination
	 * @param x x coord shot destination
	 * @param force if true, change turn if that was the thing preventing us from shooting
	 * @return new char at shot destination
	 * @throws InvalidInstruction if the instruction was illegal for another reason than NOTYOURTURN
	 */
	public char attack(int i, int y, int x, boolean force) throws InvalidInstruction {
		if (force) {
			try {
				return attack(i, y, x);
			} catch (InvalidInstruction e) {
				if (e.getReason() == InvalidInstruction.Reason.NOTYOURTURN) {
					state.changeTurn();
					return attack(i, y, x);
				} else {
					throw e;
				}
			}
		} else {
			return attack(i, y, x);
		}
	}
}
