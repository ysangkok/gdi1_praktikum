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
		while (!engine.finished) {
			print(engine.getLevel().toString());
			int x = readInt("X Koordinate:");
			int y = readInt("Y Koordinate:");
			
			try {
				engine.attack(0, x, y);
			} catch (InvalidInstruction e) {
				showErrorMessage(e.getMessage());
				continue;
			}
		}

	}
	
	public static void main(String[] args) {
		new CLISchiffe().start();
	}

}
