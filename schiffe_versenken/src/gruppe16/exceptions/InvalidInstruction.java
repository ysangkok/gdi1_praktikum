package gruppe16.exceptions;

public class InvalidInstruction extends Exception {
	@Override public String getMessage() {
		return "Input invalid! Try again.";
	}

}
