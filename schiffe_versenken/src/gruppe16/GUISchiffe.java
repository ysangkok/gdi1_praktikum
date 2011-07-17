package gruppe16;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gruppe16.exceptions.InvalidInstruction;
import gruppe16.exceptions.InvalidLevelException;
import gruppe16.gui.BoardPanel;
import gruppe16.gui.BoardUser;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

/**
 * class implementing Swing UI
 */
public class GUISchiffe implements ActionListener, BoardUser {
	private JFrame frame;
	private Engine engine;
	private AI ai;
	private BoardPanel[] panels;
	
	private class WizardCoordReceiver implements ActionListener, BoardUser {
			private JDialog dialog;
			private Engine engine;
			private placeOwnShipsDialog wizard;
			private int shiplength;
			private JRadioButton rbutton1;
			private JRadioButton rbutton2;
			
			public WizardCoordReceiver(placeOwnShipsDialog app) {
				engine = app.engine;
				this.wizard = app;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton but = (JButton) e.getSource();
				//System.out.println("action: " + e.getActionCommand());
				shiplength = wizard.buttolen.get(but);
				
				//Map2DHelper<Object> helper = new Map2DHelper<Object>();
				 
				//System.out.println(helper.getBoardString(engine.getPlayerArray()));
				
				ButtonGroup group = new ButtonGroup();
				rbutton1 = new JRadioButton("Horizontal", true);
				rbutton2 = new JRadioButton("Vertical", false);
			    group.add(rbutton1);
			    group.add(rbutton2);
			    
				BoardPanel bpanel = new BoardPanel(this, engine, 0, true);
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
				dialog = new JDialog(wizard, "Click a position", Dialog.ModalityType.DOCUMENT_MODAL);
				panel.add(rbutton1);
				panel.add(rbutton2);
				panel.add(bpanel);
				dialog.setContentPane(panel);
				dialog.pack();
				dialog.setResizable(false);
				dialog.setVisible(true);
				
				try {
					engine.setState(new State(new LevelGenerator(engine.getxWidth(), engine.getyWidth(), 0, wizard.getChosencoords()).getLevel(false)));
					
					//Map2DHelper<Object> helper = new Map2DHelper<Object>();
					 
					//System.out.println(helper.getBoardString(engine.getPlayerArray()));
				} catch (InvalidLevelException e1) {
					JOptionPane.showMessageDialog(dialog,
							e1.getMessage(),
						    "Error", 0);
					wizard.chosencoords.remove(wizard.chosencoords.size()-1);
					return;
				}

				//System.out.println("action performed: " + shiplength);
				
				but.setEnabled(false);
				
			}

			@Override
			public void bomb(int p, int x, int y) {
				//System.err.println("bomb: " + shiplength + ", x=" + x + ", y=" + y);
				wizard.chose(y, x, shiplength, (rbutton1.getSelectedObjects() != null ? Orientation.HORIZONTAL : Orientation.VERTICAL));
				dialog.dispose();
			}
	}
	
	/**
	 * call this when game is over to notify user of winning player and shut down
	 */
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
	
	private GUISchiffe() {
		engine = new Engine();
		ai = new BadAI(engine);
		
		showFrame();
	}
	
