package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
//import gruppe16.exceptions.InvalidLevelException;
import acm.program.ConsoleProgram;

public class CLISchiffe extends ConsoleProgram {

	private static final long serialVersionUID = 1L;

	Engine engine; 
	
	CLISchiffe() {
		engine = new Engine();
	}
	
	public void run() {
		int winner = -1;
		AI computer = new BadAI(engine);
		boolean humanPlays = true;
		while (!engine.isFinished()) {
			print(engine.getLevelStringForPlayer((humanPlays ? 0 : 1)));
			//if (false) {
			if (humanPlays) {
				println("Your turn!");
				int y = readInt("X Koordinate:");
				int x = readInt("Y Koordinate:");

				try {
					engine.attack(0, x, y);
				} catch (InvalidInstruction e) {
					showErrorMessage(e.getMessage());
					continue;
				}
			} else {
				println("Computer plays!");
				pause(1000);
				computer.playAs(1);
			}
			
			winner = engine.checkWin();
			humanPlays = !humanPlays;
		}
		println("Game over");
		if (winner == -1) {
			println("Draw");
		} else {
			println("Player " + (winner+1) + " won!");
		}

	}
	
	public static void main(String[] args) {
		new CLISchiffe().start();
	}

}
