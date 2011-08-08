package testpackage.interfaces;

import testpackage.shared.ship.Engine;
import testpackage.shared.ship.LevelGenerator;
import testpackage.shared.ship.Ship.Orientation;
import testpackage.shared.ship.State;
import testpackage.shared.ship.exceptions.InvalidLevelException;
import translator.Translator;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * class for handling place button clicking and then making dialog and receiving coordinates from board (implement BoardUser) 
 */
class placeOwnShipsDialogCoordReceiver implements ActionListener, BoardUser {
	private JDialog dialog;
	private Engine engine;
	private placeOwnShipsDialog wizard;
	private int shiplength;
	private JRadioButton rbutton1;
	private JRadioButton rbutton2;
	private boolean didChoose = false; 
	private Translator translator;
	
	/**
	 * @param app the ship placement dialog class
	 */
	public placeOwnShipsDialogCoordReceiver(placeOwnShipsDialog app) {
		engine = app.engine;
		this.wizard = app;
		this.translator = wizard.translator;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton but = (JButton) e.getSource();
		//System.out.println("action: " + e.getActionCommand());
		shiplength = wizard.buttolen.get(but);
		
		//Map2DHelper<Object> helper = new Map2DHelper<Object>();
		 
		//System.out.println(helper.getBoardString(engine.getPlayerArray()));
		
		ButtonGroup group = new ButtonGroup();
		rbutton1 = new JRadioButton(translator.translateMessage("CRHorizontal"), true);
		rbutton2 = new JRadioButton(translator.translateMessage("CRVertical"), false);
	    group.add(rbutton1);
	    group.add(rbutton2);
	    
		BoardPanel bpanel = new BoardPanel(this, engine, 0, true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		//panel.setLayout(new FlowLayout());
		dialog = new JDialog(wizard, translator.translateMessage("CRWindowTitle"), Dialog.ModalityType.DOCUMENT_MODAL);
		JPanel top = new JPanel();
		top.add(new JLabel(translator.translateMessage("CRLabel")));
		top.add(rbutton1);
		top.add(rbutton2);
		panel.add(top);
		bpanel.setMaximumSize( bpanel.getPreferredSize() );
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
					translator.translateMessage("userErrorWindowTitle"), 0);
			wizard.chosencoords.remove(wizard.chosencoords.size()-1);
			return;
		}

		//System.out.println("action performed: " + shiplength);
		
		if (didChoose) but.setEnabled(false);
		
	}

	@Override
	public void bomb(int p, int x, int y) {
		didChoose = true;
		//System.err.println("bomb: " + shiplength + ", x=" + x + ", y=" + y);
		wizard.chose(y, x, shiplength, (rbutton1.getSelectedObjects() != null ? Orientation.HORIZONTAL : Orientation.VERTICAL));
		dialog.dispose();
	}
}
