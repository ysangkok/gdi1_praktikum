package students.testcases;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import adapters.BattleshipTestAdapterMinimal;
import students.utils.TestHelper;

public class BasicGameTest {
	
	

	private static final String CORRECT_LEVEL = 
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

	private static final String LEVEL_FIRST_SHOT = 
		"lr--t---lr|lr-------t\n"+
		"----b-----|---lhr---b\n"+
		"----------|----------\n"+
		"--t--lhhr-|--*---lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t----\n"+
		"lhr----lhr|b-b--b--lr";
	
	
	private static final String CORRECT_LEVEL_OTHER = 
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
	
	private static final String LEVEL_OTHER_FIRST_SHOT = 
		"lr--t---lr|Lr-------t\n"+
		"----b-----|---lhr---b\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t----\n"+
		"lhr----lhr|b-b--b--lr";

	private static final String LEVEL_OTHER_SECOND_SHOT = 
		"lr--t---lr|Lr-------t\n"+
		"----b-----|-*-lhr---b\n"+
		"----------|----------\n"+
		"--t--lhhr-|------lhr-\n"+
		"--v-------|--lhr-----\n"+
		"--v-lhhr--|t---------\n"+
		"--v------t|v-t--lhhr-\n"+
		"--b-lhr--b|v-v-------\n"+
		"----------|v-v--t----\n"+
		"lhr----lhr|b-b--b--lr";

	private static final String CORRECT_LEVEL_THREE =
		 "t----lr---|t---------\n"+
		 "b---------|v---lhhr--\n"+
		 "-------lr-|v-------t-\n"+
		 "--lhhr----|b-------b-\n"+
		 "-t--------|-t-lhhhr-t\n"+
		 "-b-----lhr|-b-------b\n"+
		 "----lhr---|t-lhr-----\n"+
		 "lhhr---lhr|v-------lr\n"+
		 "----------|b-lhr-----\n"+
		 "-lhhhr----|----------";
	private static final String LEVEL_THREE_ONE =
		 "t----lr---|t---------\n"+
		 "b---------|V---lhhr--\n"+
		 "-------lr-|v-------t-\n"+
		 "--lhhr----|b-------b-\n"+
		 "-t--------|-t-lhhhr-t\n"+
		 "-b-----lhr|-b-------b\n"+
		 "----lhr---|t-lhr-----\n"+
		 "lhhr---lhr|v-------lr\n"+
		 "----------|b-lhr-----\n"+
		 "-lhhhr----|----------";
	
	private static final String LEVEL_THREE_TWO = 
		 "t----lr---|t---------\n"+
		 "b---------|V---lhhr--\n"+
		 "-------lr-|V-------t-\n"+
		 "--lhhr----|b-------b-\n"+
		 "-t--------|-t-lhhhr-t\n"+
		 "-b-----lhr|-b-------b\n"+
		 "----lhr---|t-lhr-----\n"+
		 "lhhr---lhr|v-------lr\n"+
		 "----------|b-lhr-----\n"+
		 "-lhhhr----|----------";

	

	
	private static final String CORRECT_LEVEL_FOUR =
		"----------|----------\n"+
		"lhhr--lhhr|-lr-------\n"+
		"----------|-------lhr\n"+
		"t-t--lr---|-lhhr-----\n"+
		"v-v------t|t------t--\n"+
		"v-b------v|b-lhhr-v--\n"+
		"v----lr--b|-------v--\n"+
		"b-------t-|-lhr---v-t\n"+
		"----lr--v-|----lr-b-v\n"+
		"--lr----b-|-lr------b";
	private static final String LEVEL_FOUR_ONE = 
		"----------|----------\n"+
		"lhhr--lhhr|-lr-------\n"+
		"----------|-------lhr\n"+
		"t-t--lr---|-lhhr-----\n"+
		"v-v------t|t------t--\n"+
		"v-b------v|b-lhhr-v--\n"+
		"v----lr--b|-------v--\n"+
		"b-------t-|-lhr---v-t\n"+
		"----lr--v-|----lr-b-v\n"+
		"--lr----b-|-lr------B";

