package testpackage.interfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import testpackage.shared.ship.AI;
import testpackage.shared.ship.BadAI;
import testpackage.shared.ship.Engine;
import testpackage.shared.ship.GoodAI;
import testpackage.shared.ship.IntelligentAI;
import testpackage.shared.ship.Rules;
import translator.Translator;



/**
 * class for choosing game settings with guischiffe
 */
class SettingsChooser {

	class ShipConfigrator implements ChangeListener {
		
		private JPanel panel;
		private SpinnerModel model;
		List<JFormattedTextField[]> shiptypes;

		ShipConfigrator(JPanel panel, SpinnerModel model) {
			this.model = model;
			this.panel = panel;
			redrawPanel(true);
		}
		
		private void redrawPanel(boolean first) {
			panel.removeAll();
			shiptypes = new ArrayList<JFormattedTextField[]>();
			for (int i = 1; i <= (Integer) model.getValue(); i++) {
				JPanel line = new JPanel();
				line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
				
		        JFormattedTextField type = new JFormattedTextField();
		        //numPeriodsField.setValue(new Integer(numPeriods));
		        type.setColumns(2);
		        //type.addPropertyChangeListener("value", this);
				
		        JFormattedTextField count = new JFormattedTextField();
		        count.setColumns(2);
		        
		        type.setMaximumSize(type.getPreferredSize());
		        count.setMaximumSize(count.getPreferredSize());
		        
		        if (first) {
		        	type.setValue(new Integer(Rules.ships[i-1][0]));
		        	count.setValue(new Integer(Rules.ships[i-1][1]));
		        } else {
		        	type.setValue(new Integer(0));
		        	count.setValue(new Integer(0));
		        }

		        line.add(new JLabel("Ship length:"));
		        line.add(type);
		        line.add(new JLabel("Ship count:"));
		        line.add(count);
		        panel.add(line);
		        shiptypes.add(new JFormattedTextField[] {type, count});

			}
			d.pack();
		}

		@Override
		public void stateChanged(ChangeEvent arg0) {
			//System.err.println(((SpinnerModel) arg0.getSource()).getValue());
			redrawPanel(false);
		}
	}
	
	JDialog d;
	int[][] shiprules;
	boolean finished = false; // is set to true if the user confirmed his choices by clicking ok and validation passed
	boolean speerfeuerenabled = false; // these next attributes store the results and are set by the ok button handler and read outside this class
	boolean ammoenabled = false;
	boolean moreshotsenabled = false;
	boolean rangeenabled = false;
	int ammospinnervalue;
	int speerfeuerspinnervalue;
	int w; //board width
	int h; //board height
	Class<? extends AI> chosenAI;
	Translator translator;
	
	SettingsChooser(Translator translator) {
		 this.translator = translator;
	}
	
