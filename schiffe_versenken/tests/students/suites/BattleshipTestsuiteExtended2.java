package students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import students.testcases.ShotsPerShipTest;

public class BattleshipTestsuiteExtended2 {

	public static Test suite() {
		TestSuite suite = new TestSuite("Student tests for Battleship - Extended 2");
		//$JUnit-BEGIN$

		suite.addTest(new JUnit4TestAdapter(ShotsPerShipTest.class));
		//$JUnit-END$
		return suite;
	}

}
