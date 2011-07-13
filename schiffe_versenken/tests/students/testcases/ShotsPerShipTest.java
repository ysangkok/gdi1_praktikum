package students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import adapters.BattleshipTestAdapterExtended2;

public class ShotsPerShipTest {

	private static final String LEVEL = 
			"lr--t---lr|lr-------t\n"+
			"----b-----|---lhr---b\n"+
			"----------|----------\n"+
			"--t--lhhr-|------lhr-\n"+
			"--v-------|--lhr-----\n"+
			"--v-lhhr--|t---------\n"+
			"--v------t|v-t--lhhr-\n"+
			"--b-lhr--b|v-v-------\n"+
			"----------|v-v--t----\n"+
			"lhr----lhr|b-b--b--lr";
	
	BattleshipTestAdapterExtended2 adapter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterExtended2();
	}

	@Test
	public void testSinglePlayerShot() {
		adapter.enableShotsPerShipForNextGame();
		adapter.createGameUsingLevelString(LEVEL);
		
		assertEquals("A new ship part should have 5 shots.", 5, adapter.remainingShotsAtPosition(0, 0));
		assertEquals("A new ship part should have 5 shots.", 5, adapter.remainingShotsAtPosition(11, 0));
		
		adapter.selectCell(0, 0);
		adapter.selectCell(11, 0);
		
		assertEquals("A shot should decrement the ammuniation on the shooting ship part.", 4, adapter.remainingShotsAtPosition(0, 0));
	}
	
	@Test
	public void testNoHit() {
		adapter.enableShotsPerShipForNextGame();
		adapter.createGameUsingLevelString(LEVEL);
		
		assertEquals("A new ship part should have 5 shots.", 5, adapter.remainingShotsAtPosition(0, 0));
		assertEquals("A new water field should have 0 shots.", 0, adapter.remainingShotsAtPosition(13, 0));
		
		adapter.selectCell(0, 0);
		adapter.selectCell(13, 0);
		
		assertEquals("A shot should decrement the ammuniation on the shooting ship part.", 4, adapter.remainingShotsAtPosition(0, 0));
		assertEquals("A water field will always have 0 ammunition.", 0, adapter.remainingShotsAtPosition(13, 0));
	}
	
	@Test
	public void testHit() {
		adapter.enableShotsPerShipForNextGame();
		adapter.createGameUsingLevelString(LEVEL);
		
		assertEquals("A new ship part should have 5 shots.", 5, adapter.remainingShotsAtPosition(0, 0));
		assertEquals("A new ship part should have 5 shots.", 5, adapter.remainingShotsAtPosition(11, 0));
		
		adapter.selectCell(0, 0);
		adapter.selectCell(11, 0);
		
		assertEquals("A shot should decrement the ammuniation on the shooting ship part.", 4, adapter.remainingShotsAtPosition(0, 0));
		assertEquals("A hit ship part should always lose its whole ammunition.", 0, adapter.remainingShotsAtPosition(11, 0));
	}
	
	@Test
	public void testIllegalShootingShip() {
		adapter.enableShotsPerShipForNextGame();
		adapter.createGameUsingLevelString(LEVEL);
		
		adapter.selectCell(0, 1);
		adapter.selectCell(11, 0);
		
		assertFalse("No valid shooting ship was selected, so there should be not hit.", adapter.isCellHitAt(11, 0));
		assertEquals("No valid shooting ship was selected, so there should be not hit.", 5, adapter.remainingShotsAtPosition(11, 0));
	}
}
