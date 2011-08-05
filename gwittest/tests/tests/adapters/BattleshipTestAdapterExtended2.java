package tests.adapters;

import testpackage.shared.ship.Level;
import testpackage.shared.ship.Rules;
import testpackage.shared.ship.State;
import testpackage.shared.ship.exceptions.InvalidInstruction;
import testpackage.shared.ship.exceptions.InvalidLevelException;
import tests.adapters.BattleshipTestAdapterExtended1;
import tests.adapters.BattleshipTestAdapterMinimal;

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
	
	private boolean enable = false;
	
	/**
	 * Use this constructor to initialize everything you need.
	 */
	public BattleshipTestAdapterExtended2() {
		super();
	}
	
	@Override
	public
	void createGameUsingLevelString(String levelstring) {
		try {
			engine.setState(new State(new Level(levelstring)));
		} catch (InvalidLevelException e) {
			// according to specification in Minimal adapter we should just ignore invalid levels and keep the last. Since this Test Level doesn't test level validity, no record is kept
		}
		if (enable) engine.enableShotsPerShip(Rules.shotsPerShipPart);
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
		int[] coords = Level.parseTestInterfaceCoords(x, y, engine.getyWidth());
		try {
			engine.chooseFiringXY(coords[0], coords[1], coords[2]);
			return engine.remainingShotsFor(coords[0]);
		} catch (InvalidInstruction e) {
			// shouldn't happen
			e.printStackTrace();
		}
		return -1; // returned when exception caught
	}
	
	/**
	 * Enables shots-per-ship game mode for the next game.
	 */
	public void enableShotsPerShipForNextGame() {
		enable = true;
	}
}
