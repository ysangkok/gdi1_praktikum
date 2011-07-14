package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidLevelException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

enum Orientation {
	VERTICAL, HORIZONTAL
}

enum Direction {
	DOWN, RIGHT
}

class Ship {
	public int x;
	public int y;
	public int len;
	public Orientation o;
	
	Ship(int x, int y, int length, Orientation o) {
		this.x = x;
		this.y = y;
		this.len = length;
		this.o = o;
	}
	
	public String toString() {
		return len + "-ship at (" + x + "," + y + "), orientation " + o;
	}
	
	public List<Integer[]> getAllOccupiedCoords() {
		//int[][] coords = new int[len][2];
		List<Integer[]> coords = new ArrayList<Integer[]>();
		
		for (int i=0; i<len; i++) {
			if (o == Orientation.HORIZONTAL) {
				coords.add(new Integer[] { x, y+i });
			} else if (o == Orientation.VERTICAL) {
				coords.add(new Integer[] { x+i, y });					
			} else {
				assert false;
			}
		}
		
		return coords;
	}
}


/**
 * class for parsing, storing, manipulating and outputting levels
 */
public class Level {
	
	private static boolean DEBUG = false;
	private static void debug(String str) {
		if (DEBUG)
			System.out.println(str);
	}
	
	static final String unharmedShip = "lrtbvh";
	static final String   harmedShip = "LRTBVH";
		
	List<List<List<Character>>> boards; // for storing the board of each player
	
	/**
	 * constructor that takes a all-ready List data structure
	 * @param newBoards 3D List. 1st dimension: players, 2nd dimension: lines, 3rd dimension: columns
	 */
	Level(List<List<List<Character>>> newBoards) {
		boards = newBoards;
	}
	
