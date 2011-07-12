package gruppe16.exceptions;

import gruppe16.Map2DHelper;

import java.util.List;

public class InvalidLevelException extends Exception {
	String message; 
	
	public InvalidLevelException(String string) {
		this.message = string;
	}
	public InvalidLevelException(String string, List<List<Character>> list) {
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		StringBuilder sb = new StringBuilder();
		sb.append(string);
		sb.append("\n\n" + helper.getBoardString(list) + "\n\n");
		this.message = sb.toString();
	}
	public String getMessage() {
		return message;
	}

	private static final long serialVersionUID = 1L;
}