	private static final String CORRECT_LEVEL_FIVE =
		"lhhr---lr-|lhhhr----t\n"+
		"----lr----|---------b\n"+
		"-t----lr--|-lhr---t--\n"+
		"-v--------|-----t-v--\n"+
		"-v-lhhr---|-----b-b--\n"+
		"-v--------|-lhhr---lr\n"+
		"-b------t-|----------\n"+
		"t---lhr-v-|--lhr-lr--\n"+
		"v-------b-|----------\n"+
		"b----lr---|-----lhhr-";
	private static final String LEVEL_FIVE_ONE = 
		"lhhr---lr-|lhhhr----t\n"+
		"----lr----|---------b\n"+
		"-t----lr--|-lhr---t--\n"+
		"-v--------|-----t-v--\n"+
		"-v-lhhr---|-----b-b--\n"+
		"-v--------|-lhhr---lr\n"+
		"-b------t-|----------\n"+
		"t---lhr-v-|--lhr-lr--\n"+
		"v-------b-|----------\n"+
		"b----lr---|*----lhhr-";


	private static final String CORRECT_LEVEL_SIX = 
		"---lhr----|----lr----\n"+
		"--------t-|-t--------\n"+
		"lhhr-lr-b-|-v-t---lhr\n"+
		"---------t|-v-b------\n"+
		"--lhhhr--v|-v--------\n"+
		"---------b|-b---lr---\n"+
		"--lr---lr-|t---t--lhr\n"+
		"-----t----|v-t-v-t---\n"+
		"lhhr-v----|v-v-v-b---\n"+
		"-----b----|b-b-b-----";
	private static final String LEVEL_SIX_ONE = 
		"---lhr----|----lr----\n"+
		"--------t-|-t--------\n"+
		"lhhr-lr-b-|-v-t---lhr\n"+
		"---------t|-v-b------\n"+
		"--lhhhr--v|-v--------\n"+
		"---------b|-b--*lr---\n"+
		"--lr---lr-|t---t--lhr\n"+
		"-----t----|v-t-v-t---\n"+
		"lhhr-v----|v-v-v-b---\n"+
		"-----b----|b-b-b-----";

