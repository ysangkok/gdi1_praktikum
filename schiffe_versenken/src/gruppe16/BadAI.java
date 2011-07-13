package gruppe16;

import gruppe16.exceptions.InvalidInstruction;

import java.util.Random;

/**
 * stupid AI that randomly chooses target fields
 */
public class BadAI extends AI {

	Random gen;
	int xwidth;
	int ywidth;
	Engine engine;
	
	/**
	 * constructor takes the game engine to play in
	 * @param engine game engine to manipulate
	 */
	BadAI(Engine engine) {
		gen = new Random();
		xwidth = engine.getxWidth();
		ywidth = engine.getyWidth();
		this.engine = engine;
	}
	
	/**
	 * randomly and recursively tries fields until it succeeds. doesn't care if it hits or not.
	 */
	@Override
	public void playAs(int player) {
		assert !engine.isFinished();
		try {
			engine.attack(player, gen.nextInt(xwidth), gen.nextInt(ywidth));
		} catch (InvalidInstruction e) {
			playAs(player);
		}
		
	}

	
}
