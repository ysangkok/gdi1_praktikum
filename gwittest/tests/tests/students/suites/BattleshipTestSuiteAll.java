package tests.students.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BattleshipTestSuiteAll {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("All student tests for Battleship");
		//$JUnit-BEGIN$
		
		//Tutor suites include the student suites
		suite.addTest(tests.students.suites.BattleshipTestsuiteMinimal.suite());
		suite.addTest(tests.students.suites.BattleshipTestsuiteExtended1.suite());
		suite.addTest(tests.students.suites.BattleshipTestsuiteExtended2.suite());
		suite.addTest(tests.students.suites.BattleshipTestsuiteExtended3.suite());
		//$JUnit-END$
		return suite;
	}

}