	BattleshipTestAdapterMinimal adapter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterMinimal();
	}
	
	@Test
	public void twoShots() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL);
		assertTrue(adapter.isValidLevel());

		adapter.selectCell(13, 3);
		assertEquals(LEVEL_FIRST_SHOT, adapter.getCurrentLevelStringRepresentation().trim());
		
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertFalse(LEVEL_FIRST_SHOT.equals(studentLevel));
		assertTrue(TestHelper.compareLevel(LEVEL_FIRST_SHOT, studentLevel, 1));
	}

	
	@Test
	public void twoShotsEach() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL_OTHER);
		assertTrue(adapter.isValidLevel());
		
		adapter.selectCell(11, 0);
		assertEquals(LEVEL_OTHER_FIRST_SHOT, adapter.getCurrentLevelStringRepresentation().trim());
		
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertFalse(LEVEL_OTHER_FIRST_SHOT.equals(studentLevel));
		assertTrue(TestHelper.compareLevel(LEVEL_OTHER_FIRST_SHOT, studentLevel, 1));
		
		adapter.selectCell(12, 1);
		assertFalse(studentLevel.equals(adapter.getCurrentLevelStringRepresentation().trim()));
		studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_OTHER_SECOND_SHOT, studentLevel, 1));

		adapter.doAIShot();
		assertFalse(studentLevel.equals(adapter.getCurrentLevelStringRepresentation().trim()));
		studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_OTHER_SECOND_SHOT, studentLevel, 2));

	}
	
	@Test
	public void fourShotsDoubleSelection() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL_THREE);
		assertTrue(adapter.isValidLevel());

		assertTrue(adapter.selectCell(11, 1));
		assertEquals(LEVEL_THREE_ONE, adapter.getCurrentLevelStringRepresentation().trim());
	
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_THREE_ONE, studentLevel, 1));

		assertFalse("This cell is already hit, selecting it is now illegal.", adapter.selectCell(11, 1));
		studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_THREE_ONE, studentLevel, 1));

		assertTrue(adapter.selectCell(11, 2));
		studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_THREE_TWO, studentLevel, 1));

		adapter.doAIShot();
		studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_THREE_TWO, studentLevel, 2));
	}

	@Test
	public void twoShotsEachWithBorderBetween() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL_THREE);
		assertTrue(adapter.isValidLevel());

		//this 'click' hits the border: 
		assertFalse(adapter.selectCell(10, 0));
		assertEquals(CORRECT_LEVEL_THREE, adapter.getCurrentLevelStringRepresentation().trim());
		
		assertTrue(adapter.selectCell(11, 1));
		assertEquals(LEVEL_THREE_ONE, adapter.getCurrentLevelStringRepresentation().trim());
	
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_THREE_ONE, studentLevel, 1));
	}

	@Test
	public void twoShotsEachWithClickWithoutTurnBetween() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL_FOUR);
		assertTrue(adapter.isValidLevel());

		assertTrue(adapter.selectCell(20, 9));
		assertEquals(LEVEL_FOUR_ONE, adapter.getCurrentLevelStringRepresentation().trim());

		//its computers turn, the player should not click 
		assertFalse(adapter.selectCell(12, 0));
		assertEquals(LEVEL_FOUR_ONE, adapter.getCurrentLevelStringRepresentation().trim());
		
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_FOUR_ONE, studentLevel, 1));
	}
	
	@Test
	public void twoShotsEachWithWrongClickOnOwnHalf() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL_FIVE);
		assertTrue(adapter.isValidLevel());

		//thats the players half, the player have to select a cell of the other side: 
		assertFalse(adapter.selectCell(9, 9));
		assertEquals(CORRECT_LEVEL_FIVE, adapter.getCurrentLevelStringRepresentation().trim());

		assertTrue(adapter.selectCell(11, 9));
		assertEquals(LEVEL_FIVE_ONE, adapter.getCurrentLevelStringRepresentation().trim());
		
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_FIVE_ONE, studentLevel, 1));
	}

	@Test
	public void twoShotsEachWithWrongClicks() {
		adapter.createGameUsingLevelString(CORRECT_LEVEL_SIX);
		assertTrue(adapter.isValidLevel());

		//thats not on the board: 
		assertFalse(adapter.selectCell(20, 20));
		assertEquals(CORRECT_LEVEL_SIX, adapter.getCurrentLevelStringRepresentation().trim());
		//thats not on the board: 
		assertFalse(adapter.selectCell(0, -1));
		assertEquals(CORRECT_LEVEL_SIX, adapter.getCurrentLevelStringRepresentation().trim());
		//thats not on the board: 
		assertFalse(adapter.selectCell(21, 0));
		assertEquals(CORRECT_LEVEL_SIX, adapter.getCurrentLevelStringRepresentation().trim());
		//thats not on the board: 
		assertFalse(adapter.selectCell(0, 10));
		assertEquals(CORRECT_LEVEL_SIX, adapter.getCurrentLevelStringRepresentation().trim());


		//wow, thats on the board:
		assertTrue(adapter.selectCell(15, 5));
		assertEquals(LEVEL_SIX_ONE, adapter.getCurrentLevelStringRepresentation().trim());
		
		adapter.doAIShot();
		String studentLevel = adapter.getCurrentLevelStringRepresentation().trim();
		assertTrue(TestHelper.compareLevel(LEVEL_SIX_ONE, studentLevel, 1));
	}
}
