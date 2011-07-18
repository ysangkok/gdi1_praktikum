package gruppe16.exceptions;

import gruppe16.Map2DHelper;

import java.util.List;

public class InvalidLevelException extends Exception {
	private String message; 
	private List<List<Character>> list;
	
	public InvalidLevelException(String string) {
		this.message = string;
	}
	public InvalidLevelException(String string, List<List<Character>> list) {
		StringBuilder sb = new StringBuilder();
		sb.append(string);
		//sb.append("\n\n" + helper.getBoardString(list) + "\n\n");
		this.list = list;
		this.message = sb.toString();
	}
	public String getMessage() {
		return message;
	}
	public void printBoard() {
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		System.err.println(helper.getBoardString(list));
	}

	private static final long serialVersionUID = 1L;
}