package students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import students.testcases.BasicGameTest;
import students.testcases.LoadLevelBigTest;
import students.testcases.LoadlevelTest;
import students.testcases.WonLostTest;

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
