package gruppe16.exceptions;

public class InvalidInstruction extends Exception {
	private static final long serialVersionUID = 1L;
	
	String message;
	public InvalidInstruction(int x, int y, char c) {
		message = "Char: " + c + ", X: " + x + ", Y: " + y;
	}
	public InvalidInstruction(String message) {
		this.message = message;
	}
	
	@Override public String getMessage() {
		return "Input invalid! Try again. " + message;
	}

}
