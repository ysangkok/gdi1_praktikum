package gruppe16;

//import java.awt.event.KeyListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import gruppe16.exceptions.InvalidLevelException;
import gruppe16.gui.BoardPanel;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

class actions {
	public static String quicknewgame = "0";
}

public class GUISchiffe {
	JFrame frame;
	Engine engine;
	
	public void GameOver() {
		int winner = engine.checkWin();
		
		String winnerstr;
		
		if (winner == -1) {
			winnerstr = "Draw";
		} else {
			winnerstr = "Player " + (winner+1) + " won!";
		}
		
		JOptionPane.showMessageDialog(frame,
			    "Game over. " + winnerstr, 
			    "Game over", 0);
		frame.dispose();
	}
	
	GUISchiffe() {
		engine = new Engine();
		//System.out.println(engine.getLevelStringForPlayer(0));
		
		initAIandShowFrame();
	}
	
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			System.err.println(actionEvent);
			if (actionEvent.getActionCommand().equals(actions.quicknewgame)) {
				GUISchiffe.this.quickNewGame();
			}
		}
	}
	
	void initAIandShowFrame() {
		AI ai = new BadAI(engine);
		
		frame = new JFrame("GUISchiffe");
		JPanel panel = new JPanel();
		BoardPanel panel1 = null;
		BoardPanel panel2 = null;
		panel1 = new BoardPanel(this, engine, 0, ai);
		panel.add(panel1);
		panel2 = new BoardPanel(this, engine, 1, ai);
		panel.add(panel2);
		
		panel1.setOtherBoard(panel2);
		panel2.setOtherBoard(panel1);
		
		frame.getContentPane().add(panel);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		ActionListener menuListener = new MenuActionListener();

		// File Menu, F - Mnemonic
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);

	    // File->New, N - Mnemonic
	    JMenuItem newMenuItem = new JMenuItem("Quick New", KeyEvent.VK_N);
	    newMenuItem.addActionListener(menuListener);
	    newMenuItem.setAccelerator(KeyStroke.getKeyStroke("N"));
	    newMenuItem.setActionCommand(actions.quicknewgame);
	    fileMenu.add(newMenuItem);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new GUISchiffe();
	}

	public void userError(String message) {
		JOptionPane.showMessageDialog(frame,
			    message, 
			    "Error", 0);
	}
	
	private void quickNewGame() {
		frame.dispose();
		
		String levelDir = "./template/resources/levels/defaultlevels/";
		
		File dir = new File(levelDir);

		String[] children = dir.list();
		
		int chosenLvl = -1;
		
		for (;;) {
			Random gen = new Random();
			chosenLvl = gen.nextInt(children.length);
			String ext;
			try {
				ext = children[chosenLvl].substring(children[chosenLvl].length()-4);
			} catch (StringIndexOutOfBoundsException e) {
				ext = ".tooshort";
			}
			if (ext.equals(".lvl")) break;
			else if (!ext.equals(".svn")) userError("Unknown file in levels dir: " + children[chosenLvl]);
		}
		
		Level level;
		
		try {
			level = new Level(readFileAsString(levelDir + children[chosenLvl]));
		} catch (InvalidLevelException e) {
			userError(String.format("Template level corrupted: %s",children[chosenLvl]));
			return;
		} catch (IOException e) {
			userError("IO Error: " + e.getMessage());
			return;
		}
		
		engine = new Engine(level);
		initAIandShowFrame();
	}
	
	private static String readFileAsString(String filePath) throws IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}
}
