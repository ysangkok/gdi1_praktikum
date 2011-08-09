package testpackage.interfaces;

import testpackage.shared.ship.Engine;
import testpackage.shared.ship.Ship;
import testpackage.shared.ship.Level;
import testpackage.shared.ship.gui.TemplateImages;
import testpackage.shared.ship.exceptions.InvalidLevelException;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BoardPanel extends JPanel implements MouseListener {
	private class SelectedTriple {
		int x;
		int y;
		JButton button;
	}
	
	private static final long serialVersionUID = 1L;
	private BoardPanel otherBoard;
	private Engine engine;
	private JButton[][] buttons;
	private BoardUser app;
	private int player;
	private boolean dontfog;
	private Vector<JButton> entities;
	SelectedTriple currentlyselected;
	private Border standardBorder = BorderFactory.createLineBorder(Color.black);
	private Border fancyBorder = BorderFactory.createLineBorder(Color.WHITE);
	
	public BoardPanel getOtherBoard() {
		return otherBoard;
	}

	public void setOtherBoard(BoardPanel otherBoard) {
		this.otherBoard = otherBoard;
	}

	public BoardPanel(BoardUser app, Engine engine, int player, boolean dontfog) {
		super();
		this.app = app;
		this.engine = engine;
		this.player = player;
		this.dontfog = dontfog;

		setLayout(new GridLayout(engine.getxWidth(),engine.getyWidth()));
		
		entities = new Vector<JButton>();
		allskins = new HashMap<String, Map<String, ImageIcon>>();

		for (String skinName : TemplateImages.allSkins)
		for (String icon : TemplateImages.icons) {
			String path = TemplateImages.imagesdir + skinName + "/" + icon + ".png";
			URL url = this.getClass().getResource(path);
			if (url == null) { throw new RuntimeException(path + " invalid"); }
			try {
				registerImage(skinName, icon, url);
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
		if (player == 1) {
			if (dontfog) {
				b = engine.getOpponentArrayWithoutFog();
			} else {
				b = engine.getVisibleOpponentArray();
			}
		}
		
		List<Ship> ships = null;
		
		if (player == 1)
			try {
				ships = Level.getShips(engine.getOpponentArrayWithoutFog());
			} catch (InvalidLevelException e) {
				throw new RuntimeException(e);
			}
		
		for (int i=0; i<engine.getxWidth(); i++) {
			for (int j=0; j<engine.getyWidth(); j++) {
				char c = b[i][j];
				String iconname;
				
				if (Level.isShip(c) && player == 1 && !Level.getShipAt(ships, i, j).isAllShotUp(engine.getOpponentArrayWithoutFog())) {
					iconname = "ship_hit";
				} else {
					iconname = TemplateImages.fieldToIcon(c);
				}
				buttons[i][j] = placeEntity(iconname);
			}
		}
		if (currentlyselected != null)
			addSelection(currentlyselected.x, currentlyselected.y);

	}
	
	private void removeButtons() {
			if (entities.size() == 0) return;
			JButton btn = entities.get(0);

			btn.setVisible(false);
			remove(btn);
			synchronized (entities)
			{
				entities.remove(btn);
			}
			removeButtons();
			return;
	}

	private HashMap<String, Map<String, ImageIcon>> allskins = null;

	private void registerImage(String skinName, String identifier, URL url) {
		Map skinMap = allskins.get(skinName);
		if (skinMap==null) { skinMap = new HashMap<String, ImageIcon>(); allskins.put(skinName, skinMap); }
		skinMap.put(identifier, new ImageIcon(url));
		
	}


	private JButton placeEntity(String imageIdentifier) {
		return placeEntity(allskins.get(app.getSelectedSkin()).get(imageIdentifier)	);
	}
	

	public void removeSelection() {
		if (currentlyselected != null) {
			currentlyselected.button.setBorder(standardBorder);
			currentlyselected = null;
		}
	}
	
	public void addSelection(int x, int y) {
		currentlyselected = new SelectedTriple();
		currentlyselected.x = x;
		currentlyselected.y = y;
		currentlyselected.button = buttons[x][y];
		currentlyselected.button.setBorder(fancyBorder);
	}
	
	private JButton placeEntity(Icon icon){
		JButton btn = new JButton();
		btn.setBorder(standardBorder);

		btn.setMargin(
				new Insets(
						0 , 0 , 0 , 0
				)
		);

		synchronized(entities){
			entities.add(btn);
		}

		if (app instanceof KeyListener) {
			btn.addKeyListener((KeyListener) app);
		}
		btn.addMouseListener(this);
		btn.setIcon(icon);

		add(btn);
		btn.requestFocus();

		return( btn );
	}

	@Override
	public void mousePressed(MouseEvent evt) {

		//if (player == 0) { evt.consume(); return; } // you can't shoot your own map
		
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
		
		//System.out.println(String.format("%d,%d", posX, posY));
		app.bomb (player, posX, posY);
	}
	
	public void refresh() {
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
	public void mouseClicked(MouseEvent arg0) {

		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}

	
}
