package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidLevelException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * class for parsing, storing, manipulating and outputting levels
 */
public class Level {
	
	static final String unharmedShip = "lrtbvh";
	static final String   harmedShip = "LRTBVH";
	
	static final String pat = "[" + Pattern.quote(unharmedShip + harmedShip + "-*") + "]"; // all valid level file characters, except the seperator ('|') and newlines
	
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
	Level(String text) throws InvalidLevelException  {
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
				if (prevChar == '\n') { // we can only check the boards if the last two lines aren't empty. if the previous character was a newline, they are empty.
					checkBoards(counter);
				}
				break;
			}
			if (Pattern.matches(pat, new String(new char[] {c}))) {
				if (IsP1) {
					p1line.add(c);
				} else {
					p2line.add(c);
				}
			} else if (c == '|') { // seperator means that the next character belongs to other player board
				IsP1 = !IsP1;
			} else if (c == '\n') { // newline means that the same as seperator, but we'll also shift focus to next line in our lists
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
		try {
			line = boards.get(player).get(x);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInstruction();
		}
		char c = line.get(y);
		
		if (!Pattern.matches("[" + Pattern.quote(unharmedShip + "-") + "]", new String(new char[] {c}))) {
			throw new InvalidInstruction();
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
				if (Pattern.matches("[" + Pattern.quote(unharmedShip) + "]", new String(new char[] {board[i][j]}))) {
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
	
}

