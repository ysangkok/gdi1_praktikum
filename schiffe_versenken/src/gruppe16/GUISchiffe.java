package gruppe16;

//import java.awt.event.KeyListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Random;

import gruppe16.exceptions.InvalidLevelException;
import gruppe16.gui.BoardPanel;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

class actions {
	enum actionnames {
		quicknewgame, save, load
	}
	
	public static String quicknewgame = actionnames.quicknewgame.name();
	public static String save = actionnames.save.name();
	public static String load = actionnames.load.name();
}

public class GUISchiffe implements ActionListener {
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
	
	public void actionPerformed(ActionEvent actionEvent) {
		//System.err.println(actionEvent);
		String a = actionEvent.getActionCommand();
		if (a.equals(actions.quicknewgame)) {
			quickNewGame();
		} else if (a.equals(actions.save)) {
			save();
		} else if (a.equals(actions.load)) {
			load();
		} else {
			userError("Unknown action!");
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
		ActionListener menuListener = this;

		// File Menu, F - Mnemonic
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);

	    JMenuItem quickNewItem = new JMenuItem("Quick New", KeyEvent.VK_N);
	    quickNewItem.addActionListener(menuListener);
	    quickNewItem.setAccelerator(KeyStroke.getKeyStroke("N"));
	    quickNewItem.setActionCommand(actions.quicknewgame);
	    fileMenu.add(quickNewItem);
	    
	    JMenuItem saveItem = new JMenuItem("Save");
	    saveItem.addActionListener(menuListener);
	    saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
	    saveItem.setActionCommand(actions.save);
	    fileMenu.add(saveItem);
	    
	    JMenuItem loadItem = new JMenuItem("Load");
	    loadItem.addActionListener(menuListener);
	    loadItem.setAccelerator(KeyStroke.getKeyStroke("control L"));
	    loadItem.setActionCommand(actions.load);
	    fileMenu.add(loadItem);

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
	
	private void save() {
		String saveGameName;
		
	    JFileChooser chooser = new JFileChooser();

	    int returnVal = chooser.showSaveDialog(frame);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	saveGameName = chooser.getSelectedFile().getPath();
	    } else {
	    	return;
	    }
		
		try {
			FileOutputStream fileOut = new FileOutputStream(saveGameName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(engine.getState());
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	private void load() {
		String saveGameName;
		
	    JFileChooser chooser = new JFileChooser();

	    int returnVal = chooser.showOpenDialog(frame);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	saveGameName = chooser.getSelectedFile().getPath();
	    } else {
	    	return;
	    }
		
		State e = null;
		try {
			FileInputStream fileIn = new FileInputStream(saveGameName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (State) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			userError(i.getMessage());
			return;
		} catch (ClassNotFoundException c) {
			userError("State class not found");
			c.printStackTrace();
			return;
		}
		frame.dispose();
		engine.setState(e);
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
