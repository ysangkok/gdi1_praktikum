package gruppe16.exceptions;

import gruppe16.exceptions.InvalidInstruction.Reason;

public class InvalidInstruction extends Exception {
	public static enum Reason {
		UNKNOWN, NOTYOURTURN, OUTOFBOUNDS
	}
	
	private static final long serialVersionUID = 1L;
	
	String message = "";
	Reason reason = null;
	public InvalidInstruction(int x, int y, char c) {
		message = "Char: " + c + ", X: " + x + ", Y: " + y;
	}
	public InvalidInstruction(String message) {
		this.message = message;
	}
	public InvalidInstruction(Reason r) {
		this.reason = r;
	}
	
	@Override public String getMessage() {
		return "Input invalid! Try again. " + message  + (reason != null ? " Reason: " + reason : "");
		
	}
	public Reason getReason() {
		return this.reason;
	}

}
