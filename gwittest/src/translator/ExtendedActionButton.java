package translator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * This class represents a specialized JButton that allows calling methods on
 * objects
 * 
 * To use, simply invoke the ExtendedActionButton constructor by giving the
 * action to be performed and a mnemonic.
 * 
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org>">
 *         roessling@acm.org</a>)
 * @version 1.1 2000-10-22
 */
public class ExtendedActionButton extends JButton {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 8245742211289028137L;

	/**
	 * The action object used - normally an ExtendedAction instance
	 */
	private Action action;

	/**
	 * Used to adapt to changed properties
	 */
	private transient PropertyChangeListener propertyChangeListener;

	/**
	 * Empty constructor
	 */
	public ExtendedActionButton() {
		// do nothing
	}

	/**
	 * Construct a new ExtendedActionButton with the given action using the
	 * mnemonic
	 * 
	 * @param theAction
	 *          the Action to perform on getting pressed -- usually, this will be
	 *          an ExtendedAction instance
	 * @param mnemonic
	 *          the mnemonic to use for this element
	 */
	public ExtendedActionButton(Action theAction, int mnemonic) {
		setAction(theAction);
		setMnemonic(mnemonic);
	}

	/**
	 * Set the action to perform once pressed
	 * 
	 * @param newValue
	 *          the new Action object to use
	 */
	public void setAction(Action newValue) {
		// disconnect previous action!
		if (action != null && propertyChangeListener != null) {
			action.removePropertyChangeListener(propertyChangeListener);
			removeActionListener(action);
		}

		// set the action
		action = newValue;

		// reconnect
		if (action == null) {
			setText("");
			setIcon(null);
		} else {
			setText((String) action.getValue(Action.NAME));
			setIcon((Icon) action.getValue(Action.SMALL_ICON));
			setEnabled(action.isEnabled());
			String toolTipText = (String) action.getValue(Action.SHORT_DESCRIPTION);
			if (toolTipText != null)
				setToolTipText(toolTipText);
			addActionListener(action);
			if (propertyChangeListener == null)
				propertyChangeListener = new LocalPropertyChangeListener();
			action.addPropertyChangeListener(propertyChangeListener);
		}
	}

	/**
	 * A local property change listener for taking care of changed properties
	 */
	class LocalPropertyChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			String propertyName = propertyChangeEvent.getPropertyName();
			if (propertyName.equals(Action.NAME))
				setText((String) propertyChangeEvent.getNewValue());
			else if (propertyName.equals(Action.SMALL_ICON))
				setIcon((Icon) propertyChangeEvent.getNewValue());
			else if (propertyName.equals(Action.SHORT_DESCRIPTION))
				setToolTipText((String) propertyChangeEvent.getNewValue());
			else if (propertyName.equals("enabled"))
				setEnabled(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
		}
	}
}
