package tests.students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.students.testcases.BasicGameTest;
import tests.students.testcases.LoadLevelBigTest;
import tests.students.testcases.LoadlevelTest;
import tests.students.testcases.WonLostTest;

public class BattleshipTestsuiteMinimal {

	public static Test suite() {
		TestSuite suite = new TestSuite("Student tests for Battleship - Minimal");
		//$JUnit-BEGIN$
		suite.addTest(new JUnit4TestAdapter(LoadlevelTest.class));
		suite.addTest(new JUnit4TestAdapter(BasicGameTest.class));
		suite.addTest(new JUnit4TestAdapter(WonLostTest.class));
		suite.addTest(new JUnit4TestAdapter(LoadLevelBigTest.class));
		//$JUnit-END$
		return suite;
	}

}
