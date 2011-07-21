package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
//import gruppe16.exceptions.InvalidLevelException;
import acm.program.ConsoleProgram;

/**
 * command line interface (ACM ConsoleProgram)
 */
public class CLISchiffe extends ConsoleProgram {

	private static final long serialVersionUID = 1L;

	private Engine engine; 
	
	private boolean shotspership = true;
	
	/**
	 * constructor initializes game engine, which constructs game boards and so on
	 */
	public CLISchiffe() {
		engine = new Engine();
		engine.enableShotsPerShip();
	}
	
	/**
	 * ACM JTF entry point
	 */
	public void run() {
		int winner = -1;
		AI computer = new BadAI(engine);
		while (!engine.isFinished()) {
			if (engine.getState().isPlayerTurn()) {
				print(engine.getLevelStringForPlayer(0));
				println("Your turn!");
				boolean tryAgain;
				do {
					tryAgain = false;
					
					int shootery = readInt("Shooter X Koordinate:");
					int shooterx = readInt("Shooter Y Koordinate:");
					engine.chooseFiringXY(0, shooterx, shootery);
					
					int y = readInt("X Koordinate:"); //coordinates are exchanged cause it's confusing
					int x = readInt("Y Koordinate:"); //if X is downwards and Y is towards right

					try {
						engine.attack(0, x, y);
					} catch (InvalidInstruction e) {
						showErrorMessage(e.getMessage());
						tryAgain = true; // player gets another try
					}
				} while (tryAgain);
			} else {
				println("Computer plays!");
				//pause(1000);
				computer.playAs(1); // play as player 2 (players are 0 and 1)
			}
			
			winner = engine.checkWin(); // updates also isFinished()
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
