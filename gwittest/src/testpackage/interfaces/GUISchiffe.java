package testpackage.interfaces;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.net.URL;
import java.util.Locale;
import java.util.Random;

import javax.swing.BoxLayout;
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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.apache.commons.io.FileUtils;

import testpackage.shared.Util;
import testpackage.shared.ship.AI;
import testpackage.shared.ship.BadAI;
import testpackage.shared.ship.Engine;
import testpackage.shared.ship.Level;
import testpackage.shared.ship.State;
import testpackage.shared.ship.exceptions.InvalidInstruction;
import testpackage.shared.ship.exceptions.InvalidLevelException;
import testpackage.shared.ship.gui.TemplateImages;

import translator.TranslatableGUIElement;
import translator.Translator;

/**
 * panel handles display AND logic
 */
class CountdownTimerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
// http://leepoint.net/notes-java/examples/animation/41TextClock/25textclock.html
	
    private JTextField _timeField;  // set by timer listener
    private long endtime;
    private long timeleft;
    private GUISchiffe app;
    /**
     * swing timer used measuring time. used in constructor
     */
    javax.swing.Timer t;

    void go() {
    	t.start();
    }
    
    /**
     * @param app guischiffe app so that we can change turn and attack when timer expires
     */
    public CountdownTimerPanel(GUISchiffe app) {
    	this.app = app;
    	
        _timeField = new JTextField(5);
        _timeField.setEditable(false);
        _timeField.setFont(new Font("sansserif", Font.PLAIN, 48));

        this.setLayout(new FlowLayout());
        this.add(_timeField); 
        
        resetCountdown();
        
        //    Use full package qualification for javax.swing.Timer
        //    to avoid potential conflicts with java.util.Timer.
        t = new javax.swing.Timer(50, new ClockListener());

    }
    
    /**
     * called at the point of time the user should have 5 seconds from
     */
    void resetCountdown() {
    	endtime = System.currentTimeMillis() + 5000;
    }
    
    /**
     * used for pausing the timer when the user opens a dialog window or similar that obstructs gameplay. would be unfair if timer was still running then.
     */
    void pause() {
    	t.stop();
    	timeleft = endtime - System.currentTimeMillis();
    }
    
    /**
     * used after pausing with pause()
     */
    void resume() {
    	endtime = System.currentTimeMillis() + timeleft;
    	t.start();
    }
    
    /**
     * this actionlistener is used for handling the clock timeout every 50 milliseconds
     */
    class ClockListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {

       		long nowtime = System.currentTimeMillis();

            _timeField.setText(String.format("%.1f", Math.abs((double) (endtime-nowtime))/1000)); // abs to prevent short minus interval when timer goes below 0
 
    		if (nowtime >= endtime) {
    			if (!app.engine.getState().isPlayerTurn()) return;
    			app.engine.getState().changeTurn();
    			app.aiAttackAs(1);
    			//app.userError("Time's up!");
    			//t.stop();
    		}
    	}
    }
}

/**
 * class implementing Swing UI
 */
public class GUISchiffe implements ActionListener, BoardUser, KeyListener {
	private JFrame frame;
	/**
	 * this is the engine used in gui game. initialized multiple places
	 */
	Engine engine;
	private AI ai;
	private BoardPanel[] panels;
	private boolean speerfeuer = true;
	private CountdownTimerPanel clock;
	private final Translator translator;
	private JLabel statusLabel;
	private int[] keyboardSelected = {0,0,0}; // player 0, coord 0,0
	
	/**
	 * call this when game is over to notify user of winning player and shut down
	 */
	public void GameOver() {
		if (speerfeuer) clock.pause();
		
		int winner = engine.checkWin().playernr;
		
		String winnerstr;
		
		if (winner == -1) {
			winnerstr = "Draw.";
		} else {
			winnerstr = "Player " + (winner+1) + " won!";
		}
		
		final JDialog d = new JDialog(frame, "Game over", Dialog.ModalityType.DOCUMENT_MODAL);
		d.setSize(500, 100);
		FlowLayout layout = new FlowLayout();
		d.setLayout(layout);
		d.add(new JLabel("Game over. " + winnerstr + " Reason: " + engine.checkWin().reason));
		JButton but = new JButton();
		but.setText("Quit");
		but.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.dispose();
			}
		});
		d.add(but);
		//d.add(layout);
		d.setVisible(true);
		
		
