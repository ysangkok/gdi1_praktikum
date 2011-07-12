package gruppe16;

import gruppe16.exceptions.InvalidLevelException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Level {
	
	static final String pat = "[" + Pattern.quote("lrtbvhLRTBVH-*") + "]";
	
	List<List<List<Character>>> boards;
	
	Level(String text) throws InvalidLevelException  {
		InputStream stream;
		try {
			stream = new ByteArrayInputStream(text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		
		int counter = 0;
		
		List<List<Character>> Player1Board = new ArrayList<List<Character>>();
		List<List<Character>> Player2Board = new ArrayList<List<Character>>();
		boards = new ArrayList<List<List<Character>>>();
		boards.add(Player1Board);
		boards.add(Player2Board);
		Player1Board.add(new ArrayList<Character>());
		Player2Board.add(new ArrayList<Character>());
		List<Character> p1line = Player1Board.get(counter);
		List<Character> p2line = Player2Board.get(counter);
		counter++;
		boolean IsP1 = true; 
		
		for (;;) {
			char c = 0;
			char prevChar = 0;
			try {
				prevChar = c;
				c = (char) stream.read();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			if (c == -1 || c == 65535) {
				if (prevChar == '\n') {
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
			} else if (c == '|') {
				IsP1 = !IsP1;
			} else if (c == '\n') {
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
	}

	private void checkBoards(int counter) throws InvalidLevelException {
		for (int i : new int[2]) {
			if (boards.get(i).get(counter-1).size() != boards.get(i).get(counter-2).size()) {
				throw new InvalidLevelException("Player 1 line " + counter + " length is not equal to previous length");
			}
		}
	}
	
	public Character[][] getPlayerBoard(int player) {
		Character[][] res = new Character[boards.get(player).size()][boards.get(player).get(0).size()];
		int i = 0;
		for (List<Character> l: boards.get(player)) {
			res[i++] = l.toArray(new Character[0]);
		}
		
		return res;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boards.get(0).size(); i++) {
        	sb.append(new String( unboxedArray(boards.get(0).get(i).toArray(new Character[0]) ) ) );
        	sb.append("|");
        	sb.append(new String( unboxedArray(boards.get(0).get(i).toArray(new Character[0]) ) ) );
        	sb.append("\n");
        }
            
        return sb.toString();
	}

	private static char[] unboxedArray(Character[] array) {
        char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++)
                result[i] = array[i];
        return result;
	}
	
}

