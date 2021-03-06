package testpackage.shared.ship;

import testpackage.shared.ship.Util;
import testpackage.shared.ship.exceptions.InvalidLevelException;
import testpackage.shared.ship.exceptions.InvalidInstruction;
import testpackage.shared.ship.exceptions.InvalidInstruction.Reason;
import testpackage.shared.ship.SoundHandler.Sound;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

//import com.google.common.base.Predicate;
//import com.google.common.base.Predicates;
//import com.google.common.collect.Iterables;

/**
 * class for game core logic
 */
public class Engine {
	private boolean allowMultipleShotsPerTurn = false;
	private boolean reichweite = false;

	private List<Map<Ship,Boolean>> shotships;
	private List<List<Ship>> ships;

	private List<State> undoLog;
	private State state;
	
	private int xWidth;
	private int yWidth;
	
	private int whoMadeLastShot = -1;
	
	private SoundHandler soundhandler = null;

	private int initialAmmoCount;

	private List<Map<Ship,Boolean[][]>> shipToTargets;
	
	
	/**
	 * 
	 * @return true if multiple shots per turn are enabled
	 */
	public boolean getMoreShots() {
		return allowMultipleShotsPerTurn;
	}
	/**
	 * set multiple shots per turn enabled or disabled.
	 * @param enabled true if enabled
	 */
	public void setMoreShots(boolean enabled) {
		//System.err.format("Set allow: %s", enabled);
		allowMultipleShotsPerTurn = enabled;
	}
	
	/**
	 * @return the ammo count for all fields when engine was initialized or state set
	 */
	public int getInitialAmmoCount() { return initialAmmoCount; }

	/**
	 * set sound handler so sounds can be played. only GUISchiffe currently handles
	 * @param handler handler to play sounds with
	 */
	public void setSoundHandler(SoundHandler handler) {
		this.soundhandler = handler;
	}
	
	private void maybePlaySound(Sound sound, int player) {
		if (soundhandler != null) {
			soundhandler.playSound(sound, player);
		}
	}
	/**
	 * choose a random sound and play it, if sound handler set
	 * @param sounds sounds to choose from
	 * @param player relevant player board number
	 */
	private void maybePlaySoundOneOf(Sound[] sounds, int player) {
		Random g = new Random();
		int pos = g.nextInt(sounds.length-1);
		maybePlaySound(sounds[pos], player);
	}
	/**
	 * play sound with no modifications if sound handler set
	 * @param sound sound to play
	 */
	private void maybePlaySound(Sound sound) {
		if (soundhandler != null) {
			soundhandler.playSound(sound);
		}
	}
	
	/**
	 * detect ship locations and store. initialize map for detecting shot ships
	 */
	private void detectShips() {
		ships = new ArrayList<List<Ship>>();
		try {
			ships.add(Level.getShips(state.getLevel().getPlayerBoard(0)));
			ships.add(Level.getShips(state.getLevel().getPlayerBoard(1)));
		} catch (InvalidLevelException e) {
			throw new RuntimeException(e);
		}

		shotships = new ArrayList<Map<Ship,Boolean>>();
		for (int p : new int[] {0, 1}) {
			Map<Ship, Boolean> map = new HashMap<Ship,Boolean>();
			shotships.add(map);
			for (Ship s : ships.get(p)) {
				map.put(s, false);
			}
		}
		
		if (reichweite) {
			shipToTargets = new ArrayList<Map<Ship,Boolean[][]>>();
			
			for (int shootingPlayer : new int[]{0,1}) {
				HashMap<Ship, Boolean[][]> map = new HashMap<Ship, Boolean[][]>();
				shipToTargets.add(map);
				for (Ship s : ships.get(shootingPlayer)) {
					Boolean[][] shootable = new Boolean[xWidth][yWidth*2];

					for (int i=0; i<shootable.length; i++)
						for (int j=0; j<shootable[i].length; j++)
							shootable[i][j] = false;

					for (Integer[] coord : s.getAllOccupiedCoords()) {
						for (int[] ruleset : Rules.ships) {
							if (s.len == ruleset[0]) {
								drawCircle(shootable, coord[0], coord[1], ruleset[2]);
							}
						}
					}

					Boolean[][] shootableAtOpponent = new Boolean[xWidth][yWidth];
					for (int i = 0; i < shootable.length; i++) {
						int j2 = 0;
						for (int j = shootable[i].length/2; j < shootable[i].length; j++) {
							shootableAtOpponent[i][j2++] = shootable[i][j];
						}
						if (shootingPlayer == 1) reverse(shootableAtOpponent[i]);
					}	

					map.put(s, shootableAtOpponent);
				}
			}
		}
	}

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
		detectShips();
		
