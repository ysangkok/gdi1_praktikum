package testpackage.shared.ship.exceptions;


import java.util.List;

import testpackage.shared.ship.Map2DHelper;

public class InvalidLevelException extends RuntimeException {
	private String message; 
	private Object list;

	public InvalidLevelException(String string) {
		this.message = string;
	}
	public void setBoards(Object list) {
		this.list = list;
	}
	public InvalidLevelException(String string, Object list) {
		this(string);
		this.list = list;
	}
	public String getMessage() {
		return message + (list == null ? "" : ". Invalid board available. Call InvalidLevelException.printBoard()");
	}
	public void printBoard() {
		if (list == null) {
			System.err.println("No invalid board available for debugging.");
		} else if (list instanceof Character[][][]) {
			Map2DHelper<Character> helper = new Map2DHelper<Character>();
			System.err.println(helper.getBoardString(((Character[][][]) list)[0]));
			System.err.println();
			System.err.println(helper.getBoardString(((Character[][][]) list)[1]));
		} else if (list instanceof List) {
			Map2DHelper<Character> helper = new Map2DHelper<Character>();
			System.err.println(helper.getBoardString((List<List<Character>>) list));
		} else {
			System.err.println("Don't know how to print invalid board.");
		}
	}

	private static final long serialVersionUID = 1L;
}
