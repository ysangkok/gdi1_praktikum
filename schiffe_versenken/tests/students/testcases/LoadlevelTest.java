package students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import adapters.BattleshipTestAdapterMinimal;

/**
 * @author jonas
 *
 */
public class LoadlevelTest {
	
	private static final String correct_level = 
		"-lr-t-lr--|-lr-----t-\n"+
		"----b-----|---lhr--b-\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";

	/** one ship with two fields (on the player-side) is missing. */
	private static final String invalid_level_lessShips_player =  
		"-lr-t-----|-lr-----t-\n"+
		"----b-----|---lhr--b-\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";
	
	/** one ship with two fields (on the computer-side) is missing. */
	private static final String invalid_level_lessShips_computer =  
		"-lr-t-lr--|-lr-------\n"+
		"----b-----|---lhr----\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";

	/** /one ship with two fields (on the computer-side) is too much. */
	private static final String invalid_level_tooMuchShips_player =  
		"-lr-t-lr--|-lr-----t-\n"+
		"----b-----|---lhr--b-\n"+
		"----------|t---------\n"+
		"--t--lhhr-|b-----lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";

	
	/** one ship with two fields (on the computer-side) is too much. */
	private static final String invalid_level_tooMuchShips_computer =  
		"-lr-t-lr--|-lr-----t-\n"+
		"----b----t|---lhr--b-\n"+
		"---------b|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";

	private static final String invalid_level_invalidContact_player = 
		"tlr---lr--|-lr-----t-\n"+
		"b---------|---lhr--b-\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";

	private static final String invalid_level_invalidContact_computer = 
		"-lr-t-lr--|-lr-----tt\n"+
		"----b-----|---lhr--bb\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t-lr-\n"+
		"lhr-lhr---|b-b--b----";

	private static final String invalid_level_noBorder_waterInstead = 
		"-lr-t-lr----lr-----t-\n"+
		"----b---------lhr--b-\n"+
		"---------------------\n"+
		"--t--lhhr--------lhr-\n"+
		"--v----------lhr-----\n"+
		"--v-lhhr---t---------\n"+
		"--v------t-v-t--lhhr-\n"+
		"--b-lhr--b-v-v-------\n"+
		"-----------v-v--t-lr-\n"+
		"lhr-lhr----b-b--b----";
	private static final String invalid_level_noBorder1 = 
		"-lr-t-lr---lr-----t-\n"+
		"----b--------lhr--b-\n"+
		"--------------------\n"+
		"--t--lhhr-------lhr-\n"+
		"--v---------lhr-----\n"+
		"--v-lhhr--t---------\n"+
		"--v------tv-t--lhhr-\n"+
		"--b-lhr--bv-v-------\n"+
		"----------v-v--t-lr-\n"+
		"lhr-lhr---b-b--b----";
	
	BattleshipTestAdapterMinimal adapter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterMinimal();
	}

	@Test
	public void testLoadLevelFromString() {
		adapter.createGameUsingLevelString(correct_level);
		assertTrue(adapter.isValidLevel());
	}
	
	@Test
	public void testShipCount() {
		adapter.createGameUsingLevelString(correct_level);
		assertTrue("Level has correct number of ships.", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_lessShips_player);
		assertFalse("One two element ship on the player side is missing", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_lessShips_computer);
		assertFalse("One two element ship on the computer side is missing", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_tooMuchShips_player);
		assertFalse("One two element ship on the player side is too much", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_tooMuchShips_computer);
		assertFalse("One two element ship on the computer side is too much", adapter.isValidLevel());
	}

	@Test
	public void testBorder() {
		adapter.createGameUsingLevelString(correct_level);
		assertTrue("Level has correct border.", adapter.isValidLevel());
		
		adapter.createGameUsingLevelString(invalid_level_noBorder_waterInstead);
		assertFalse("Level has no border, there is water instead.", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_noBorder1);
		assertFalse("Level has not a correct border, the middle field is missing at all.", adapter.isValidLevel());
	}

	@Test
	public void testShipsNotInContact() {
		adapter.createGameUsingLevelString(correct_level);
		assertTrue("Level has correct placing of ships.", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_invalidContact_player);
		assertFalse("Two ships on player side (upper left) are in contact.", adapter.isValidLevel());

		adapter.createGameUsingLevelString(invalid_level_invalidContact_computer);
		assertFalse("Two ships on computer side (upper right) are in contact.", adapter.isValidLevel());
	}

	
	@Test
	public void testGetLevelAsString() {
		adapter.createGameUsingLevelString(correct_level);
		assertTrue(adapter.isValidLevel());
		assertEquals("Without any action the level should not change.", correct_level, adapter.getCurrentLevelStringRepresentation().trim());
	}
	
	
	@Test
	public void testLoadLevelTestShipPos() {
		/*
		 * Remember: the upper left is 0|0
		 * the lower left is 9|0
		 * the middle is 10|x ( 0 <= x < 10 )
		 * the lower right is 20|9
		 */
		adapter.createGameUsingLevelString(correct_level);
		assertTrue(adapter.isValidLevel());
		
		//first row from the player, water:
		assertFalse(adapter.isShipAt(0, 0));
		assertFalse(adapter.isShipAt(3, 0));
		assertFalse(adapter.isShipAt(5, 0));
		assertFalse(adapter.isShipAt(8, 0));
		assertFalse(adapter.isShipAt(9, 0));
		//first row from the player, ships:
		assertTrue(adapter.isShipAt(1, 0));
		assertTrue(adapter.isShipAt(2, 0));
		assertTrue(adapter.isShipAt(4, 0));
		assertTrue(adapter.isShipAt(6, 0));
		assertTrue(adapter.isShipAt(7, 0));
		
		//fourth row from the pc, water:
		assertFalse(adapter.isShipAt(11, 3));
		assertFalse(adapter.isShipAt(12, 3));
		assertFalse(adapter.isShipAt(13, 3));
		assertFalse(adapter.isShipAt(14, 3));
		assertFalse(adapter.isShipAt(15, 3));
		assertFalse(adapter.isShipAt(16, 3));
		assertFalse(adapter.isShipAt(20, 3));
		//fourth row from the pc, ships:
		assertTrue(adapter.isShipAt(17, 3));
		assertTrue(adapter.isShipAt(18, 3));
		assertTrue(adapter.isShipAt(19, 3));
	}
	

}
