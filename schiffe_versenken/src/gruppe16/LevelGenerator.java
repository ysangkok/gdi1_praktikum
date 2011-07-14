package gruppe16;

import gruppe16.exceptions.InvalidLevelException;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class LevelGenerator {

	private static boolean DEBUG = false;
	private static void debug(String str) {
		if (DEBUG)
			System.out.println(str);
	}
	
	Map<Integer, Integer> boatCount;
	private Character[][][] boards;
	Random gen;
	int xwidth;
	int ywidth;
	
	public LevelGenerator(int xwidth, int ywidth) {
		this.xwidth = xwidth;
		this.ywidth = ywidth;
		
		boards = new Character[2][xwidth][ywidth];
		for (int k = 0; k <= 1; k++)
			for (int i = 0; i < xwidth; i++)
				for (int j = 0; j < ywidth; j++)
					boards[k][i][j] = '-';
		
		gen = new Random();
		
		boatCount = new TreeMap<Integer, Integer>();
		boatCount.put(2, 4); // 4 schnellbote
		boatCount.put(3, 3);
		boatCount.put(4, 2);
		boatCount.put(5, 1);
		
	    Iterator it = boatCount.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, Integer> pairs = (Map.Entry<Integer, Integer>)it.next();
			
			for (int i = 0; i < pairs.getValue(); i++) {
				for (int p : new int[] {0, 1}) {
					placeShipLoop((int) pairs.getKey(), p);
					try {
						Level.checkShips(p, boards[p], false);
					} catch (InvalidLevelException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}
	}
	
	private void placeShipLoop(int shiplength, int p) {
		for (;;) {
			if (placeShip(shiplength, p))
				return;
		}
	}
	
	private boolean placeShip(int shiplength, int p) {
		boolean alongxaxis = gen.nextBoolean();
		int x;
		int y;
		if (alongxaxis) {
			 x = gen.nextInt(xwidth - shiplength);
			 y = gen.nextInt(ywidth);
		} else {
			 x = gen.nextInt(xwidth);
			 y = gen.nextInt(ywidth - shiplength);			
		}
		
		debug(String.format("considering %d,%d , alongxaxis:%s",x,y,alongxaxis));
		
		debug("check north/west");
		
		if (alongxaxis) { // check north or west of ship
			debug(String.format("north: trying to check %d,%d",x-1,y));
			if (x-1 >= 0 && boards[p][x-1][y] != '-') {
				debug(String.format("ship north of %d,%d",x-1,y));
				return false;
			}
		} else {
			debug(String.format("west: trying to check %d,%d",x,y-1));
			if (y-1 >= 0 && boards[p][x][y-1] != '-') {
				debug(String.format("ship west of %d,%d",x,y-1));
				return false;
			}
		}
		
		{
			int i;

			debug("check each tile");

			for (i = 0; i < shiplength; i++) { // check next to each tile of ship
				int n = (alongxaxis ? x+i : x);
				int m = (!alongxaxis ? y+i : y);
				if (boards[p][n][m] != '-')
					return false;
				if (!alongxaxis) {
					if (n-1 >= 0 && boards[p][n-1][m] != '-') return false;
					if (n+1 < boards[p].length && boards[p][n+1][m] != '-') return false;
				} else {
					if (m-1 >= 0 && boards[p][n][m-1] != '-') return false;
					if (m+1 < boards[p][n].length && boards[p][n][m+1] != '-') return false;
				}
			}

			debug("check south/east");

			if (alongxaxis) { // check south or east of ship
				debug(String.format("south: trying to check %d,%d",x+i,y));
				if (x+i < boards[p].length && boards[p][x+i][y] != '-') {
					debug(String.format("ship south of %d,%d",x+i,y));
					return false;
				}
			} else {
				debug(String.format("east: trying to check %d,%d",x,y+i));
				if (y+i < boards[p][x].length && boards[p][x][y+i] != '-') {
					debug(String.format("ship east of %d,%d",x,y+i));
					return false;
				}
			}
		
		}
		
		debug(String.format("drawing ship %d,%d len %d",x,y,shiplength));
		
		for (int i = 0; i < shiplength; i++) {
			int n = (alongxaxis ? x+i : x);
			int m = (!alongxaxis ? y+i : y);
			if (i == 0)
				boards[p][n][m] = (alongxaxis ? 't' : 'l');
			else if (i == shiplength-1)
				boards[p][n][m] = (alongxaxis ? 'b' : 'r');
			else
				boards[p][n][m] = (alongxaxis ? 'v' : 'h');
		}
		return true;
	}
	
	public Character[][] getBoard(int player) {
		return boards[player];
	}
	
	public Level getLevel() {
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
		try {
			return new Level(sb.toString());
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