		undoLog = new LinkedList<State>(); // list for storing all states since last new game
		undoLog.add(state);
		
		checkWin();
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
		this(new LevelGenerator(Rules.defaultHeight,Rules.defaultWidth, Rules.ships).getLevel());
	}
	
	// shots per ship
	
	/**
	 * choosing coordinates we are shooting from, for given player
	 * @param player player shooting
	 * @param x our coord format
	 * @param y our coord format
	 * @return remaining shots at given position
	 * @throws InvalidInstruction coordinates out of range
	 */
	public int chooseFiringXY(int player, int x, int y) throws InvalidInstruction {
		if (x > xWidth-1 || y > yWidth-1 || x < 0 || y < 0) throw new InvalidInstruction(Reason.INVALIDSHOOTER);
		state.chosenFiringX[player] = x;
		state.chosenFiringY[player] = y;
		return remainingShotsFor(player);
	}
	
	/**
	 * remaining shots at given position
	 * @param player player number to check for
	 * @return integer with amount of shots available
	 * @throws InvalidInstruction when shooter wasn't designated
	 */
	public int remainingShotsFor(int player) throws InvalidInstruction {
		if (state.chosenFiringX[player] == -1 || state.chosenFiringY[player] == -1) throw new InvalidInstruction(Reason.NOSHOOTERDESIGNATED);
		return remainingShotsFor(player,state.chosenFiringX[player],state.chosenFiringY[player]);
	}

	public int remainingShotsFor(int player, int x, int y) {
		if (state.remainingshots == null) throw new RuntimeException("tried to get remaining shots, but its not enabled");
		return state.remainingshots[player][x][y];
	}
	
	/**
	 * called to enable shots per ship game mode. only works after level initialization
	 * @param ammocount 
	 */
	public void enableShotsPerShip(int ammocount) {
		this.initialAmmoCount = ammocount;
		state.initRemainingShots(ammocount);
		checkWin();
	}

	public void enableRange() {
		if (!isShotsPerShipEnabled()) throw new RuntimeException("Can't enable range without shots per ship!");
		//System.err.println("Enabling range");
		this.reichweite = true;
		detectShips();
		checkWin();
	}
	

	/**
	 * for usage in CLISchiffe
	 * @param targets in format from getTargets
	 * @return string for outputting
	 */
	public static String getTargetString(Boolean[][] targets) {
		Map2DHelper<Boolean> helper = new Map2DHelper<Boolean>();
		
		return helper.getCustomPaddedBoardString(targets, 6);
	}
	
	/**
	 * get boolean map of shootable fields in opponents board from x,y in players board. if player is 1, the opponent is left (i.e. lower y position), if player is 0, the opponent is right (i.e. higher y position). that means the results are mirrored on one dimension.
	 * @param shootingPlayer player number who is shooting
	 * @param x x coord (our format, i.e. y/x reversed)
	 * @param y y coord (our format, i.e. y/x reversed)
	 * @return boolean 2d array
	 * @throws InvalidInstruction when shooter invalid
	 */
	public Boolean[][] getTargets(int shootingPlayer, int x, int y) throws InvalidInstruction {
		if (!reichweite) throw new RuntimeException("Range not enabled!");
	
		//List<Ship> ships = Level.getShips(getCurrentBoards(shootingPlayer, false)[0]);
		
		Ship s = Level.getShipAt(ships.get(shootingPlayer), x, y);
		
		Boolean[][] targets = shipToTargets.get(shootingPlayer).get(s);
		if (targets == null)
			throw new InvalidInstruction(Reason.WATERCANTSHOOT, Util.format("No ship at %d,%d,%d\n" + new Map2DHelper<Character>().getBoardString(state.getLevel().getPlayerBoard(shootingPlayer)),shootingPlayer,x,y));

		return targets;
		
	}
	/**
	 * generic. reverse an array
	 * @param arr array to reverse
	 * @return reversed array. different object
	 */
    private static Object[] reverse(Object[] arr) {
        List < Object > list = Arrays.asList(arr);
        Collections.reverse(list);
        return list.toArray();
    }
    /**
     * generic circle drawing algorithm. used for range of ships
     * @param b boolean array to draw in
     * @param CircCenterX circle center coord
     * @param CircCenterY circle center coord
     * @param radius radius of circle
     */
	private static void drawCircle(Boolean[][] b, int CircCenterX, int CircCenterY, int radius) {
		double rstep = 1.0/(((double) radius)*4);//set rstep to something like 1/radius, something that draws quickly but doesn't skip pixels
		for (double r=0; r<Math.PI*.5; r=r+rstep) //1 to 1/2pi
		{
			int y1 = (int) Math.round(((double) CircCenterY) + Math.sin(r) * ((double) radius));
			int x2 = (int) Math.round(((double) CircCenterX) - Math.cos(r) * ((double) radius));
			int y2 = (int) Math.round(((double) CircCenterY) - Math.sin(r) * ((double) radius));
			int x3 = (int) Math.round(((double) CircCenterX) + Math.cos(r) * ((double) radius));
			int y3 = (int) Math.round(((double) CircCenterY) - Math.sin(r) * ((double) radius));
			int y4 = (int) Math.round(((double) CircCenterY) + Math.sin(r) * ((double) radius));

			
			for (int k=y2; k<=y4; k++) {
				try {
					b[x2][k] = true;
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
			for (int k=y3; k<=y1; k++) {
				try {
					b[x3][k] = true;
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
	}
	
	/**
	 * quick cheat to make it easier to code AI. shouldn't be used. YOU STILL NEED TO CHOOSE SHOOTER AFTER DOING THIS
	 * @param player player to give infinite(-ish) ammo
	 * @param enable not currently used, but might be used later for enabling/disabling during game
	 */
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
		State oldState = this.state;
		try {
		char hit;
		
		if (player < 0 || player > 1) {
			throw new RuntimeException("player ungueltig");
		}

		//System.err.println("==attack==");
		//System.err.println(state.isPlayerTurn());
		//System.err.println(player);
		
		if ( (getState().isPlayerTurn() && player != 0) || (!getState().isPlayerTurn() && player != 1) ) throw new InvalidInstruction(InvalidInstruction.Reason.NOTYOURTURN);
		
		State newState = state.clone(false); // clone state so that we don't destroy previous game state
		undoLog.add(newState); 
		
		this.state = newState;
		
		if (state.shotspershipenabled) {
			if (state.chosenFiringX[player] == -1 || state.chosenFiringY[player] == -1) throw new InvalidInstruction(Reason.NOSHOOTERDESIGNATED);
			if (reichweite) {
				Boolean[][] targets = getTargets(player, state.chosenFiringX[player], state.chosenFiringY[player]);
				if (!targets[x][y]) throw new InvalidInstruction(Reason.OUTOFSHOOTERRANGE);
			}
			Integer field = state.remainingshots[player][state.chosenFiringX[player]][state.chosenFiringY[player]];
			if (field == 0) {
				throw new InvalidInstruction(Reason.NOMOREAMMO);
			}
			field -= 1;
			state.remainingshots[player][state.chosenFiringX[player]][state.chosenFiringY[player]] = field;
			
			//System.err.println(Engine.getTargetString(getTargets(player, x, y)));
			
			//Map2DHelper<Integer> helper = new Map2DHelper<Integer>();
			//System.err.println(helper.getBoardString(remainingshots[player]));
		}
		
		hit = newState.getLevel().attack(player, x, y);
		whoMadeLastShot = player;
		
		if (player == 0) maybePlaySoundOneOf(new Sound[] {SoundHandler.Sound.Boom1_wav, SoundHandler.Sound.Boom2_wav, SoundHandler.Sound.Boom3_wav}, otherPlayer(player));
		
		if (state.shotspershipenabled) {
			if (Level.isShip(hit)) state.remainingshots[Engine.otherPlayer(player)][x][y] = 0;
		}
		
		//System.err.println(getLevel().toString());
		
		//System.err.format("Allow: %s\n", allowMultipleShotsPerTurn);
		if (allowMultipleShotsPerTurn) {
			if (!Level.isShip(hit)) state.changeTurn();
		} else {
			state.changeTurn();
		}
		
		if (Level.isShip(hit)) {
			state.incrHits(player);
			Map<Ship,Boolean> shipsmap = shotships.get(otherPlayer(player));
			Ship s = Level.getShipAt(shipsmap.keySet(), x, y);
			boolean allshotup = s.isAllShotUp(state.getLevel().getPlayerBoard(otherPlayer(player)));
			shipsmap.put(s, allshotup);
			if (allshotup) { maybePlaySound(SoundHandler.Sound.Water_wav, otherPlayer(player)); }

//			Iterable<Ship> evenNumbers = Iterables.filter(s.entrySet(), Predicates.alwaysTrue);
		}
		
		checkWin(); // update isFinished, just in case we dont explicitly check from GUI or CLI
		
		boolean[][] fog = state.getFog(player);
		fog[x][y] = false; // this field is not visible
		
		if (Level.isShip(hit)) {
			boolean allshotup = Level.getShipAt(state.getLevel().getShips(otherPlayer(player)), x, y).isAllShotUp(state.getLevel().getPlayerBoard(otherPlayer(player)));
			//System.err.println("hit: " + hit + ", allshotup: " + allshotup);
			return (allshotup ? 'X' : 'Y');
		} else
			return hit;
		} catch (InvalidLevelException e) {
			e.addText(oldState.toString());
			return '?';
		}
	}

	public boolean isShotsPerShipEnabled() {
		return state.shotspershipenabled;
	}
	
	/**
	 * check if someone won
	 * @return -1 if no one won so far. returns the winning player if somebody lost
	 */
	public Loser checkWin() {
		for (int i : new int[] {0, 1}) {
			//System.err.println("Checking player " + i);
			if (state.shotspershipenabled) {
				if (!hasRemainingShots(i) && hasRemainingShots(Engine.otherPlayer(i))) {
					this.state.setFinished(true);
					return new Loser(Util.format("Player %d had no shots, unlike player %d", i+1, otherPlayer(i)+1), otherPlayer(i));
				}
				if (!hasRemainingShots(i) && !hasRemainingShots(Engine.otherPlayer(i))) {
					if (state.getHits()[Engine.otherPlayer(i)] > state.getHits()[i]) {
						this.state.setFinished(true);
						return new Loser(Util.format("Hit count %d > %d", state.getHits()[Engine.otherPlayer(i)], state.getHits()[i]),otherPlayer(i));
					} else if (state.getHits()[Engine.otherPlayer(i)] < state.getHits()[i]) {
						this.state.setFinished(true);
						return new Loser(Util.format("Hit count %d < %d", state.getHits()[Engine.otherPlayer(i)], state.getHits()[i]),i);
					} else {
						if (whoMadeLastShot != -1) {
							this.state.setFinished(true);
							return new Loser("Losing player didn't make last shot", whoMadeLastShot);
						}
					}
				}
				if (reichweite) {
					boolean foundShootable = false; 
					outer: for (Ship s : ships.get(i)) {
						for (Integer[] coord : s.getAllOccupiedCoords()) {
							if (remainingShotsFor(i, coord[0], coord[1]) == 0) continue;
							
							Boolean[][] shipTargets;
							try {
								shipTargets = getTargets(i, coord[0], coord[1]);
							} catch (InvalidInstruction e) {
								throw new RuntimeException(Util.format("Couldn't get targets for %s", s.toString()), e);
							}

							for (int j=0; j<shipTargets.length; j++) {
								for (int k=0; k<shipTargets[j].length; k++) {
									if (shipTargets[j][k] && state.getFog(i)[j][k]) { foundShootable = true; break outer; }
								}
							}
						}
					}
					if (!foundShootable) {
						this.state.setFinished(true);
						return new Loser("No available targets", otherPlayer(i));
					}
				}
			}
			if (state.getLevel().isPlayerLoser(i)) {
				this.state.setFinished(true);
				return new Loser("Other player had all ships shot",otherPlayer(i));
			}
					
		}
		return new Loser(-1);
	}
	
	/**
	 * Shots per ship
	 * does given player have any available ships to fire from?
	 * @param i player number to analyze
	 * @return true when ships available, false when not
	 */
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
		return this.state.getFinished();
	}

	/**
	 * generates string that only shows our board and the visible fields in opponents board
	 * @param i player number playing
	 * @return String which is mostly usable in CLI interface
	 */
	public String getLevelStringForPlayer(int i) {
		Character[][][] boards = getCurrentBoards(i, true);
		
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		//return helper.getBoardString(new Character[][] {{'-'}, {'-'}});
		return "Your board:\n" + helper.getBoardString(boards[0]) + "Their board:\n" + helper.getBoardString(boards[1]);
		
		//return state.getLevel().toString();
	}
	
	/**
	 * get ammo string for use in CLI interface
	 * @param i player number
	 * @return string for use in UI
	 */
	public String getAmmoStringForPlayer(int i) {
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
	
	/**
	 * Shouldn't normally be used, use getOpponentArrayWithoutFog/getVisibleOpponentArray/getPlayerArray
	 * @param i player perspective (playing player)
	 * @param fogBoardAndObscureShips whether to fog opponents board and obscure non-sunken ships
	 * @return board layout relative to i
	 */
	public Character[][][] getCurrentBoards(int i, boolean fogBoardAndObscureShips) {
		Character[][] opponent	= state.getLevel().getPlayerBoard(otherPlayer(i)	);
		Character[][] our		= state.getLevel().getPlayerBoard(i					);
		
		if (fogBoardAndObscureShips) {
			boolean[][] ourFog = state.getFog(i);
		
			for (int j = 0; j < ourFog.length; j++) {
				for (int k = 0; k < ourFog[j].length; k++) {
					if (ourFog[j][k])
						opponent[j][k] = '#';
				}
			}
			
			for (int j = 0; j < opponent.length; j++) {
				for (int k = 0; k < opponent[j].length; k++) {
					char c = opponent[j][k];
					if (Level.isShip(c) && !Level.getShipAt(ships.get(otherPlayer(i)), j, k).isAllShotUp(getOpponentArrayWithoutFog())) {
						opponent[j][k] = 'Y';
					}
				}
			}
		}
		
		return new Character[][][] {our, opponent};

	}

	
	/**
	 * restart level by reverting to first undo position. not tested, and not available anywhere.
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
		detectShips();
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
	public boolean canShootAnythingWithCurrentShip(int shootingPlayer) {
		Boolean[][] shootable;
		try {
			shootable = getTargets(shootingPlayer, state.getFiringX(shootingPlayer), state.getFiringY(shootingPlayer));
		} catch (InvalidInstruction e) {
			return false; // not even a ship
		}
		
		for (int i = 0; i < shootable.length; i++)
			for (int j = 0; j < shootable[i].length; j++)
				if (shootable[i][j])
					return true;
		return false;
	}
	public boolean isRangeEnabled() {
		return reichweite;
	}
	public static void main(String[] args) {
		//Currently for testing lost level because of nothing shootable
		String text ="lhhhr---lr--------------------|-----lr-lr-----------t---t----\n-----lhr--lr------------------|------------lhhhr----v---v----\nlhhr----lr--------------------|---lhr---------------b---b----\n----lhr---lr------------------|lr--------------------lr------\nlhhr---lhr--------------------|----lhhr-------lhhr-----------\n";
		
		Engine engine = new Engine(new Level(text, Rules.ships));
		engine.enableShotsPerShip(1);
		engine.enableRange();
		
		System.err.println(engine.isFinished());
	}
	public int[][] getShipRuleSet() {
		return state.getLevel().ruleset;
	}
	public static int decideShipRange(int width, int height, Integer shiplength, Integer shipcount) {
		return (int) Math.pow(shiplength, 2);
	}
}
