package testpackage.shared.ship;

import testpackage.shared.ship.Util;
import testpackage.shared.ship.exceptions.InvalidInstruction;

import java.util.Random;

/**
 * stupid AI that randomly chooses target fields
 */
public class BadAI extends AI {
	
	class NoMoreMovesAvailableException extends RuntimeException {
		private static final long serialVersionUID = 1L;	
	}

	public boolean supportsAmmo() { return true; }
	public boolean supportsRange() { return true; }

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
	private int shootery = -1;
	
	/**
	 * constructor takes the game engine to play in
	 * @param engine game engine to manipulate
	 */
	@Override
	public void setEngine(Engine engine) {
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
			//System.err.println(e.getMessage());
			if (e.getReason() == InvalidInstruction.Reason.NOTYOURTURN) return;
			if (e.getReason() == InvalidInstruction.Reason.NOSHOOTERDESIGNATED || e.getReason() == InvalidInstruction.Reason.NOMOREAMMO) {
				chooseNewShooter(player);
			} else if (e.getReason() == InvalidInstruction.Reason.WATERCANTSHOOT || e.getReason() == InvalidInstruction.Reason.OUTOFSHOOTERRANGE) {
				if (!engine.canShootAnythingWithCurrentShip(player))
					chooseNewShooter(player);
			}
			System.err.println("BadAI: " + e.getMessage());
			//playAs(player);
		}
		
	}
	private void chooseNewShooter(int player) {
		int num = (shooterx*xwidth + shootery);
		num++;
		if (!(num <= xwidth*ywidth-1)) return; // no more usable fields, will run out next turn
		shooterx = (int) Math.floor( (double) num / xwidth);
		shootery = num % xwidth;
		
		try {
			engine.chooseFiringXY(player, shooterx, shootery);
		} catch (InvalidInstruction e1) {
			throw new RuntimeException(Util.format("can't choose firing (x,y): %d %d",shooterx,shootery) );
		}
	}

	
}
