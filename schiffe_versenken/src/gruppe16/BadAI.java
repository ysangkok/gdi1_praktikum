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
	
	private int shooterx = 0;
	private int shootery = 0;
	
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
		int x, y;
		x = gen.nextInt(xwidth);
		y = gen.nextInt(ywidth);
		try {
			engine.attack(player, x, y);
		} catch (InvalidInstruction e) {
			if (e.getReason() == InvalidInstruction.Reason.NOTYOURTURN) return;
			if (e.getReason() == InvalidInstruction.Reason.NOSHOOTERDESIGNATED || e.getReason() == InvalidInstruction.Reason.NOMOREAMMO) {
				try {
					engine.chooseFiringXY(player, shooterx, shootery);
				} catch (InvalidInstruction e1) {
					throw new RuntimeException("ran out, should have been caught by win detector");
				}
				int num = (shooterx*xwidth + shootery);
				num++;
				if (!(num <= xwidth*ywidth-1)) System.err.println("no more usable fields, will run out next turn");
				shooterx = (int) Math.floor( (double) num / xwidth);
				shootery = num % xwidth;
			}
			playAs(player);
		}
		
	}

	
}
