package testpackage.interfaces;

import testpackage.shared.ship.Engine;
import testpackage.shared.ship.Ship;
import testpackage.shared.ship.Level;
import testpackage.shared.ship.TemplateImages;
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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * originally from the template, but modified a lot
 */
public class BoardPanel extends JPanel implements MouseListener {
	static private class SelectedTriple {
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
	private SelectedTriple currentlyselected;
	private Border standardBorder;
	private Border cursorBorder;
	private Border selectedShooterBorder;
	private Border targetBorder; // reichweite von schiffen
	private List<Border> remainingAmmoBorders; // for grading when ammo depletes
	private Map<JButton, Border> originalBorders; // for restoring original border after cursor moves
	private HashMap<String, Map<String, ImageIcon>> allskins = null;
	
	private Border getBorder(Color c) {
		if (engine.getState().shotspershipenabled)
			return BorderFactory.createMatteBorder(2,2,2,2,c);
		else
			return BorderFactory.createMatteBorder(1,1,1,1,c);
			
	}

	/**
	 * @return the other boardpanel in use. can return null when i.e. placing ships
	 */
	public BoardPanel getOtherBoard() {
		return otherBoard;
	}

	public void setOtherBoard(BoardPanel otherBoard) {
		this.otherBoard = otherBoard;
	}

	/**
	 * @param app for calling into on events
	 * @param engine for probing about game state
	 * @param player for use when notifying about events
	 * @param dontfog would be true when showing own board or placing ships
	 */
	public BoardPanel(BoardUser app, Engine engine, int player, boolean dontfog) {
		super();
		this.app = app;
		this.engine = engine;
		this.player = player;
		this.dontfog = dontfog;

		standardBorder = getBorder(Color.BLACK);
		cursorBorder = getBorder(Color.WHITE);
		selectedShooterBorder = getBorder(Color.RED);
		targetBorder = getBorder(new Color(160,32,240));

		remainingAmmoBorders = new ArrayList<Border>();
		int initialAmmoCount = engine.getInitialAmmoCount();
		for (int i = 0; i <= initialAmmoCount; i++) {
			float h = 0.10f;
			float s = 1.00f;
			float l = ((float) i) / initialAmmoCount;
			remainingAmmoBorders.add(getBorder(Color.getHSBColor(h, s, 0.5f + l/2))); // HSB == HSL
		}

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

	/**
	 * for marking targets when reichweite von schiffen is enabled. takes array of format from Engine.getTargets
	 * @param targets true when in range, false when not. only object because of generics
	 */
	public void markTargets(Boolean[][] targets) {
		
		for (int i = 0; i < targets.length; i++) {
			for (int j = 0; j < targets[i].length; j++) {
				if (targets[i][j]) {
					
					buttons[i][j].setBorder(targetBorder);
				}
			}
		}
		
		for (int i = 0; i < targets.length; i++) {
			for (int j = 0; j < targets[i].length; j++) {
				Border bo;
				if (targets[i][j]) {
					bo = targetBorder;
				} else {
					bo = standardBorder;
				}
				buttons[i][j].setBorder(bo);
				originalBorders.put(buttons[i][j], bo);
			}
		}
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
		
		originalBorders = new HashMap<JButton, Border>();

		for (int i=0; i<engine.getxWidth(); i++) {
			for (int j=0; j<engine.getyWidth(); j++) {
				String iconname = TemplateImages.fieldToIcon(b[i][j]);

				buttons[i][j] = placeEntity(iconname);

				Border bo;
				if (!engine.getState().shotspershipenabled || player != 0) {
					bo = standardBorder;
				} else {
					if (engine.getState().getFiringX(0) == i && engine.getState().getFiringY(0) == j) {
						bo = selectedShooterBorder;
					} else {
						if (engine.getState().getLevel().isShipAt(0,i,j))
						  bo = remainingAmmoBorders.get(engine.remainingShotsFor(0,i,j));
						else
						  bo = standardBorder;
					}
				}
				originalBorders.put(buttons[i][j], bo);
				buttons[i][j].setBorder(bo);
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

	/**
	 * from template
	 */
	private void registerImage(String skinName, String identifier, URL url) {
		Map<String, ImageIcon> skinMap = allskins.get(skinName);
		if (skinMap==null) { skinMap = new HashMap<String, ImageIcon>(); allskins.put(skinName, skinMap); }
		skinMap.put(identifier, new ImageIcon(url));
		
	}


	/**
	 * from template
	 */
	private JButton placeEntity(String imageIdentifier) {
		return placeEntity(allskins.get(app.getSelectedSkin()).get(imageIdentifier)	);
	}
	

	/**
	 * when moving cursor
	 */
	public void removeSelection() {
		if (currentlyselected != null) {
			currentlyselected.button.setBorder(originalBorders.get(buttons[currentlyselected.x][currentlyselected.y]));
			currentlyselected = null;
		}
	}
	
	public void addSelection(int x, int y) {
		currentlyselected = new SelectedTriple();
		currentlyselected.x = x;
		currentlyselected.y = y;
		currentlyselected.button = buttons[x][y];
		currentlyselected.button.setBorder(cursorBorder);
	}
	
	/**
	 * from template
	 */
	private JButton placeEntity(Icon icon){
		JButton btn = new JButton();

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

	/**
	 * from template, but removed useless loop. was mouseClicked in template, but pressed is better since clicked doesnt fire when mouse moves too much before button goes up
	 */
	@Override
	public void mousePressed(MouseEvent evt) {	
		JButton refBtn = entities.get(0);
		
		int posX = evt.getXOnScreen();
		posX = posX - (int) this.getLocationOnScreen().getX();

		int posY = evt.getYOnScreen();
		posY = posY - (int) this.getLocationOnScreen().getY();
				
		posX /= refBtn.getWidth();
		posY /= refBtn.getHeight();
				
		evt.consume();
		
		//System.out.println(String.format("%d,%d", posX, posY));
		app.bomb (player, posX, posY);
	}
	
	/**
	 * remake buttons from engine state
	 */
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
