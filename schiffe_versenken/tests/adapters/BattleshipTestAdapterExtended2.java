package adapters;


/**
 * This is the test adapter for the second extended stage of completion.
 * Implement all method stubs in order for the tests to work.
 * <br><br>
 * <i>Note:</i> This test adapter inherits from the first extended test adapter
 * 
 * @see BattleshipTestAdapterMinimal
 * @see BattleshipTestAdapterExtended1
 * 
 * @author Jonas Marczona
 * @author Fabian Vogt
 */
public class BattleshipTestAdapterExtended2 extends BattleshipTestAdapterExtended1 {
	
	/**
	 * Use this constructor to initialize everything you need.
	 */
	public BattleshipTestAdapterExtended2() {
		//TODO implement this stub
	}
	
	/**
	 * Gets the remaining shots for a given position.
	 * If the position is a ship, the remaining shots for this ship should be returned.
	 * In any other case, 0 should be returned.
	 * 
	 * @param x	x coordinate
	 * @param y	y coordinate
	 * @return 0 or number of shots left.
	 */
	public int remainingShotsAtPosition(final int x, final int y) {
		//TODO implement this stub
		return -1;
	}
	
	/**
	 * Enables shots-per-ship game mode for the next game.
	 */
	public void enableShotsPerShipForNextGame() {
		//TODO implement this stub
	}
}
