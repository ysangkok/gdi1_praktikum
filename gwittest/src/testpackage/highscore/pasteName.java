package testpackage.highscore;

import java.awt.Frame;
import javax.swing.JOptionPane;

public class pasteName {

	/**
	 * @param args
	 *
	 */
	Object[] options = {"ok", "cancel"} ;
	
	
	public static void main(String[] args) {
		Frame frame = new Frame("dialog");
		String result = JOptionPane.showInputDialog(frame, null,"Enter your name");
		System.out.println(result);
	    

	}
	
}


