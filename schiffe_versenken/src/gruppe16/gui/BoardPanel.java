package gruppe16.gui;

import gruppe16.AI;
import gruppe16.Engine;
import gruppe16.exceptions.InvalidInstruction;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	BoardPanel otherBoard;
	
	public BoardPanel getOtherBoard() {
		return otherBoard;
	}

	public void setOtherBoard(BoardPanel otherBoard) {
		this.otherBoard = otherBoard;
	}

	Engine engine;

	JButton[][] buttons;
	
	JFrame parentWindow;
	
	int player;
	
	AI ai;

	public BoardPanel(JFrame theParentWindow, Engine engine, int player, AI ai) {
		super();
		parentWindow = theParentWindow;
		this.engine = engine;
		this.ai = ai;
		this.player = player;

		setLayout(new GridLayout(engine.getxWidth(),engine.getyWidth()));
		
		entities = new Vector<JButton>();
		images = new HashMap<String, ImageIcon>();

		for (String icon : icons) {
			try {
				registerImage(icon, "./template/resources/images/defaultskin/" + icon + ".png");
			} catch (RuntimeException e){
				System.err.println(e.getMessage());
			}
		}
			
		addButtons();
	}

	private void addButtons() {
		buttons = new JButton[engine.getxWidth()][engine.getyWidth()];

		Character[][] b = null;
		
		if (player == 0) b = engine.getPlayerArray();
		if (player == 1) b = engine.getVisibleOpponentArray();
		for (int i=0; i<engine.getxWidth(); i++) {
			for (int j=0; j<engine.getyWidth(); j++) {
				String iconname = fieldToIcon(b[i][j]);
				buttons[i][j] = placeEntity(iconname);
				//if (j >= 6) return;
			}
		}

	}
	
	private void removeButtons() {
		for (int i = 0; i < entities.size(); i++) {
			JButton btn = entities.get(i);
			btn.setVisible(false);
			remove(btn);
			synchronized (entities)
			{
				entities.remove(btn);
			}
			removeButtons();
			return;
		}
	}

	private static String[] icons = { "border", "fogofwar", "ship_bottom",
		"ship_hit_bottom", "ship_hit_horizontal", "ship_hit_left",
		"ship_hit", "ship_hit_right", "ship_hit_top",
		"ship_hit_vertical", "ship_horizontal", "ship_left",
		"ship_right", "ship_top", "ship_vertical", "water_hit",
	"water" };

	private static String fieldToIcon(Character c) {
		Map<Character, String> m = new HashMap<Character, String>();
		for (String n : icons) {
			if (n == "fogofwar") m.put('#',n);
			if (n == "ship_bottom") m.put('b',n);
			if (n == "ship_hit_bottom") m.put('B',n);
			if (n == "ship_hit_horizontal") m.put('H',n);
			if (n == "ship_hit_left") m.put('L',n);
			if (n == "ship_hit_right") m.put('R',n);
			if (n == "ship_hit_top") m.put( 'T', n);
			if (n == "ship_hit_vertical") m.put('V', n);
			if (n == "ship_horizontal") m.put('h', n);
			if (n == "ship_left") m.put('l', n);
			if (n == "ship_right") m.put('r', n);
			if (n == "ship_top") m.put('t', n);
			if (n == "ship_vertical") m.put('v', n);
			if (n == "water_hit") m.put('*', n);
			if (n == "water") m.put('-', n);
		}
		return m.get(c);
	}

	private HashMap<String, ImageIcon> images = null;
	
	boolean registerImage(String identifier, String fileName) {
		try
		{
			File f = new File (fileName);

			StringBuilder builder = new StringBuilder ();
			builder.append("file://");
			builder.append(f.getCanonicalPath ());
			return registerImage(identifier, new URL (builder.toString ()));
		}
		catch (MalformedURLException ex)
		{
			throw new RuntimeException("Malformed URL");
		}
		catch (IOException ex)
		{
			throw new RuntimeException("IO Exception: " + ex.getMessage());
		}
	}

	boolean registerImage(String identifier, URL fileName) {
		return images.put(identifier, new ImageIcon(fileName)) != null;
	}


	JButton placeEntity(String imageIdentifier) {
		return placeEntity(images.get(imageIdentifier)	);
	}
	JButton placeEntity(Image image){
		return placeEntity(new ImageIcon(image));
	}
	
	Vector<JButton> entities;
	
	JButton placeEntity(Icon icon){
		JButton btn = new JButton();

		btn.setMargin(
				new Insets(
						0 , 0 , 0 , 0
				)
		);

		synchronized(entities){
			entities.add(btn);
		}

		//btn.addKeyListener(parentWindow);
		btn.addMouseListener(this);
		btn.setIcon(icon);

		add(btn);
		btn.requestFocus();

		return( btn );
	}

	@Override
	public void mouseClicked(MouseEvent evt) {

		if (player == 0) { evt.consume(); return; } // you can't shoot your own map
		
		JButton refBtn = entities.get(0);
		int posX = -1 , posY = -1;
		
		for (JButton btn : entities) {
			if (evt.getSource() == btn) {
				posX = evt.getXOnScreen();
				posX = posX - (int) this.getLocationOnScreen().getX();

				posY = evt.getYOnScreen();
				posY = posY - (int) this.getLocationOnScreen().getY();
				
				posX /= refBtn.getWidth();
				posY /= refBtn.getHeight();
				
				evt.consume();
				break;
			}
		}
		
		System.out.println(String.format("%d,%d", posX, posY));
		bomb (posX, posY);
	}
	
	private void bomb(int x, int y) {
		try {
			engine.attack(Engine.otherPlayer(player), y, x);
			
			int i = 0;
			while (!engine.getState().isPlayerTurn()) {
				System.out.println(i++ + " AI plays as " + Engine.otherPlayer(player));
				ai.playAs(player);
			}
			otherBoard.refresh();
		} catch (InvalidInstruction e) {
			e.printStackTrace();
		}
		refresh();
	}
	
	private void refresh() {
		removeButtons();
		addButtons();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}

	
}
