package gruppe16;

//import java.awt.event.KeyListener;

import gruppe16.gui.BoardPanel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUISchiffe /*implements KeyListener*/ {
	public static void main(String[] args) {
		Engine engine = new Engine();
		System.out.println(engine.getLevelStringForPlayer(0));
		
		AI ai = new GoodAI(engine);
		
		JFrame frame = new JFrame("GUISchiffe");
		JPanel panel = new JPanel();
		BoardPanel panel1 = null;
		BoardPanel panel2 = null;
		panel1 = new BoardPanel(frame, engine, 0, ai);
		panel.add(panel1);
		panel2 = new BoardPanel(frame, engine, 1, ai);
		panel.add(panel2);
		
		panel1.setOtherBoard(panel2);
		panel2.setOtherBoard(panel1);
		
		frame.getContentPane().add(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
