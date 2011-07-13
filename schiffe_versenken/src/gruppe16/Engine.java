package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidLevelException;

import java.util.LinkedList;
import java.util.List;

public class Engine {

	private List<State> undoLog;
	private State state;
	
	private boolean finished = false;
	private int xWidth;
	private int yWidth;

	public int getxWidth() {
		return xWidth;
	}

	public int getyWidth() {
		return yWidth;
	}

	public Engine() {
		undoLog = new LinkedList<State>();
		
		String text = 
			"lr--t---lr|lr--------\n"+
			"----b-----|----------\n"+
			"----------|----------\n"+
			"--t--lhhr-|----------\n"+
			"--v-------|----------\n"+
			"--v-lhhr--|----------\n"+
			"--v------t|----------\n"+
			"--b-lhr--b|----------\n"+
			"----------|----------\n"+
			"lhr----lhr|----------\n";
		
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
		this.xWidth = initialLevel.getPlayerBoard(0).length;
		this.yWidth = initialLevel.getPlayerBoard(0)[0].length;
	}
	
	public boolean attack(int player, int x, int y) throws InvalidInstruction {
		boolean hit;
		
		State newState = state.clone();
		undoLog.add(newState); 
		
		hit = newState.getLevel().attack(player, x, y);
		
		this.state = newState;
		//System.err.println(getLevel().toString());
		
		checkWin();
		
		boolean[][] fog = state.getFog(player);
		fog[x][y] = false;
		
		return hit;
	}
	
	public int checkWin() {
		for (int i : new int[] {0, 1}) {
			//System.err.println("Checking player " + i);
			if (state.getLevel().isPlayerLoser(i)) {
				this.finished = true;
				return otherPlayer(i);
			}
		}
		return -1;
	}
	
	private int otherPlayer(int i) {
		if (i == 0) {
			return 1;
		} else {
			return 0;
		}
	}
		
	public boolean isFinished() {
		return this.finished;
	}

	public String getLevelStringForPlayer(int i) {
		Character[][] opponent	= state.getLevel().getPlayerBoard(otherPlayer(i)	);
		Character[][] our		= state.getLevel().getPlayerBoard(i					);
		
		boolean[][] ourFog = state.getFog(i);
		
		for (int j = 0; j < ourFog.length; j++) {
			for (int k = 0; k < ourFog[j].length; k++) {
				if (ourFog[j][k])
					opponent[j][k] = '#';
			}
		}
		
		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		//return helper.getBoardString(new Character[][] {{'-'}, {'-'}});
		return "Your board:\n" + helper.getBoardString(our) + "Their board:\n" + helper.getBoardString(opponent);
		
		//return state.getLevel().toString();
	}

}
