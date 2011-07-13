package gruppe16.exceptions;

public class InvalidInstruction extends Exception {
	int x;
	int y;
	char c;
	public InvalidInstruction(int x, int y, char c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	@Override public String getMessage() {
		return "Input invalid! Try again. Char: " + c + ", X: " + x + ", Y: " + y;
	}

}