	private class placeOwnShipsDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		public List<Ship> getChosencoords() {
			return chosencoords;
		}
		private List<Ship> chosencoords;
		private Map<JButton, Integer> buttolen; 
		public Engine engine;
		private int shipcount = 0;
		JButton ok;
		public boolean finished = false;
		private void build(Container cp) {
			chosencoords = new ArrayList<Ship>();
			buttolen = new HashMap<JButton, Integer>();
			
			for (int i = 0; i < Rules.ships.length; i++) {
				int[] shiprules = Rules.ships[i];
				for (int j = 0; j < shiprules[1]; j++) {
					shipcount++;
					JPanel g = new JPanel();
					
					JLabel lab = new JLabel(String.format("%d-ship: ", shiprules[0]));
					JButton but = new JButton("Place");
					
					buttolen.put(but,shiprules[0]);
					g.add(lab);
					g.add(but);
					cp.add(g);
					but.addActionListener(new WizardCoordReceiver(this));
				}
			}
		}
		public void chose(int x, int y, int len, Orientation o) {
			chosencoords.add(new Ship(x, y, len, o));
			if (chosencoords.size() == shipcount) ok.setEnabled(true);
		}
		public placeOwnShipsDialog(JFrame parent) {
			super(parent, "Place your ships", true);
			engine = new Engine();
			engine.getState().getLevel().clearPlayerBoard();
			
			Container cp = getContentPane();
			cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
			JPanel placers = new JPanel();
			placers.setLayout(new BoxLayout(placers, BoxLayout.PAGE_AXIS));
			build(cp);
			cp.add(placers);

			JPanel bottompanel = new JPanel();
			ok = new JButton("OK");
			ok.setEnabled(false);
			ok.addActionListener(this);
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(this);
			
			bottompanel.add(ok);
			bottompanel.add(cancel);
			cp.add(bottompanel);
			setSize(250, 500);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "OK") {
				finished = true;
				/*for (Ship s : chosencoords) {
					System.out.println(s.toString());
				}*/
			} else if (e.getActionCommand() == "Cancel") {

			}
			dispose();
		}
	}
	
	private void placeOwnShipsWizard() {
		placeOwnShipsDialog shipchooser = new placeOwnShipsDialog(frame);

		shipchooser.setVisible(true); // blocks

		if (shipchooser.finished ) {
			engine = shipchooser.engine;
			ai = new BadAI(engine);
			
			frame.dispose();
			showFrame();
		}
	}

	private static class actions {
		static enum actionnames {
			quicknewgame, save, load, newplaceownships, newgenerated
		}
		
		public static String quicknewgame = actionnames.quicknewgame.name();
		public static String save = actionnames.save.name();
		public static String load = actionnames.load.name();
		public static String newplaceownships = actionnames.newplaceownships.name();
		public static String newgenerated = actionnames.newgenerated.name();
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
		} else if (a.equals(actions.newplaceownships)) {
			placeOwnShipsWizard();
		} else if (a.equals(actions.newgenerated)) {
			generatedNewGame();
		} else {
			userError("Unknown action!");
		}
	}
	
	private void showFrame() {	
		frame = new JFrame("GUISchiffe");
		JPanel panel = new JPanel();
		
		BoardPanel panel1 = new BoardPanel(this, engine, 0, false);
		panel.add(panel1);
		BoardPanel panel2 = new BoardPanel(this, engine, 1, false);
		panels = new BoardPanel[] {panel1, panel2};
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

	    JMenu newMenu = new JMenu("New");
	    fileMenu.add(newMenu);
	    
	    JMenuItem quickNewItem = new JMenuItem("Quick New (load random built-in level)", KeyEvent.VK_N);
	    quickNewItem.addActionListener(menuListener);
	    quickNewItem.setAccelerator(KeyStroke.getKeyStroke("N"));
	    quickNewItem.setActionCommand(actions.quicknewgame);
	    newMenu.add(quickNewItem);
	    
	    JMenuItem place = new JMenuItem("Place own ships...", KeyEvent.VK_P);
	    place.addActionListener(menuListener);
	    place.setActionCommand(actions.newplaceownships);
	    newMenu.add(place);
	    
	    JMenuItem generate = new JMenuItem("New Game (using generated level)", KeyEvent.VK_G);
	    generate.addActionListener(menuListener);
	    generate.setActionCommand(actions.newgenerated);
	    newMenu.add(generate);
	    
	    JMenuItem saveItem = new JMenuItem("Save as...", KeyEvent.VK_S);
	    saveItem.addActionListener(menuListener);
	    saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
	    saveItem.setActionCommand(actions.save);
	    fileMenu.add(saveItem);
	    
	    JMenuItem loadItem = new JMenuItem("Load from...", KeyEvent.VK_L);
	    loadItem.addActionListener(menuListener);
	    loadItem.setAccelerator(KeyStroke.getKeyStroke("control L"));
	    loadItem.setActionCommand(actions.load);
	    fileMenu.add(loadItem);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * entry point
	 * @param args not used
	 */
	public static void main(String[] args) {
		new GUISchiffe();
	}

	private void userError(String message) {
		JOptionPane.showMessageDialog(frame,
			    message, 
			    "Error", 0);
	}
	
	private void quickNewGame() {
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
		ai = new BadAI(engine);
		
		frame.dispose();
		showFrame();
	}
	
	private void generatedNewGame() {
		engine = new Engine();
		ai = new BadAI(engine);
		
		frame.dispose();
		showFrame();
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
		engine.setState(e);
		
		frame.dispose();
		showFrame();
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

	@Override
	public void bomb(int player, int x, int y) {
			if (player == 0) return;
			
			try {
				engine.attack(Engine.otherPlayer(player), y, x);
			} catch (InvalidInstruction e) {
				userError(e.getMessage());
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
				
			if (engine.isFinished()) { GameOver(); return; }

			int i = 0;
			while (!engine.getState().isPlayerTurn()) {
				System.out.println(i++ + " AI plays as " + player);
				ai.playAs(player);
			}
			panels[Engine.otherPlayer(player)].refresh();

			if (engine.isFinished()) { GameOver(); return; }
			
			panels[player].refresh();
	}
}
