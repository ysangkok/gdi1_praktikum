package testpackage.shared.ship;

/**
 * mother class for implementing AI's
 */
abstract public class AI {
	public void setEngine(Engine engine) {
		if (engine.isShotsPerShipEnabled() && !supportsAmmo()) throw new RuntimeException("Incapable AI assigned to engine with shots per ship enabled!");
		if (engine.isRangeEnabled() && !supportsRange()) throw new RuntimeException("Incapable AI assign to engine with range enabled!");
	}
	
	/**
	 * called when the AI should make a move
	 * @param player the player number it should play as (0 or 1)
	 */
	abstract public void playAs(int player);

	abstract public boolean supportsAmmo();
	abstract public boolean supportsRange();
}
