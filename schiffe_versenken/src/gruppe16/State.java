package gruppe16;

import gruppe16.exceptions.InvalidLevelException;

public class State implements Cloneable {
	public Level level;
	
	State(Level level) {
		this.level = level;
	}
	
	@Override public State clone() {
		State newState;
		try {
			newState = new State(new Level(level.toString()));
		} catch (InvalidLevelException e) {
			System.err.println("We generated an invalid level!" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return newState;
	}
}