/*		JOptionPane.showMessageDialog(frame,
			    "Game over. " + winnerstr + " Reason: " + engine.checkWin().reason, 
			    "Game over", 0);*/
		frame.dispose();
	}
	
	private void copyTranslations() {
		for (String str : new String[] {"Battleship.en_US", "Battleship.de_DE"}) {
			URL url = this.getClass().getResource(TemplateImages.translationspath + str);
			File destination = new File(str);
			
			if (url == null) { throw new RuntimeException("can't extract translation"); }
			
			try {
				FileUtils.copyURLToFile(url, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private GUISchiffe(Locale targetLocale) {
		initNewEngineAndAI();
		
		copyTranslations();
		
		translator = new Translator("Battleship", targetLocale);
		showFrame();
	}
	
	private boolean placeOwnShipsWizard() {
		placeOwnShipsDialog shipchooser = new placeOwnShipsDialog(frame);

		shipchooser.setVisible(true); // blocks. i.e. next line is only executed after ship chooser is closed.

		if (shipchooser.finished ) {
			engine = shipchooser.engine;
			ai = new BadAI(engine);
			
			frame.dispose();
		    showFrame();
			return true;
		}
		return false;
	}

	private static class actions {
		static enum actionnames {
			quicknewgame, save, load, newplaceownships, newgenerated, about, skins
		}
		
		public static String quicknewgame = actionnames.quicknewgame.name();
		public static String save = actionnames.save.name();
		public static String load = actionnames.load.name();
		public static String newplaceownships = actionnames.newplaceownships.name();
		public static String newgenerated = actionnames.newgenerated.name();
		public static String about = actionnames.about.name();
		public static String skins = actionnames.skins.name();
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		//System.err.println(actionEvent);
		String a = actionEvent.getActionCommand();
		clock.pause();
		if (a.equals(actions.quicknewgame)) { // would be nice with the Java 7 string switch
			quickNewGame();
			clock.resetCountdown();
		} else if (a.equals(actions.save)) {
			save();
		} else if (a.equals(actions.load)) {
			load();
			clock.resetCountdown();
		} else if (a.equals(actions.about)) {
			about();
		} else if (a.equals(actions.newplaceownships)) {
			if (placeOwnShipsWizard())
				clock.resetCountdown();
		} else if (a.equals(actions.newgenerated)) {
			generatedNewGame();
			clock.resetCountdown();
		} else {
			userError("Unknown action!");
		}
		clock.resume();
	}
	
	private void setStatusBarMessage(String message) {
		statusLabel.setText(message);
	}
	
	/**
	 * initialize and show main game window
	 */
	private void showFrame() {	
		TranslatableGUIElement guiBuilder = translator.getGenerator();
		
		frame = guiBuilder.generateJFrame("guiFrame");
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		frame.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel();
		if (engine.getState().shotspershipenabled) {
			setStatusBarMessage("Select a shooter");
		} else {
			setStatusBarMessage("Shots per ship not enabled, just attack");
		}
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel boardpanel = new JPanel();
		
		BoardPanel panel1 = new BoardPanel(this, engine, 0, false);
		boardpanel.add(panel1);
		BoardPanel panel2 = new BoardPanel(this, engine, 1, false);
		panels = new BoardPanel[] {panel1, panel2};
		boardpanel.add(panel2);
		
		panel.add(boardpanel);
		
		panel1.setOtherBoard(panel2);
		panel2.setOtherBoard(panel1);
		
		panels[keyboardSelected[0]].addSelection(keyboardSelected[1], keyboardSelected[2]);
		
		if (speerfeuer) {
			clock = new CountdownTimerPanel(this);
			panel.add(clock);
		}
		
		frame.getContentPane().add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		ActionListener menuListener = this;

		// File Menu
	    JMenu fileMenu = guiBuilder.generateJMenu("fileMenu");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);
	    
	    // Options
	    JMenu Options = guiBuilder.generateJMenu("Options");
	    menuBar.add(Options);
	    
	    //New
	    JMenu newMenu = guiBuilder.generateJMenu("New");
	    fileMenu.add(newMenu);
	    
	    //About
	    JMenuItem About = guiBuilder.generateJMenuItem("About");
	    About.addActionListener(menuListener);
	    About.setActionCommand(actions.about);
	    Options.add(About);
	    
	    //QuickNew
	    JMenuItem quickNewItem = guiBuilder.generateJMenuItem("QuickNew");
	    quickNewItem.addActionListener(menuListener);
	    quickNewItem.setAccelerator(KeyStroke.getKeyStroke("N"));
	    quickNewItem.setActionCommand(actions.quicknewgame);
	    newMenu.add(quickNewItem);
	    
	    //PlaceShips
	    JMenuItem place = guiBuilder.generateJMenuItem("PlaceShips");
	    place.addActionListener(menuListener);
	    place.setAccelerator(KeyStroke.getKeyStroke("G"));
	    place.setActionCommand(actions.newplaceownships);
	    newMenu.add(place);
	    
	    //DefaultNewGame
	    JMenuItem generate = guiBuilder.generateJMenuItem("DefaultNewGame");
	    generate.addActionListener(menuListener);
	    generate.setActionCommand(actions.newgenerated);
	    newMenu.add(generate);
	    
	    //SaveGame
	    JMenuItem saveItem = guiBuilder.generateJMenuItem("saveGame");
	    saveItem.addActionListener(menuListener);
	    saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
	    saveItem.setActionCommand(actions.save);
	    fileMenu.add(saveItem);
	    
	    //loadGame
	    JMenuItem loadItem = guiBuilder.generateJMenuItem("loadGame");
	    loadItem.addActionListener(menuListener);
	    loadItem.setAccelerator(KeyStroke.getKeyStroke("control L"));
	    loadItem.setActionCommand(actions.load);
	    fileMenu.add(loadItem);
	    
	    //Language Menu
	    JMenu languageMenu = guiBuilder.generateJMenu("languageMenu");
		
	    JMenuItem germanItem = guiBuilder.generateJMenuItem("German");
		germanItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				translator.setTranslatorLocale(Locale.GERMANY);
			}
		});
		JMenuItem englishItem = guiBuilder.generateJMenuItem("English");
		englishItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				translator.setTranslatorLocale(Locale.US);
			}
		});
		
	    Options.add(languageMenu);
		languageMenu.add(englishItem);
		languageMenu.add(germanItem);
		// End Language Menu
		
		

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		if (speerfeuer) {
			clock.go();
		}
	}
	
	/**
	 * entry point
	 * @param args not used
	 */
	public static void main(String[] args) {
		new GUISchiffe(Locale.GERMANY);
	}

	/**
	 * show error message to user
	 * @param message message to show
	 */
	void userError(String message) {
		JOptionPane.showMessageDialog(frame,
			    message, 
			    "Error", 0);
	}
	
	/**
	 * randomly loads level from built-in collection. this functionality is required by minimal assignment level
	 */
	private void quickNewGame() {
		String[] levels = {"01.lvl", "02.lvl", "03.lvl", "04.lvl", "05.lvl", "06.lvl", "07.lvl", "08.lvl", "09.lvl", "10.lvl", "11.lvl", "12.lvl", "13.lvl", "14.lvl", "15.lvl", "16.lvl", "17.lvl", "18.lvl", "19.lvl"};
		
		Random gen = new Random();
		String chosenLvl = levels[gen.nextInt(levels.length)];
		
		/*
		String levelDir = TemplateImages.levelspath;
		
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
		*/
		Level level;
		
		try {
			//level = new Level(readFileAsString(levelDir + children[chosenLvl]));
			level = new Level(Util.getResourceAsString(TemplateImages.levelspath + chosenLvl));
		
		} catch (InvalidLevelException e) {
			userError(String.format("Template level corrupted: %s",chosenLvl));
			return;
		}
		
		initNewEngineAndAIWithLevel(level);
		
		frame.dispose();
		showFrame();
	}

	private void initNewEngineAndAIWithLevel(Level level) {
		engine = new Engine(level);
		engine.enableShotsPerShip();
		ai = new BadAI(engine);
	}
	
	private void initNewEngineAndAI() {
		engine = new Engine();
		engine.enableShotsPerShip();
		ai = new BadAI(engine);
	}
	
	private void generatedNewGame() {
		initNewEngineAndAI();
		
		frame.dispose();
		showFrame();
	}
	
	private void skins(){
		//TODO
	}
	
	private void about(){
		JOptionPane.showMessageDialog(frame,
			    "Gruppe 16: Dimitrios, Janus, Alymbek, Yanai", 
			    "About", JOptionPane.PLAIN_MESSAGE);
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
	
	/**
	 * reads file into string. from here: http://snippets.dzone.com/posts/show/1335
	 * @param filePath file to load
	 * @return string with file contents
	 * @throws IOException
	 */
	private static String readFileAsString(String filePath) throws IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { } // doesn't matter if we get error while closing, since we're just doing it to be nice
	    }
	    return new String(buffer);
	}

	@Override
	public void bomb(int player, int x, int y) {
			if (player == 0) {
				if (engine.getState().shotspershipenabled) {
					try {
						int shots = engine.chooseFiringXY(player, y, x);
						setStatusBarMessage(String.format("Selected (%d,%d): %d shots remaining", x,y,shots));
					} catch (InvalidInstruction e) {
						userError(e.getMessage());
					}
				}
				return;
			}
			
			try {
				engine.attack(Engine.otherPlayer(player), y, x);
			} catch (InvalidInstruction e) {
				if (speerfeuer) clock.pause();
				if (e.getReason() != testpackage.shared.ship.exceptions.InvalidInstruction.Reason.NOMOREAMMO)
					userError(e.getMessage());
				else
					setStatusBarMessage(e.getMessage());
				if (speerfeuer) clock.resume();
				return;
			}
			
			if (engine.getState().shotspershipenabled && player == 1)
				try {
					setStatusBarMessage(String.format("Remaining shots: %d", engine.remainingShotsFor(0)));
				} catch (InvalidInstruction e) {
				}
				
			if (engine.isFinished()) { GameOver(); return; }

			aiAttackAs(player);
			
			panels[player].refresh();
	}
	
	/**
	 * called when 5 sec speerfeuer timer expires
	 * @param player the player number the AI plays as
	 */
	void aiAttackAs(int player) {		
		int i = 0;
		while (!engine.getState().isPlayerTurn() && !engine.isFinished()) {
			System.out.println(i++ + " AI plays as " + player); // debug output to detect runaway AI
			ai.playAs(player);
			engine.checkWin();
		}
		panels[Engine.otherPlayer(player)].refresh();
		
		if (engine.isFinished()) { GameOver(); return; }
		
		if (speerfeuer) clock.resetCountdown();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int p = keyboardSelected[0];
		int x = keyboardSelected[1];
		int y = keyboardSelected[2];
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			x--;
			break;
		case KeyEvent.VK_RIGHT:
			x++;
			break;
		case KeyEvent.VK_UP:
			y--;
			break;
		case KeyEvent.VK_DOWN:
			y++;
			break;
		case KeyEvent.VK_SPACE:
			bomb(p,x,y);
			break;
		default:
			return;
		}
		
		if (x < 0 && p == 1) {
			x = engine.getyWidth()-1;
			p = Engine.otherPlayer(p);
		}
		if (x < 0) {
			x = 0;
		}
		if (x > engine.getyWidth()-1 && p == 0) {
			x = 0;
			p = Engine.otherPlayer(p);
		}
		if (x > engine.getyWidth()-1) {
			x = engine.getyWidth()-1;
		}
		
		if (y < 0) {
			y = 0;
		}
		
		if (y > engine.getxWidth()-1) {
			y = engine.getxWidth()-1;
		}
		
		keyboardSelected = new int[] {p, x, y};
		
		//System.err.println(Arrays.toString(keyboardSelected));
		
		panels[0].removeSelection();
		panels[1].removeSelection();
		panels[p].addSelection(y, x);
	}

	@Override
	public void keyReleased(KeyEvent e) {

		
	}

	@Override
	public void keyTyped(KeyEvent e) {

		
	}
}
