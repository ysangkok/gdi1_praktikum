package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
//import gruppe16.exceptions.InvalidLevelException;
import acm.program.ConsoleProgram;

/**
 * command line interface (ACM ConsoleProgram)
 */
public class CLISchiffe extends ConsoleProgram {

	private static final long serialVersionUID = 1L;

	Engine engine; 
	
	/**
	 * constructor initializes game engine, which constructs game boards and so on
	 */
	CLISchiffe() {
		engine = new Engine();
	}
	
	/**
	 * ACM JTF entry point
	 */
	public void run() {
		int winner = -1;
		AI computer = new BadAI(engine);
		boolean humanPlays = true; // human plays as player 1 and therefore plays first turn
		while (!engine.isFinished()) {
			print(engine.getLevelStringForPlayer((humanPlays ? 0 : 1)));
			//if (false) {
			if (humanPlays) {
				println("Your turn!");
				int y = readInt("X Koordinate:"); //coordinates are exchanged cause it's confusing
				int x = readInt("Y Koordinate:"); //if X is downwards and Y is towards right

				try {
					engine.attack(0, x, y);
				} catch (InvalidInstruction e) {
					showErrorMessage(e.getMessage());
					continue; // player gets another try
				}
			} else {
				println("Computer plays!");
				pause(1000);
				computer.playAs(1); // play as player 2 (players are 0 and 1)
			}
			
			winner = engine.checkWin(); // updates also isFinished()
			humanPlays = !humanPlays; // other player plays
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
