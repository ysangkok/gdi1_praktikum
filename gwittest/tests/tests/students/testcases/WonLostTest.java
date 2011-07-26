package tests.students.testcases;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import tests.adapters.BattleshipTestAdapterMinimal;

public class WonLostTest {
	
	private static final String LEVEL_WON = 
		"lr--t-*-lr|LR-------T\n"+
		"-*--B-*-*-|---LHR---B\n"+
		"--*----*--|----------\n"+
		"--T**LHhr-|------LHR-\n"+
		"--V--*-*--|--LHR-----\n"+
		"--V-lhhr--|T---------\n"+
		"--V-*--*-t|V-T--LHHR-\n"+
		"--B-LHR--b|V-V-------\n"+
		"-*-*-*----|V-V--T----\n"+
		"lhr*--*lHr|B-B--B--LR";

	
	private static final String LEVEL_LOST = 
		"LR**T***LR|Lr-------t\n"+
		"****B*****|---lhr---b\n"+
		"**********|----------\n"+
		"**T**LHHR*|------lhr-\n"+
		"**V*******|--lhr-----\n"+
		"**V*LHHR**|t---------\n"+
		"**V******T|v-t--lhhr-\n"+
		"**B*LHR**B|v-v-------\n"+
		"**********|v-v--t----\n"+
		"LHR****LHR|b-b--b--lr";
	
	private static final String LEVEL_ALMOST_WON = 
		"lr--t-*-lr|LR-------T\n"+
		"-*--B-*-*-|---LhR---B\n"+
		"--*----*--|----------\n"+
		"--T**LHhr-|------LHR-\n"+
		"--V--*-*--|--LHR-----\n"+
		"--V-lhhr--|T---------\n"+
		"--V-*--*-t|V-T--LHHR-\n"+
		"--B-LHR--b|V-V-------\n"+
		"-*-*-*----|V-V--T----\n"+
		"lhr*--*lHr|B-B--B--LR";
	
	private static final String LEVEL_ALMOST_LOST = 
		"LR**T***LR|Lr-------t\n"+
		"****b*****|---lhr---b\n"+
		"**********|----------\n"+
		"**T**LHHR*|------lhr-\n"+
		"**V*******|--lhr-----\n"+
		"**V*LHHR**|t---------\n"+
		"**V******T|v-t--lhhr-\n"+
		"**B*LHR**B|v-v-------\n"+
		"**********|v-v--t----\n"+
		"LHR****LHR|b-b--b--lr";
	
	BattleshipTestAdapterMinimal adapter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterMinimal();
	}
	
	@Test
	public void testWon() {
		adapter.createGameUsingLevelString(LEVEL_WON);
		assertTrue(adapter.isValidLevel());

		assertTrue("The level should be won at this point.", adapter.isWon());
		assertFalse("The level should be won at this point.", adapter.isLost());
		assertTrue("The level should be won at this point.", adapter.isFinished());
	}
	
	@Test
	public void testAlmostWon() {
		adapter.createGameUsingLevelString(LEVEL_ALMOST_WON);
		assertTrue(adapter.isValidLevel());

		assertFalse("The level should not be won at this point.", adapter.isWon());
		assertFalse("The level should not be won at this point.", adapter.isFinished());
		assertFalse("The level should not be won at this point.", adapter.isLost());
		
		adapter.selectCell(15, 1);
		assertTrue("The level should be won at this point.", adapter.isWon());
		assertFalse("The level should be won at this point.", adapter.isLost());
		assertTrue("The level should be won at this point.", adapter.isFinished());
	}
	
	@Test
	public void testLost() {
		adapter.createGameUsingLevelString(LEVEL_LOST);
		assertTrue(adapter.isValidLevel());

		assertFalse("The level should be lost at this point.", adapter.isWon());
		assertTrue("The level should be lost at this point.", adapter.isLost());
		assertTrue("The level should be lost at this point.", adapter.isFinished());
	}

	@Test
	public void testAlmostLost() {
		adapter.createGameUsingLevelString(LEVEL_ALMOST_LOST);
		assertTrue(adapter.isValidLevel());

		assertFalse("The level should not be finished at this point.", adapter.isWon());
		assertFalse("The level should not be finished at this point.", adapter.isLost());
		assertFalse("The level should not be finished at this point.", adapter.isFinished());
		
		adapter.selectCell(15, 1);
		assertFalse("The level should not be finished at this point.", adapter.isWon());
		assertFalse("The level should not be finished at this point.", adapter.isLost());
		assertFalse("The level should not be finished at this point.", adapter.isFinished());
		
		adapter.doAIShot();
		assertFalse("The level should be lost at this point.", adapter.isWon());
		assertTrue("The level should be lost at this point.", adapter.isLost());
		assertTrue("The level should be lost at this point.", adapter.isFinished());
	}
}
