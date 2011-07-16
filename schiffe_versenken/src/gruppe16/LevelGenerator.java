package gruppe16;

import gruppe16.exceptions.InvalidLevelException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class LevelGenerator {
	class PlaceShipRulesViolation extends Exception {
		private static final long serialVersionUID = 1L;
		
		String message = "";
		
		PlaceShipRulesViolation(String message) {
			this.message = message;
		}
		PlaceShipRulesViolation() {
		}
		public String getMessage() {
			return "Couldn't place ship! " + message;
		}
	}
	
	class LevelGenerationException extends Exception {
		private static final long serialVersionUID = 1L;
		
		String message;
		
		LevelGenerationException(String message) {
			this.message = message;
		}
		public String getMessage() {
			return message;
		}
	}

	private static boolean DEBUG = false;
	private static void debug(String str) {
		if (DEBUG)
			System.out.println(str);
	}
	
	private Character[][][] boards;
	Random gen;
	int xwidth;
	int ywidth;
	Map<Integer, Integer> boatCount;
	
	private void initBoard(int xwidth, int ywidth) {
		this.xwidth = xwidth;
		this.ywidth = ywidth;
		
		boards = new Character[2][xwidth][ywidth];
		for (int k = 0; k <= 1; k++)
			for (int i = 0; i < xwidth; i++)
				for (int j = 0; j < ywidth; j++)
					boards[k][i][j] = '-';
		
		gen = new Random();
		
		boatCount = new TreeMap<Integer, Integer>(Collections.reverseOrder()); // launch big ships first
		for (int i = 0; i < Rules.ships.length; i++) {
			boatCount.put(Rules.ships[i][0], Rules.ships[i][1]);
		}
		
	}
	
	public LevelGenerator(int xwidth, int ywidth) {
		initBoard(xwidth, ywidth);
		for (int p : new int[] {0, 1}) {
			placeTheseShips(this, p, boatCount);
		}
	}
	
	public LevelGenerator(int xwidth, int ywidth, int player, List<Ship> ships) throws InvalidLevelException {
		initBoard(xwidth, ywidth);
		
		placeTheseShips(this, Engine.otherPlayer(player), boatCount);

		drawGivenShips(player, ships);
	}

	private static void placeTheseShips(LevelGenerator lg, int p, Map<Integer, Integer> boatCount) {
	    Iterator it = boatCount.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, Integer> pairs = (Map.Entry<Integer, Integer>)it.next();
			
			for (int i = 0; i < pairs.getValue(); i++) {
					try {
						lg.placeShipLoop((int) pairs.getKey(), p);
					} catch (LevelGenerationException e) {
						System.err.println(e.getMessage());
						return;
					}
					if (!DEBUG) continue;
					try {
						Level.checkShips(p, lg.boards[p], false);
					} catch (InvalidLevelException e) {
						e.printStackTrace();
						return;
					}
			}
		}
	}
	
	private void placeShipLoop(int shiplength, int p) throws LevelGenerationException {
		long tries = 0;
		
		for (;;) {
			if (tries > 100000) {
				throw new LevelGenerationException(String.format("Created unfillable map. Couldn't launch %d-ship for player %d.", shiplength, p+1));
			}

			boolean alongXAxis = gen.nextBoolean();
			int x;
			int y;
			if (alongXAxis) {
				 x = gen.nextInt(xwidth - shiplength);
				 y = gen.nextInt(ywidth);
			} else {
				 x = gen.nextInt(xwidth);
				 y = gen.nextInt(ywidth - shiplength);			
			}

			try {
				placeShip(shiplength, p, x, y, alongXAxis);
			} catch (PlaceShipRulesViolation e) {
				tries++;
				continue;
			}
			return;
			
		}
	}
	
	private void drawGivenShips(int player, List<Ship> ships) throws InvalidLevelException {
		for (Ship s : ships) {
			try {
				placeShip(s.len, player, s.x, s.y, s.o == Orientation.VERTICAL);
			} catch (PlaceShipRulesViolation e) {
				throw new InvalidLevelException(String.format("This ship couldn't be placed (reason: %s): %s", s.toString(), e.getMessage()));
			}
		}
	}
	
	private void placeShip(int shiplength, int p, int x, int y, boolean alongXAxis) throws PlaceShipRulesViolation {
		
		debug(String.format("considering %d,%d , alongxaxis:%s",x,y,alongXAxis));
		
		debug("check north/west");
		
		if (alongXAxis) { // check north or west of ship
			debug(String.format("north: trying to check %d,%d",x-1,y));
			if (x-1 >= 0 && boards[p][x-1][y] != '-') {
				throw new PlaceShipRulesViolation(String.format("ship north of %d,%d",x-1,y));
			}
		} else {
			debug(String.format("west: trying to check %d,%d",x,y-1));
			if (y-1 >= 0 && boards[p][x][y-1] != '-') {
				throw new PlaceShipRulesViolation(String.format("ship west of %d,%d",x,y-1));
			}
		}
		
		{
			int i;

			debug("check each tile");

			for (i = 0; i < shiplength; i++) { // check next to each tile of ship
				int n = (alongXAxis ? x+i : x);
				int m = (!alongXAxis ? y+i : y);
				if (boards[p][n][m] != '-')
					throw new PlaceShipRulesViolation("Ship not using unused fields");
				if (!alongXAxis) {
					if (n-1 >= 0 && boards[p][n-1][m] != '-') throw new PlaceShipRulesViolation();
					if (n+1 < boards[p].length && boards[p][n+1][m] != '-') throw new PlaceShipRulesViolation();
				} else {
					if (m-1 >= 0 && boards[p][n][m-1] != '-') throw new PlaceShipRulesViolation();
					if (m+1 < boards[p][n].length && boards[p][n][m+1] != '-') throw new PlaceShipRulesViolation();
				}
			}

			debug("check south/east");

			if (alongXAxis) { // check south or east of ship
				debug(String.format("south: trying to check %d,%d",x+i,y));
				if (x+i < boards[p].length && boards[p][x+i][y] != '-') {
					throw new PlaceShipRulesViolation(String.format("ship south of %d,%d",x+i,y));
				}
			} else {
				debug(String.format("east: trying to check %d,%d",x,y+i));
				if (y+i < boards[p][x].length && boards[p][x][y+i] != '-') {
					throw new PlaceShipRulesViolation(String.format("ship east of %d,%d",x,y+i));
				}
			}
		
		}
		
		debug(String.format("drawing ship %d,%d len %d",x,y,shiplength));
		
		for (int i = 0; i < shiplength; i++) {
			int n = (alongXAxis ? x+i : x);
			int m = (!alongXAxis ? y+i : y);
			if (i == 0)
				boards[p][n][m] = (alongXAxis ? 't' : 'l');
			else if (i == shiplength-1)
				boards[p][n][m] = (alongXAxis ? 'b' : 'r');
			else
				boards[p][n][m] = (alongXAxis ? 'v' : 'h');
		}
	}
	
	public Character[][] getBoard(int player) {
		return boards[player];
	}
	
	public String getLevelString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < boards[0].length; i++) {
			for (int j = 0; j < boards[0][0].length; j++) {
				sb.append(boards[0][i][j]);
			}
			sb.append("|");
			for (int j = 0; j < boards[0][0].length; j++) {
				sb.append(boards[1][i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public Level getLevel() {
		try {
			return new Level(getLevelString());
		} catch (InvalidLevelException e) {
			Map2DHelper<Object> helper = new Map2DHelper<Object>();
			System.err.println(helper.getBoardString(getBoard(0)));
			System.err.println(helper.getBoardString(getBoard(1)));
			
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		for (int i=0; i<500; i++) {
			System.out.println("==" + i +"==");
			
			LevelGenerator lg = new LevelGenerator(12, 12);
		
			//Map2DHelper<Object> helper = new Map2DHelper<Object>();
			//debug(helper.getBoardString(lg.getBoard(0)));
			//debug(helper.getBoardString(lg.getBoard(1)));
		
			System.out.println(lg.getLevel().toString());
		}
	}

}
