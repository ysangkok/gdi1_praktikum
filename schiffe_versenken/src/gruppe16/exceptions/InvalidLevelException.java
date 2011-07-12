package gruppe16.exceptions;

public class InvalidLevelException extends Exception {
	public InvalidLevelException(char c) {
		System.out.println((int) c);
	}

	public InvalidLevelException(String string) {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
}