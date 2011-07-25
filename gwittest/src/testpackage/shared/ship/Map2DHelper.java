package testpackage.shared.ship;

import java.util.Formatter;
import java.util.List;

import testpackage.shared.Util;

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
	String getBoardString(char[][] a) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				sb.append(Util.format("%1$2s", a[i][j]));
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
	String getBoardString(T[][] a) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				//System.err.println(a[i][j]);
				sb.append(Util.format("%1$2s", a[i][j]));
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
				sb.append(Util.format("%1$2s", a.get(i).get(j)));
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
}