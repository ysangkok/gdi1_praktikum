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
			//System.out.println(level.toString());
			
			newState = new State(new Level(level.toString()));
		} catch (InvalidLevelException e) {
			//System.err.println("We generated an invalid level!" + e.getMessage());
			e.printStackTrace();
			e.printBoard();
			return null;
		}
		return newState;
	}
	
	public static void main(String[] args) {
		String text = 
			"lr--t---lr|lr-------t\n"+
			"----b-----|---lhr---b\n"+
			"----------|----------\n"+
			"--t--lhhr-|------lhr-\n"+
			"--v-------|--lhr-----\n"+
			"--v-lhhr--|t---------\n"+
			"--v------t|v-t--lhhr-\n"+
			"--b-lhr--b|v-v-------\n"+
			"----------|v-v--t----\n"+
			"lhr----lhr|b-b--b--lr\n";
		Level level;
		try {
			level = new Level(text);
		} catch (Exception e) { return; }
		State st1 = new State(level);
		State st2 = st1.clone();
	}
}
