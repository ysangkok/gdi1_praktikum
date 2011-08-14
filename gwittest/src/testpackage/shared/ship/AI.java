package testpackage.shared.ship;

/**
 * mother class for implementing AI's
 */
abstract public class AI {
	abstract public void setEngine(Engine engine);
	
	/**
	 * called when the AI should make a move
	 * @param player the player number it should play as (0 or 1)
	 */
	abstract public void playAs(int player);

	abstract public boolean supportsAmmo();
	abstract public boolean supportsRange();
}
