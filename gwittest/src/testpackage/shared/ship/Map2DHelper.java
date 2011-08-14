package testpackage.shared.ship;

import java.util.List;

import testpackage.shared.ship.Util;

/**
 * helper for printing the board (debugging)
 *
 * @param <T> type of each element in board
 */
public class Map2DHelper<T> {
	/**
	 * formats board for output. currently unused.
	 * @param a board to view
	 * @return string for output
	 */
	public String getBoardString(char[][] a) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				sb.append(Util.format("%d ", a[i][j]));
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$#" + n + "s", s);  
	}
	public String getCustomPaddedBoardString(Boolean[][] a, int pad) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				sb.append((a[i][j] ? "." : "#") + " ");
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();	
	}
	
	/**
	 * formats board for output
	 * @param a board to view
	 * @return string for output
	 */
	public String getBoardString(T[][] a) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				//System.err.println(a[i][j]);
				sb.append(Util.format("%d ", a[i][j]));
			}
			sb.append("\n");
		}
		sb.append("\n");
		//System.err.println(sb.toString());
		return sb.toString();
	}

	/**
	 * formats board for output
	 * @param a board to view
	 * @return string for output
	 */
	public String getBoardString(List<List<T>> a) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.size(); i++) {
			for (int j = 0; j < a.get(i).size(); j++) {
				//sb.append(Integer.toString(i) + "," + Integer.toString(j));
				sb.append(Util.format("%d ", a.get(i).get(j)));
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
}
