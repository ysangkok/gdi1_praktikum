package gruppe16;

/**
 * mother class for implementing AI's
 */
abstract public class AI {
	/**
	 * called when the AI should make a move
	 * @param player the player number it should play as (0 or 1)
	 */
	abstract public void playAs(int player);
}
