package gruppe16;

import gruppe16.exceptions.InvalidInstruction;

import java.util.Random;

/**
 * stupid AI that randomly chooses target fields
 */
public class BadAI extends AI {

	/**
	 * random generator used for finding coordinates to shoot at
	 */
	private Random gen;
	/**
	 * x width of map
	 */
	private int xwidth;
	/**
	 * y width of map
	 */
	private int ywidth;
	/**
	 * engine we're working with
	 */
	private Engine engine;
	
	/**
	 * constructor takes the game engine to play in
	 * @param engine game engine to manipulate
	 */
	public BadAI(Engine engine) {
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
			if (e.getReason() == InvalidInstruction.Reason.NOTYOURTURN) return;
			if (e.getReason() == InvalidInstruction.Reason.NOSHOOTERDESIGNATED || e.getReason() == InvalidInstruction.Reason.NOMOREAMMO) engine.setInfiniteAmmoFor(player,true);
			playAs(player);
		}
		
	}

	
}
