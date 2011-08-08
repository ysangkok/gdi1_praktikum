package testpackage.interfaces;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import testpackage.shared.ship.AI;
import testpackage.shared.ship.BadAI;
import testpackage.shared.ship.GoodAI;
import testpackage.shared.ship.Rules;

class SettingsChooser {

	boolean finished = false;
	boolean speerfeuerenabled = false;
	boolean ammoenabled = false;
	int ammospinnervalue;
	int speerfeuerspinnervalue;
	Class<? extends AI> chosenAI;
	
	void askForSettings(JFrame parent) {

		/* TODO
		 * 3. board size vælger
		 */
		
		SpinnerModel ammomodel = new SpinnerNumberModel(Rules.shotsPerShipPart, 1, 20, 1);
		SpinnerModel speerfeuermodel = new SpinnerNumberModel(((double) Rules.standardSpeerfeuerTime)/1000, 0.1, 30.0, 0.1);
		
		final JSpinner ammospinner = new JSpinner(ammomodel);
		final JSpinner speerfeuerspinner = new JSpinner(speerfeuermodel);
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean checked = ((AbstractButton) arg0.getSource()).isSelected();

				if (arg0.getActionCommand().equals("munition")) {
					ammospinner.setEnabled(checked);
				} else if (arg0.getActionCommand().equals("speerfeuer")) {
					speerfeuerspinner.setEnabled(checked);					
				}
			}
		};
		
		final JDialog d = new JDialog(parent, "Choose game type", Dialog.ModalityType.DOCUMENT_MODAL);
		BoxLayout layout = new BoxLayout(d.getContentPane(), BoxLayout.Y_AXIS);
		d.setLayout(layout);
		final JCheckBox speerfeuercb = new JCheckBox("Speerfeuer enabled:");
		speerfeuercb.setSelected(speerfeuerenabled);
		speerfeuercb.addActionListener(listener);
		speerfeuercb.setActionCommand("speerfeuer");
		final JCheckBox ammocb = new JCheckBox("Munition enabled:");
		ammocb.setSelected(ammoenabled);
		ammocb.addActionListener(listener);
		ammocb.setActionCommand("munition");
		
		speerfeuerspinner.setEnabled(speerfeuerenabled);
		ammospinner.setEnabled(ammoenabled);
		
		@SuppressWarnings("unchecked")
		final Class<? extends AI>[] availableAIs = new Class[] {BadAI.class, GoodAI.class};
		final JComboBox aidropdown = new JComboBox();
		for (Class<? extends AI> ai : availableAIs) {
			String name = ai.getName();
			if (name.lastIndexOf('.') > 0) {
				name = name.substring(name.lastIndexOf('.')+1);
			}
			aidropdown.addItem(name);
		}
		
		d.add(speerfeuercb);
		d.add(new JLabel("Time per turn in seconds:"));
		d.add(speerfeuerspinner);
		d.add(ammocb);
		d.add(new JLabel("Ammo per field in seconds:"));
		d.add(ammospinner);
		d.add(new JLabel("AI:"));
		d.add(aidropdown);
		JButton but = new JButton("OK");
		but.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				finished = true;
				speerfeuerenabled = speerfeuercb.isSelected();
				ammoenabled = ammocb.isSelected();
				speerfeuerspinnervalue = (int) (1000 * ((Double)speerfeuerspinner.getValue()));
				ammospinnervalue = (Integer) ammospinner.getValue();
				chosenAI = availableAIs[aidropdown.getSelectedIndex()];
				try {
					if (!chosenAI.getConstructor().newInstance().supportsAmmo() && ammoenabled) {
						javax.swing.JOptionPane.showMessageDialog(d, aidropdown.getSelectedItem() + " doesn't support ammunition. Choose another AI.", "AI skills inadequate", 0);
						return;
					}
				} catch (Exception ex) {
				}
				d.dispose();
			}
		});
		d.add(but);
		d.pack();
		d.setVisible(true);
		//return new boolean[] {ammoenabled, speerfeuerenabled};
	}
}
