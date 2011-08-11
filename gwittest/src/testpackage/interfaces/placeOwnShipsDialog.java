package testpackage.interfaces;

import testpackage.shared.ship.Engine;
import testpackage.shared.ship.Ship.Orientation;
import testpackage.shared.ship.Rules;
import testpackage.shared.ship.Ship;
import testpackage.shared.ship.gui.TemplateImages;
import translator.Translator;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * dialog for placing own ships
 */
public class placeOwnShipsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	Translator translator;
	
	/**
	 * list for storing all ships placed so far
	 */
	List<Ship> chosencoords;
	/**
	 * map so that we can know the length of the ship we're placing, from the "place" button pushed
	 */
	Map<JButton, Integer> buttolen; 
	/**
	 * our own engine in which we draw the ships. if user chooses all ships, this will be used in-game.
	 */
	public Engine engine;
	private int shipcount = 0;
	/**
	 * ok button for confirming all ships chosen correctly
	 */
	private JButton ok;
	/**
	 * did user choose all ship and confirm?
	 */
	public boolean finished = false;

	BoardUser parentuser;
	
	/**
	 * @return list of chosen ships
	 */
	public List<Ship> getChosencoords() {
		return chosencoords;
	}
	
	private void build(Container cp) {
		chosencoords = new ArrayList<Ship>();
		buttolen = new HashMap<JButton, Integer>();
		
		for (int i = 0; i < Rules.ships.length; i++) {
			int[] shiprules = Rules.ships[i];
			for (int j = 0; j < shiprules[1]; j++) {
				shipcount++;
				JPanel g = new JPanel();
				
				JLabel lab = new JLabel(translator.translateMessage("POSDN_Ship", String.valueOf(shiprules[0])));
				JButton but = new JButton(translator.translateMessage("POSDPlace"));
				but.setIcon(TemplateImages.getIcon("ship"));
				
				buttolen.put(but,shiprules[0]);
				g.add(lab);
				g.add(but);
				cp.add(g);
				but.addActionListener(new placeOwnShipsDialogCoordReceiver(this));
			}
		}
	}
	
	/**
	 * called when player chose ship position
	 * @param x x coord of upper/left ship end
	 * @param y y coord of upper/left ship end
	 * @param len length of ship
	 * @param o orientation of ship
	 */
	void chose(int x, int y, int len, Orientation o) {
		chosencoords.add(new Ship(x, y, len, o));
		if (chosencoords.size() == shipcount) ok.setEnabled(true);
	}
	
	/**
	 * constructor shows window
	 * @param parent parent will normally be GUIShiffe frame
	 */
	public placeOwnShipsDialog(JFrame parent, Translator translator, Engine engine, BoardUser parentuser) {
		super(parent, translator.translateMessage("POSDWindowTitle"), true);
		
		this.translator=translator;
		this.engine=engine;
		this.parentuser = parentuser;
		
		engine.getState().getLevel().clearPlayerBoard();
		
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
		//cp.setLayout(new FlowLayout());
		JPanel placers = new JPanel();
		placers.setLayout(new BoxLayout(placers, BoxLayout.PAGE_AXIS));
		build(cp);
		cp.add(placers);

		JPanel bottompanel = new JPanel();
		ok = new JButton(translator.translateMessage("OK"));
		ok.setActionCommand("ok");
		ok.setEnabled(false);
		ok.addActionListener(this);
		JButton cancel = new JButton(translator.translateMessage("Cancel"));
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		
		bottompanel.add(ok);
		bottompanel.add(cancel);
		cp.add(bottompanel);
		//setSize(250, 500);
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			finished = true;
			/*for (Ship s : chosencoords) {
				System.out.println(s.toString());
			}*/
		} else if (e.getActionCommand().equals("cancel")) {

		}
		dispose();
	}
}
