package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidLevelException;

import java.util.LinkedList;
import java.util.List;

public class Engine {

	private List<State> undoLog;
	State state;
	
	public boolean finished = false;

	public Engine() {
		undoLog = new LinkedList<State>();
		
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
		
		Level initialLevel;
		
		try {
			initialLevel = new Level(text);
		} catch (InvalidLevelException e) {
			System.err.println(e.getMessage());
			finished = true; 
			return;
		}
		
		State initialState = new State(initialLevel);
		
		undoLog.add(initialState);
		
		this.state = initialState;
	}
	
	public void attack(int player, int x, int y) throws InvalidInstruction {
		State newState = state.clone();
		undoLog.add(newState);
		
		newState.level.attack(player, x, y);
	}

	public Level getLevel() {
		return state.level;
	}
}
