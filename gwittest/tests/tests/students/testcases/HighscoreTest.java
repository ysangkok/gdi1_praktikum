package tests.students.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import tests.adapters.BattleshipTestAdapterExtended3;

/**
 * Tests whether the highscore-entries were correctly handled and sorted. 
 */
public class HighscoreTest {
	

	private final Date date1 = new Date(1234567890000l);
	private final Date date2 = new Date(1234567880000l);
	
	
	private final String player1 = "player1";
	private final String player2 = "player2";
	private final String player3 = "player3";
	private final String player4 = "player4";
	
	BattleshipTestAdapterExtended3 adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterExtended3();
		adapter.resetHighscore();
	}
	
	
	@Test
	public void testHighscoreOneEntry() {
		assertEquals("After resetHighscore the highscore-count should be zero.", 0, adapter.getHighscoreCount());
		
		adapter.addHighscoreEntry(player1, 10, date1, 55);
		
		assertEquals("After addHighscoreEntry the highscore should contain exactly one entry.", 1, adapter.getHighscoreCount());
		assertEquals("The date representation of the sole entry is wrong", date1, adapter.getDateAtHighscorePosition(0));
		assertEquals("The name of the sole entry is wrong", player1, adapter.getPlayernameAtHighscorePosition(0));
		assertEquals("The remaining time (to target-time) of the sole entry is wrong", 10, adapter.getTimeAtHighscorePosition(0), 0);
		assertEquals("The points of the sole entry are wrong", 55, adapter.getShotsAtHighscorePosition(0), 0);
	}

	@Test
	public void testHighscoreIllegalAccess() {
		assertEquals("After resetHighscore the highscore-count should be zero.", 0, adapter.getHighscoreCount());
		adapter.addHighscoreEntry(player1, 10, date1, 55);

		for (int i = -20; i < 0; i++) {
			assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i));
			assertNull("The date of a non-existing entry should be null.", adapter.getDateAtHighscorePosition(i));
			assertEquals("The points of a non-existing entry should be -1.", -1, adapter
					.getShotsAtHighscorePosition(i), 0);
			assertEquals("The remaining time of a non-existing entry should be -1.", -1, adapter
					.getTimeAtHighscorePosition(i), 0);
		}

		assertNotNull("The name of an existing entry should not be null.", adapter.getPlayernameAtHighscorePosition(0));
		assertNotNull("The date of an existing entry should not be null.", adapter.getDateAtHighscorePosition(0));
		assertEquals("The points of the existing entry should be 55.", 55, adapter.getShotsAtHighscorePosition(0), 0);
		assertEquals("The remaining time of the existing entry should be 10.", 10, adapter
				.getTimeAtHighscorePosition(0), 0);

		for (int i = 1; i < 20; i++) {
			assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i));
			assertNull("The date of a non-existing entry should be null.", adapter.getDateAtHighscorePosition(i));
			assertEquals("The points of a non-existing entry should be -1.", -1, adapter
					.getShotsAtHighscorePosition(i), 0);
			assertEquals("The remaining time of a non-existing entry should be -1.", -1, adapter
					.getTimeAtHighscorePosition(i), 0);
		}
	}
	
	
	@Test
	public void testHighscoreMaxCount1() {
		adapter.addHighscoreEntry("playerA", 250, date1, 17);
		adapter.addHighscoreEntry("playerB", 250, date1, 17);
		adapter.addHighscoreEntry("playerC", 250, date1, 17);
		adapter.addHighscoreEntry("playerD", 250, date1, 17);
		adapter.addHighscoreEntry("playerE", 250, date1, 17);
		adapter.addHighscoreEntry("playerF", 250, date1, 17);
		adapter.addHighscoreEntry("playerG", 250, date1, 17);
		adapter.addHighscoreEntry("playerH", 250, date1, 17);
		adapter.addHighscoreEntry("playerI", 250, date1, 17);
		adapter.addHighscoreEntry("player10", 250, date1, 17);
		adapter.addHighscoreEntry("player11", 250, date1, 17);
		adapter.addHighscoreEntry("player12", 250, date1, 17);
		adapter.addHighscoreEntry("playerJ", 250, date1, 17);
		adapter.addHighscoreEntry("player0a", 250, date1, 17);
		adapter.addHighscoreEntry("player15", 250, date1, 17);
		adapter.addHighscoreEntry("player16", 250, date1, 17);
		adapter.addHighscoreEntry("player17", 250, date1, 17);
		adapter.addHighscoreEntry("player18", 250, date1, 17);
		adapter.addHighscoreEntry("player19", 250, date1, 17);
		adapter.addHighscoreEntry("player80", 250, date1, 17);
		
		assertEquals("You should save by maximum 10 entries.", 10, adapter.getHighscoreCount());
		
		int i = -4;
		
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player0a", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player10", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player11", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player12", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player15", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player16", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player17", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player18", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player19", adapter.getPlayernameAtHighscorePosition(i++));
		assertEquals("The name of an existing entry is wrong. Maybe wrongly sorted?", "player80", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
		assertNull("The name of a non-existing entry should be null.", adapter.getPlayernameAtHighscorePosition(i++));
	}

	
	@Test
	public void testHighscoreSortAfterShots() {
		adapter.addHighscoreEntry(player2, 10, date1, 56);
		adapter.addHighscoreEntry(player3, 10, date1, 58);
		adapter.addHighscoreEntry(player4, 10, date1, 57);
		adapter.addHighscoreEntry(player1, 10, date1, 55);
		
		assertEquals(4, adapter.getHighscoreCount());

		assertEquals(player1, adapter.getPlayernameAtHighscorePosition(0));
		assertEquals(55, adapter.getShotsAtHighscorePosition(0), 0);
		assertEquals(player2, adapter.getPlayernameAtHighscorePosition(1));
		assertEquals(56, adapter.getShotsAtHighscorePosition(1), 0);
		assertEquals(player4, adapter.getPlayernameAtHighscorePosition(2));
		assertEquals(57, adapter.getShotsAtHighscorePosition(2), 0);
		assertEquals(player3, adapter.getPlayernameAtHighscorePosition(3));
		assertEquals(58, adapter.getShotsAtHighscorePosition(3), 0);
		
		for (int i = 0; i <= 3; i++) {
			assertEquals(date1, adapter.getDateAtHighscorePosition(i));
			assertEquals( 10, adapter.getTimeAtHighscorePosition(i), 0);
		}
	}
	
	
	@Test
	public void testHighscoreSortAfterNeededTime() {
		adapter.addHighscoreEntry(player4, 30, date1, 55);
		adapter.addHighscoreEntry(player3, 20, date1, 55);
		adapter.addHighscoreEntry(player1, 31, date1, 55);
		adapter.addHighscoreEntry(player2, 21, date1, 55);
		
		assertEquals(4, adapter.getHighscoreCount());

		assertEquals(player3, adapter.getPlayernameAtHighscorePosition(0));
		assertEquals(55, adapter.getShotsAtHighscorePosition(0), 0);
		assertEquals(20, adapter.getTimeAtHighscorePosition(0), 0);
		
		assertEquals(player2, adapter.getPlayernameAtHighscorePosition(1));
		assertEquals(55, adapter.getShotsAtHighscorePosition(1), 0);
		assertEquals(21, adapter.getTimeAtHighscorePosition(1), 0);

		assertEquals(player4, adapter.getPlayernameAtHighscorePosition(2));
		assertEquals(55, adapter.getShotsAtHighscorePosition(2), 0);
		assertEquals(30, adapter.getTimeAtHighscorePosition(2), 0);

		assertEquals(player1, adapter.getPlayernameAtHighscorePosition(3));
		assertEquals(55, adapter.getShotsAtHighscorePosition(3), 0);
		assertEquals(31, adapter.getTimeAtHighscorePosition(3), 0);

		
		for (int i = 0; i <= 3; i++) {
			assertEquals(date1, adapter.getDateAtHighscorePosition(i));
		}
	}
	
	@Test
	public void testHighscoreSortAfterDate() {
		
		adapter.addHighscoreEntry(player2, 38, date1, 55);
		adapter.addHighscoreEntry(player1, 53, date1, 55);
		adapter.addHighscoreEntry(player4, 38, date2, 55);
		adapter.addHighscoreEntry(player3, 52, date1, 55);
		
		assertEquals(4, adapter.getHighscoreCount());

		assertEquals(player4, adapter.getPlayernameAtHighscorePosition(0));
		assertEquals(55, adapter.getShotsAtHighscorePosition(0), 0);
		assertEquals(38, adapter.getTimeAtHighscorePosition(0), 0);
		assertEquals(date2, adapter.getDateAtHighscorePosition(0));
		
		
		assertEquals(player2, adapter.getPlayernameAtHighscorePosition(1));
		assertEquals(55, adapter.getShotsAtHighscorePosition(1), 0);
		assertEquals(38, adapter.getTimeAtHighscorePosition(1), 0);

		assertEquals(player3, adapter.getPlayernameAtHighscorePosition(2));
		assertEquals(55, adapter.getShotsAtHighscorePosition(2), 0);
		assertEquals(52, adapter.getTimeAtHighscorePosition(2), 0);

		assertEquals(player1, adapter.getPlayernameAtHighscorePosition(3));
		assertEquals(55, adapter.getShotsAtHighscorePosition(3), 0);
		assertEquals(53, adapter.getTimeAtHighscorePosition(3), 0);

		
		for (int i = 1; i <= 3; i++) {
			assertEquals(date1, adapter.getDateAtHighscorePosition(i));
		}
	}
	
	
	@Test
	public void testHighscoreSortAfterName() {
		
		adapter.addHighscoreEntry(player2, 100, date1, 49);
		adapter.addHighscoreEntry(player1, 100, date1, 49);
		
		adapter.addHighscoreEntry(player3, 99, date2, 49);
		adapter.addHighscoreEntry(player4, 99, date2, 49);
		
		assertEquals(4, adapter.getHighscoreCount());

		assertEquals(player3, adapter.getPlayernameAtHighscorePosition(0));
		assertEquals(49, adapter.getShotsAtHighscorePosition(0), 0);
		assertEquals(99, adapter.getTimeAtHighscorePosition(0), 0);
		assertEquals(date2, adapter.getDateAtHighscorePosition(0));
		
		assertEquals(player4, adapter.getPlayernameAtHighscorePosition(1));
		assertEquals(49, adapter.getShotsAtHighscorePosition(1), 0);
		assertEquals(99, adapter.getTimeAtHighscorePosition(1), 0);
		assertEquals(date2, adapter.getDateAtHighscorePosition(1));

		assertEquals(player1, adapter.getPlayernameAtHighscorePosition(2));
		assertEquals(49, adapter.getShotsAtHighscorePosition(2), 0);
		assertEquals(100, adapter.getTimeAtHighscorePosition(2), 0);
		assertEquals(date1, adapter.getDateAtHighscorePosition(2));
		
		assertEquals(player2, adapter.getPlayernameAtHighscorePosition(3));
		assertEquals(49, adapter.getShotsAtHighscorePosition(3), 0);
		assertEquals(100, adapter.getTimeAtHighscorePosition(3), 0);
		assertEquals(date1, adapter.getDateAtHighscorePosition(3));
	}
	
	
	
}
