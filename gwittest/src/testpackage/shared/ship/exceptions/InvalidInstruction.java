package testpackage.shared.ship.exceptions;

import java.io.Serializable;

public class InvalidInstruction extends Exception implements Serializable {
	public static enum Reason {
		NOTYOURTURN, NOMOREAMMO, NOSHOOTERDESIGNATED, OUTOFBOUNDS, NOTSHOOTABLE, INVALIDSHOOTER
	}
	
	private static final long serialVersionUID = 1L;
	
	private String message = "";
	private Reason reason = null;
	public InvalidInstruction(int x, int y, char c, Reason r) {
		this.reason = r;
		message = "Char: " + c + ", X: " + x + ", Y: " + y;
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
	public InvalidInstruction() {
	}

}