	void askForSettings(JFrame parent) {
		
		d = new JDialog(parent, translator.translateMessage("SCWindowTitle"), Dialog.ModalityType.DOCUMENT_MODAL);
		
		SpinnerModel ammomodel = new SpinnerNumberModel(Rules.shotsPerShipPart, 1, 20, 1);
		SpinnerModel speerfeuermodel = new SpinnerNumberModel(((double) Rules.standardSpeerfeuerTime)/1000, 0.1, 30.0, 0.1);
		SpinnerModel wboarddimensionmodel = new SpinnerNumberModel(Rules.defaultWidth, 4, 100 ,1);
		SpinnerModel hboarddimensionmodel = new SpinnerNumberModel(Rules.defaultHeight, 4, 100 ,1);
		SpinnerModel shipcountmodel = new SpinnerNumberModel(Rules.ships.length, 1, 25, 1);
		
		final JPanel customizepanel = new JPanel();
		customizepanel.setLayout(new BoxLayout(customizepanel, BoxLayout.PAGE_AXIS));
		final JPanel shipspanel = new JPanel();
		shipspanel.setBorder(BorderFactory.createTitledBorder("Ship types"));
		shipspanel.setLayout(new BoxLayout(shipspanel, BoxLayout.PAGE_AXIS));
		shipspanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		final ShipConfigrator sc = new ShipConfigrator(shipspanel, shipcountmodel);
		shipcountmodel.addChangeListener(sc);
		
		final JCheckBox rangecb = new JCheckBox(translator.translateMessage("SCRange"));
		final JCheckBox customizeshipscb = new JCheckBox("Customize ship properties"); //
		
		final JSpinner shipcountspinner = new JSpinner(shipcountmodel);
		shipcountspinner.setMaximumSize(shipcountspinner.getPreferredSize());
		shipcountspinner.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		customizepanel.add(new JLabel("Number of different ship lengths:"));
		customizepanel.add(shipcountspinner);
		customizepanel.add(shipspanel);
		customizepanel.setVisible(customizeshipscb.isSelected());
		
		final JSpinner ammospinner = new JSpinner(ammomodel);
		final JSpinner speerfeuerspinner = new JSpinner(speerfeuermodel);
		final JSpinner heightspinner = new JSpinner(hboarddimensionmodel);
		final JSpinner widthspinner = new JSpinner(wboarddimensionmodel);
		heightspinner.setMaximumSize(heightspinner.getPreferredSize());
		widthspinner.setMaximumSize(widthspinner.getPreferredSize());
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean checked = ((AbstractButton) arg0.getSource()).isSelected();

				if (arg0.getActionCommand().equals("munition")) {
					ammospinner.setEnabled(checked);
					rangecb.setEnabled(checked);
				} else if (arg0.getActionCommand().equals("speerfeuer")) {
					speerfeuerspinner.setEnabled(checked);
				} else if (arg0.getActionCommand().equals("customizeships")) {
					customizepanel.setVisible(customizeshipscb.isSelected());
					d.pack();
				}
			}
		};
		
		Box box = Box.createVerticalBox();

		final JCheckBox moreshotscb = new JCheckBox(translator.translateMessage("SCMoreShots"));
		moreshotscb.setSelected(moreshotsenabled);
		
		final JCheckBox speerfeuercb = new JCheckBox(translator.translateMessage("SCSpeerCheckBox"));
		//speerfeuercb.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
		speerfeuercb.setSelected(speerfeuerenabled);
		speerfeuercb.addActionListener(listener);
		speerfeuercb.setActionCommand("speerfeuer");
		final JCheckBox ammocb = new JCheckBox(translator.translateMessage("SCAmmoCheckBox"));
		ammocb.setSelected(ammoenabled);
		ammocb.addActionListener(listener);
		ammocb.setActionCommand("munition");
		customizeshipscb.addActionListener(listener);
		customizeshipscb.setActionCommand("customizeships");
		
		rangecb.setEnabled(ammoenabled);
		rangecb.setSelected(rangeenabled);
		
		speerfeuerspinner.setEnabled(speerfeuerenabled);
		ammospinner.setEnabled(ammoenabled);
		
		@SuppressWarnings("unchecked")
		final Class<? extends AI>[] availableAIs = new Class[] {BadAI.class, GoodAI.class, IntelligentAI.class};
		final JComboBox aidropdown = new JComboBox();
		for (Class<? extends AI> ai : availableAIs) {
			String name = ai.getName();
			if (name.lastIndexOf('.') > 0) {
				name = name.substring(name.lastIndexOf('.')+1);
			}
			aidropdown.addItem(name);
		}

		float alignment = Component.LEFT_ALIGNMENT;
		
		heightspinner.setAlignmentX(alignment);
		widthspinner.setAlignmentX(alignment);

		JLabel heightlabel = new JLabel(translator.translateMessage("SCHeightLabel"));
		JLabel widthlabel = new JLabel(translator.translateMessage("SCWidthLabel"));

		moreshotscb.setAlignmentX(alignment);
		speerfeuercb.setAlignmentX(alignment);
		JLabel speerlabel = new JLabel(translator.translateMessage("SCSpeerLabel"));
		speerfeuerspinner.setAlignmentX(alignment);
		speerfeuerspinner.setMaximumSize(speerfeuerspinner.getPreferredSize());
		ammocb.setAlignmentX(alignment);
		JLabel ammolabel = new JLabel(translator.translateMessage("SCAmmoLabel"));
		ammospinner.setAlignmentX(alignment);
		ammospinner.setMaximumSize(ammospinner.getPreferredSize());
		JLabel ailabel = new JLabel(translator.translateMessage("SCAILabel"));
		aidropdown.setAlignmentX(alignment);
		aidropdown.setMaximumSize(aidropdown.getPreferredSize());
		JButton but = new JButton(translator.translateMessage("OK"));
		but.setAlignmentX(alignment);
		but.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				w = (Integer) heightspinner.getValue(); // swap w/h because of our coord format
				h = (Integer) widthspinner.getValue();
				
				moreshotsenabled = moreshotscb.isSelected();
				speerfeuerenabled = speerfeuercb.isSelected();
				ammoenabled = ammocb.isSelected();
				speerfeuerspinnervalue = (int) (1000 * ((Double)speerfeuerspinner.getValue()));
				ammospinnervalue = (Integer) ammospinner.getValue();
				rangeenabled = ammoenabled && rangecb.isSelected();
				chosenAI = availableAIs[aidropdown.getSelectedIndex()];
				if (customizeshipscb.isSelected()) {
					Map<Integer,Integer> mapLengthToCount = new HashMap<Integer,Integer>();
					for (JFormattedTextField[] pair : sc.shiptypes) {
						System.err.println(pair[0].getValue());
						System.err.println(pair[1].getValue());
						int type, count;

						type = ((Number)pair[0].getValue()).intValue();
						count = ((Number)pair[1].getValue()).intValue();
						
						if (type <= 0 || count <= 0) {
							javax.swing.JOptionPane.showMessageDialog(d, "Numbers must be positive.", "Error", 0);
							return;
						}
						
						if (mapLengthToCount.containsKey(type)) {
							javax.swing.JOptionPane.showMessageDialog(d, "You can't specify the same ship type count more than once.", "Error", 0);
							return;
						}
						mapLengthToCount.put(type, count);
					}
					shiprules = new int[mapLengthToCount.size()][3];
					int i = 0;
					for (Entry<Integer, Integer> entry : mapLengthToCount.entrySet()) {
						shiprules[i++] = new int[] {entry.getKey(), entry.getValue(), Engine.decideShipRange(w, h, entry.getKey(), entry.getValue())};
					}
				} else {
					shiprules = Rules.ships;
				}
				try {
					if (ammoenabled) {
						if (!chosenAI.getConstructor().newInstance().supportsAmmo()) {
							javax.swing.JOptionPane.showMessageDialog(d, translator.translateMessage("SCAIUncapable",(String) aidropdown.getSelectedItem()), translator.translateMessage("userErrorWindowTitle"), 0);
							return;
						}
						if (rangeenabled && !chosenAI.getConstructor().newInstance().supportsRange()) {
							javax.swing.JOptionPane.showMessageDialog(d, "Selected AI doesn't support range", translator.translateMessage("userErrorWindowTitle"), 0);
							return;
						}
					}
				} catch (Exception ex) { // for the getConstructor().newInstance() which should never fail
				}
				finished = true;
				d.dispose();
			}
		});

		box.add(heightlabel);
		box.add(heightspinner);
		box.add(widthlabel);
		box.add(widthspinner);
		box.add(Box.createRigidArea(new Dimension(0,15)));
		box.add(moreshotscb);
		box.add(Box.createRigidArea(new Dimension(0,15)));
		box.add(speerfeuercb);
		box.add(speerlabel);
		box.add(speerfeuerspinner);
		box.add(Box.createRigidArea(new Dimension(0,15)));
		box.add(ammocb);
		box.add(ammolabel);
		box.add(ammospinner);
		box.add(rangecb);
		box.add(Box.createRigidArea(new Dimension(0,15)));
		box.add(ailabel);
		box.add(aidropdown);
		box.add(Box.createRigidArea(new Dimension(0,15)));
		box.add(customizeshipscb);
		box.add(customizepanel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(but);
		box.add(Box.createVerticalGlue());
		box.add(buttonPanel);

		d.add(box, BorderLayout.CENTER);
		d.pack();
		d.setVisible(true);
		//return new boolean[] {ammoenabled, speerfeuerenabled};
	}
}
