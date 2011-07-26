package tests.students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.students.testcases.HighscoreTest;

public class BattleshipTestsuiteExtended3 {

	public static Test suite() {
		TestSuite suite = new TestSuite("Student tests for Battleship - Extended 3");
		//$JUnit-BEGIN$

		suite.addTest(new JUnit4TestAdapter(HighscoreTest.class));
		//$JUnit-END$
		return suite;
	}

}
