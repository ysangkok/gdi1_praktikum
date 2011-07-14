package adapters;

import gruppe16.State;
import gruppe16.LevelGenerator;


/**
 * This is the test adapter for the first extended stage of completion.
 * Implement all method stubs in order for the tests to work.
 * <br><br>
 * <i>Note:</i> This test adapter inherits from the minimal test adapter
 * 
 * @see BattleshipTestAdapterMinimal
 * 
 * @author Jonas Marczona
 * @author Fabian Vogt
 */
public class BattleshipTestAdapterExtended1 extends BattleshipTestAdapterMinimal {

	/**
	 * Use this constructor to initialize everything you need.
	 */
	public BattleshipTestAdapterExtended1() {
		super();
	}
	
	/**
	 * Generates an entire new level.
	 * Both the player and computer sides should be generated using the default board size and ship configuration.
	 * @see BattleshipTestAdapterMinimal#getCurrentLevelStringRepresentation()
	 */
	public void generateCompleteLevel() {
		engine.setState(new State(new LevelGenerator(10, 10).getLevel()));
	}

}