	/**
	 * constructor that takes the level as a string
	 * @param text level string
	 * @throws InvalidLevelException
	 */
	public Level(String text) throws InvalidLevelException  {
		InputStream stream;
		try {
			stream = new ByteArrayInputStream(text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		
		int counter = 0; // for counting lines
		
		LinkedList<List<Character>> Player1Board = new LinkedList<List<Character>>();
		LinkedList<List<Character>> Player2Board = new LinkedList<List<Character>>();
		boards = new ArrayList<List<List<Character>>>();
		boards.add(Player1Board);
		boards.add(Player2Board);
		Player1Board.add(new ArrayList<Character>());
		Player2Board.add(new ArrayList<Character>());
		List<Character> p1line = Player1Board.get(counter);
		List<Character> p2line = Player2Board.get(counter);
		counter++;
		boolean IsP1 = true; // the fields at the beginning of the file are always player one's
		
		for (;;) {
			char c = 0; // used for storing read character
			char prevChar = 0;
			try {
				prevChar = c;
				c = (char) stream.read();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			if (c == -1 || c == 65535) { // end of file
				if (prevChar == '\n' | prevChar == '|') { // we can only check the boards if the last two lines aren't empty. if the previous character was a newline, they are empty.
					checkBoards(counter);
				}
				break;
			}
			if (matchChar(unharmedShip + harmedShip + "-*", c)) {
				if (IsP1) {
					p1line.add(c);
				} else {
					p2line.add(c);
				}
			} else if (c == '|') { // Separator means that the next character belongs to other player board
				IsP1 = !IsP1;
			} else if (c == '\n') { // newline means that the same as separator, but we'll also shift focus to next line in our lists
				if (counter >= 2) {
					checkBoards(counter);
				}
				
				IsP1 = !IsP1;
				Player1Board.add(new ArrayList<Character>());
				Player2Board.add(new ArrayList<Character>());
				p1line = Player1Board.get(counter);
				p2line = Player2Board.get(counter);
				counter++;
			} else {
				throw new InvalidLevelException("Invalid character: " + c);
			}
		}
		if (p1line.size() == 0 && p2line.size() == 0) { // file had trailing newline
			Player1Board.removeLast();
			Player2Board.removeLast();
		}
		for ( int i : new int[] {0, 1})
			for ( int j=0; j<boards.size(); j++)
				for ( int k=0; k<boards.get(j).size(); k++)
					if (boards.get(j).size() != boards.get(i).get(k).size())
						throw new InvalidLevelException("different width and height");
		
		checkShips(0, getPlayerBoard(0), true);
		checkShips(1, getPlayerBoard(1), true);
	}
	
	public static void checkShips(int player, Character[][] b, boolean countShips) throws InvalidLevelException {
		debug("Checking player " + player);
		
		List<Ship> ships = new ArrayList<Ship>();
		
		for (int i=0; i<b.length; i++) {
			for (int j=0; j<b[i].length; j++) {
				char c = b[i][j];
				switch (c) {
				
				case 't':
				case 'T':
					ships.add(new Ship(i, j, countShipLength(i, j, Direction.DOWN, b, 0), Orientation.VERTICAL));
					break;
				case 'L':
				case 'l':
					ships.add(new Ship(i, j, countShipLength(i, j, Direction.RIGHT, b, 0), Orientation.HORIZONTAL));
					break;
					
				}
			}
		}
		
		Map<Integer, Integer[]> reference;
		reference = new TreeMap<Integer, Integer[]>();
		for (int i = 0; i < Rules.ships.length; i++) {
			reference.put(Rules.ships[i][0], new Integer[] {0, Rules.ships[i][1]});
		}
		
		for (Ship s : ships) { // count boats
			Integer[] counts = reference.remove(s.len);
			if (counts == null) throw new InvalidLevelException(s.len + "-boats are not allowed! Player: " + player + ", Coord: (" + s.x + "," + s.y + "), Orientation: " + s.o);
			counts[0]++;
			reference.put(s.len, counts);
		}
		
		if (countShips) {
			Iterator it = reference.entrySet().iterator();
			while (it.hasNext()) { // check counts
				Map.Entry<Integer, Integer[]> pairs = (Map.Entry<Integer, Integer[]>)it.next();
				if (pairs.getValue()[0] != pairs.getValue()[1]) throw new InvalidLevelException("Incorrect " + pairs.getKey() + "-ship count: " + pairs.getValue()[0] + ". Should be " + pairs.getValue()[1]);
			}
		}
	    
	    //Check ship proximity
	    for (Ship s : ships) {
	    	debug(s.toString());
	    	
	    	if (s.o == Orientation.HORIZONTAL) { // !alongxaxis
				debug(String.format("west: trying to check %d,%d",s.x,s.y-1));
				if (s.y-1 >= 0 && staticIsShip(s.x,s.y-1,b)) {
					throw new InvalidLevelException(String.format("ship north of %d,%d",s.x,s.y-1));
				}
	    	} else if (s.o == Orientation.VERTICAL) {
				debug(String.format("north: trying to check %d,%d",s.x-1,s.y));
				if (s.x-1 >= 0 && staticIsShip(s.x-1,s.y,b)) {
					throw new InvalidLevelException(String.format("ship north of %d,%d",s.x-1,s.y));
				}	    		
	    	}
	    	
	    	{
	    	
	    		int i = -1;
	    		int j = -1;

	    		for (Integer[] arr : s.getAllOccupiedCoords()) {
	    			debug(Arrays.toString(arr));
	    			i = arr[0];
	    			j = arr[1];
	    			if (s.o == Orientation.HORIZONTAL) {
	    				if (i>=1			&&	staticIsShip(i-1, j, b)) throw new InvalidLevelException(String.format("There is a ship at (%d,%d) too close north of this ship: %s",i-1, j, s.toString())); 
	    				if (i<b.length-1	&&	staticIsShip(i+1, j, b)) throw new InvalidLevelException(String.format("There is a ship at (%d,%d) too close south of this ship: %s",i+1, j, s.toString()));
	    			} else if (s.o == Orientation.VERTICAL) {
	    				if (j<b[0].length-1	&&	staticIsShip(i, j+1, b)) throw new InvalidLevelException(String.format("There is a ship at (%d,%d) too close east of this ship: %s", i, j+1, s.toString())); 
	    				if (j>=1			&&	staticIsShip(i, j-1, b)) throw new InvalidLevelException(String.format("There is a ship at (%d,%d) too close west of this ship: %s", i, j-1, s.toString()));
	    			}
	    		}
	    		
	    		i++;
	    		j++;
	    		debug(i + "," + j);

	    		if (s.o == Orientation.HORIZONTAL) {
	    			debug(String.format("east: trying to check %d,%d",s.x,j));
	    			if (j < b[s.x].length && staticIsShip(s.x,j,b)) {
	    				throw new InvalidLevelException(String.format("ship east of %d,%d",s.x,j));
	    			}

	    		} else if (s.o == Orientation.VERTICAL) {
	    			debug(String.format("south: trying to check %d,%d",i,s.y));
	    			if (i < b.length && staticIsShip(i,s.y,b)) {
	    				throw new InvalidLevelException(String.format("ship south of %d,%d",i,s.y));
	    			}
	    		}
	    	}
	    }
	}

	private static int countShipLength(int x, int y, Direction d, Character[][] b, int akku) throws InvalidLevelException {
		if (d == Direction.DOWN) {
			if (matchChar("bB", b[x][y])) return akku + 1;
			if (!matchChar("tTvV", b[x][y])) throw new InvalidLevelException(String.format("Expected ship at (%d,%d) direction %s",x,y,d));
			try {
				return countShipLength(x+1, y, d, b, akku + 1);
			} catch (ArrayIndexOutOfBoundsException e) {
				return akku;
			}
		} else if (d == Direction.RIGHT) {
			if (matchChar("rR", b[x][y])) return akku + 1;
			if (!matchChar("lLhH", b[x][y])) throw new InvalidLevelException(String.format("Expected ship at (%d,%d) direction %s",x,y,d));
			try {
				return countShipLength(x, y+1, d, b, akku + 1);
			} catch (ArrayIndexOutOfBoundsException e) {
				return akku;
			}
		}
		assert false;
		return -1;
	}

	public Level() {
		boards = new LevelGenerator(12, 12).getLevel().getBoards();
	}
	public List<List<List<Character>>> getBoards() {
		return boards;
	}

	/**
	 * verify board validity
	 * @param counter current line number being constructed, that means the previous two are complete
	 * @throws InvalidLevelException file invalid
	 */
	private void checkBoards(int counter) throws InvalidLevelException {
		for (int i : new int[] {0, 1}) {
			if (boards.get(i).get(counter-1).size() != boards.get(i).get(counter-2).size()) {
				throw new InvalidLevelException("Player 1 line " + counter + " length is not equal to previous length", boards.get(i));
			}
		}
	}
	
	/**
	 * get a player's board as an array with Characters
	 * @param player player number
	 * @return 2D array
	 */
	public Character[][] getPlayerBoard(int player) {
		Character[][] res = new Character[boards.get(player).size()][boards.get(player).get(0).size()];
		int i = 0;
		for (List<Character> l: boards.get(player)) {
			res[i++] = l.toArray(new Character[0]);
		}
		
		return res;
	}
	
	/**
	 * toString as mandated by assignment description. generates valid level that we can parse
	 * @return valid level
	 */
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boards.get(0).size(); i++) {
        	sb.append(new String( unboxedArray(boards.get(0).get(i).toArray(new Character[0]) ) ) );
        	sb.append("|");
        	sb.append(new String( unboxedArray(boards.get(1).get(i).toArray(new Character[0]) ) ) );
        	sb.append("\n");
        }
            
        return sb.toString();
	}
	
	private static boolean matchChar(String chars, char c) {
		return Pattern.matches("[" + Pattern.quote(chars) + "]", new String(new char[] {c}));
	}
	
	/**
	 * shoot at given coordinates
	 * @param player the shooting player
	 * @param x int 1st coord
	 * @param y int 2nd coord
	 * @return new character after shooting
	 * @throws InvalidInstruction given coordinates were invalid
	 */
	public char attack(int player, int x, int y) throws InvalidInstruction {
		if		(player == 1)	player = 0;
		else if (player == 0)	player = 1;
		else 					assert true;
		
		List<Character> line;
		char c;
		try {
			line = boards.get(player).get(x);
			c = line.get(y);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInstruction(x,y,'\0');
		}
		
		if (!matchChar(unharmedShip + "-", c)) {
			throw new InvalidInstruction(x,y,c);
		}
		
		line.remove(y);
		
		if (c == '-')	{ c = '*'; } // mark water as hit
		else			{ c = Character.toUpperCase(c); } // mark ship as hit
				
		line.add(y, c);
		
		return c;
	}
	
	/**
	 * did given player lose this game?
	 * @param p player to consider
	 * @return true if lost, false if not
	 */
	public boolean isPlayerLoser(int p) {
		Character[][] board = getPlayerBoard(p);
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (matchChar(unharmedShip, board[i][j])) {
					//System.err.println("matched on " + i + ","+ j + "," + board[i][j]);
					return false; // if we have just one field of unharmed ship, we didn't lose yet
				}
			}
		}
		return true;
	}

	/**
	 * unboxes an 2D array of Characters. doesn't handle null values.
	 * @param array to unbox
	 * @return char[][] with the same values.
	 */
	private static char[] unboxedArray(Character[] array) {
        char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++)
                result[i] = array[i];
        return result;
	}
	
	public static void main(String[] args) {
		Level level;
		try {
			String text = "-lr-t-lr--|-lr-----t-\n"+
			"----b-----|---lhr--b-\n"+
			"----------|----------\n"+
			"--t--lhhr-|------lhr-\n"+
			"--v-------|--lhr-----\n"+
			"--v-lhhr--|t---------\n"+
			"--v------t|v-t--lhhr-\n"+
			"--b-lhr--b|v-v-------\n"+
			"----------|v-v--t-lr-\n"+
			"lhr-lhr---|b-b--b----";
			level = new Level(text);
		} catch (InvalidLevelException e) {
			System.err.println("Invalid: " + e.getMessage());
			return;
		}
		System.out.println(level.toString());

	}

	public boolean isShip(int i, int j) {
		int[] coords = parseTestInterfaceCoords(i,j,boards.get(0).get(0).size());
		return isShip(boards.get(coords[0]).get(coords[1]).get(coords[2]));
	}
	public boolean isShip(int p, int i, int j) {
		return isShip(boards.get(p).get(i).get(j));
	}
	public static boolean staticIsShip(int i, int j, Character[][] boards) {
		return isShip(boards[i][j]);
	}
	public static boolean isShip(char c) {
		return matchChar(unharmedShip + harmedShip, c);
	}

	public static int[] parseTestInterfaceCoords(int y, int x, int yWidth) {
		int newplayer, newx, newy;
		if (y > yWidth-1) {
			newplayer = 1;
			newy = y - yWidth - 1;
			newx = x;
		} else {
			newplayer = 0;
			newy = y;
			newx = x;
		}
		return new int[] {newplayer, newx, newy};
	}
	
}

