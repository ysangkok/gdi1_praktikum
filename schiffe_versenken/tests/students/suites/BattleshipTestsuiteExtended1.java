package students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import students.testcases.GeneratorTest;

public class BattleshipTestsuiteExtended1 {

	public static Test suite() {
		TestSuite suite = new TestSuite("Student tests for Battleship - Extended 1");
		//$JUnit-BEGIN$
		
		suite.addTest(new JUnit4TestAdapter(GeneratorTest.class));
		//$JUnit-END$
		return suite;
	}

}
