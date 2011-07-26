package tests.students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import tests.adapters.BattleshipTestAdapterExtended1;

public class GeneratorTest {

	BattleshipTestAdapterExtended1 adapter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterExtended1();
	}
	
	@Test
	public void testGeneration() {
		adapter.generateCompleteLevel();
		assertTrue("You should be able to generate a valid level.", adapter.isValidLevel());
		assertFalse("A generated level should never be finished in the first place.", adapter.isFinished());
		assertFalse("A generated level should never be finished in the first place.", adapter.isWon());
		assertFalse("A generated level should never be finished in the first place.", adapter.isLost());
	}
	
	@Test
	public void testSize() {
		for (int i = 0; i < 99; i++) {
			adapter.generateCompleteLevel();
			String levelString = adapter.getCurrentLevelStringRepresentation();

			adapter.createGameUsingLevelString(levelString);
			assertTrue("You should be able to generate a valid level.", adapter.isValidLevel());
			assertFalse("A generated level should never be finished in the first place.", adapter.isFinished());
			assertFalse("A generated level should never be finished in the first place.", adapter.isWon());
			assertFalse("A generated level should never be finished in the first place.", adapter.isLost());

			
			String[] levelRows = levelString.trim().split("\n");
			assertEquals("All generated levels should have the default board size.", 10, levelRows.length);

			for (String row : levelRows) {
				assertEquals("All generated levels should have the default board size.", 21, row.length());
				assertTrue("Each row should contain a border element.", row.contains("|"));
				assertTrue("Each row should contain only one border element.", row.indexOf('|') == row.lastIndexOf('|'));
			}
		}
	}
}
