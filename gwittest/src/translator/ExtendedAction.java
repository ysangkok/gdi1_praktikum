package translator;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JToolBar;

/**
 * This class represents an action object that allows calling methods on objects
 * 
 * To use, simply invoke the ExtendedAction constructor by giving the label,
 * icon name, tool tip text and the method to call (which currently must not
 * have any parameters).
 * 
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org>">
 *         roessling@acm.org</a>)
 * @version 1.1 2000-10-22
 */
public class ExtendedAction extends AbstractAction {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The arguments for the method call
	 */
	private Object[] arguments;

	/**
	 * The icon shown in this action element
	 */
	private Icon icon;

	//
	// /**
	// * The key used for requesting a translation for the current locale
	// */
	// private String key;

	/**
	 * The method name to invoke once the action element is activated
	 */
	private String methodToCall;

	/**
	 * The wrapper of the current Action element
	 */
	protected AbstractButton wrapper;

	/**
	 * toggles if the label is to be shown; set false in constructor for buttons!
	 */
	// private boolean showLabel = true;
	/**
	 * The object on which to invoke the method
	 */
	private Object targetObject;

	/**
	 * The current Translator instance for this object
	 */
	private transient Translator trans = null;

	/**
	 * Build a new object for invoking targetCall on the default Animal object
	 * 
	 * @param objectKey
	 *          the key for this object, used for retrieving the localized
	 *          information
	 * @param locale
	 *          the current locale
	 * @param bundle
	 *          the resource bundle used for retrieving the data from
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 */
	public ExtendedAction(String objectKey, 
			Locale locale,
			ExtendedResourceBundle bundle, 
			Object invocationTargetObject,
			Translator translator) {
		this(bundle.getMessage(objectKey + ".label"), bundle.getMessage(objectKey
				+ ".iconName"), bundle.getMessage(objectKey + ".toolTipText"), bundle
				.getMessage(objectKey + ".targetCall"), invocationTargetObject, null,
				null, null, translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 */
	public ExtendedAction(String label, String iconName, String toolTipText,
			String targetCall, Object invocationTargetObject, Translator translator) {
		this(label, iconName, toolTipText, targetCall, invocationTargetObject,
				null, null, null, translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param args
	 *          the arguments for the method invocation
	 */
	public ExtendedAction(String label, String iconName, String toolTipText,
			String targetCall, Object invocationTargetObject, Object[] args,
			Translator translator) {
		this(label, iconName, toolTipText, targetCall, invocationTargetObject,
				args, null, null, translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param args
	 *          the arguments for the method invocation
	 * @param key
	 *          the key for this action when stored in the Hashtable
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 */
	public ExtendedAction(String label, String iconName, String toolTipText,
			String targetCall, Object invocationTargetObject, Object[] args,
			String key, Hashtable<String, ExtendedAction> hash, Translator translator) {
		super(label, translator.getGenerator().getImageIcon(iconName));
		trans = translator;
		// showLabel = (label != null);
		icon = translator.getGenerator().getImageIcon(iconName);
		methodToCall = targetCall;
		setToolTipText(toolTipText);
		targetObject = invocationTargetObject;
        if (args != null)
          arguments = args.clone();
		if (hash != null && key != null)
			hash.put(key, this);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param args
	 *          the arguments for the method invocation
	 * @param key
	 *          the key for this action when stored in the Hashtable
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 * @param toolBar
	 *          the JToolBar to which the element is added
	 * 
	 * @return the ExtendedAction object generated
	 */
	public static ExtendedAction addToToolBar(String label, String iconName,
			String toolTipText, String targetCall, Object invocationTargetObject,
			Object[] args, String key, Hashtable<String, ExtendedAction> hash,
			JToolBar toolBar, Translator translator) {
		return addToToolBar(label, iconName, toolTipText, targetCall,
				invocationTargetObject, args, key, hash, '\n', toolBar, translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param args
	 *          the arguments for the method invocation
	 * @param key
	 *          the key for this action when stored in the Hashtable
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 * @param mnemonic
	 *          the mnemonic to be used for the element
	 * @param toolBar
	 *          the JToolBar to which the element is added
	 * @param translator the Translator used for I18N         
	 * 
	 * @return the ExtendedAction object generated
	 */
	public static ExtendedAction addToToolBar(String label, String iconName,
			String toolTipText, String targetCall, Object invocationTargetObject,
			Object[] args, String key, Hashtable<String, ExtendedAction> hash,
			char mnemonic, JToolBar toolBar, Translator translator) {
		return insertAction(label, iconName, toolTipText, targetCall,
				invocationTargetObject, args, key, hash, mnemonic, toolBar, null,
				translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param args
	 *          the arguments for the method invocation
	 * @param key
	 *          the key for this action when stored in the Hashtable
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 * @param mnemonic
	 *          the mnemonic to be used for the element
	 * @param menu
	 *          the JMenu to which the element is added
	 * @param translator the Translator used for I18N         
	 * 
	 * @return the ExtendedAction object generated
	 */
	public static ExtendedAction addToMenu(String label, String iconName,
			String toolTipText, String targetCall, Object invocationTargetObject,
			Object[] args, String key, Hashtable<String, ExtendedAction> hash,
			char mnemonic, JMenu menu, Translator translator) {
		return insertAction(label, iconName, toolTipText, targetCall,
				invocationTargetObject, args, key, hash, mnemonic, null, menu,
				translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param label
	 *          the String label shown on the action element
	 * @param iconName
	 *          the filename of the icon to use (which is loaded automatically)
	 * @param toolTipText
	 *          the text of the tool tip
	 * @param targetCall
	 *          the method to invoke once the ExtendedAction object is activated
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param args
	 *          the arguments for the method invocation
	 * @param key
	 *          the key for this action when stored in the Hashtable
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 * @param mnemonic
	 *          the mnemonic to be used for the element
	 * @param toolBar
	 *          the JToolBar to which the element is added
	 * @param menu
	 *          the JMenu to which the element is added
	 * @param translator the Translator used for I18N         
	 * 
	 * @return the ExtendedAction object generated
	 */
	public static ExtendedAction insertAction(String label, String iconName,
			String toolTipText, String targetCall, Object invocationTargetObject,
			Object[] args, String key, Hashtable<String, ExtendedAction> hash,
			char mnemonic, JToolBar toolBar, JMenu menu, Translator translator) {
		ExtendedAction action = null;
		if (toolBar != null) {
			action = new ExtendedAction(null, iconName, toolTipText, targetCall,
					invocationTargetObject, args, key + ".Button", hash, translator);
			action.wrapper = toolBar.add(action);
			action.wrapper.setToolTipText(action.getToolTipText());
			if (mnemonic != '\n')
				action.wrapper.setMnemonic(mnemonic);
		}
		if (menu != null) {
			action = new ExtendedAction(label, iconName, toolTipText, targetCall,
					invocationTargetObject, args, key + ".Item", hash, translator);
			action.wrapper = menu.add(action);
			action.wrapper.setToolTipText(action.getToolTipText());
			if (mnemonic != '\n')
				action.wrapper.setMnemonic(mnemonic);
		}
		return action;
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param objectKey
	 *          the key for the element
	 * @param locale
	 *          the locale to be used for the label
	 * @param bundle
	 *          the animalResourceBundle used for translating the label
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param arguments
	 *          the arguments for the method invocation
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 * @param toolBar
	 *          the JToolBar to which the element is added
	 * @param menu
	 *          the JMenu to which the element is added
	 * 
	 * @return the ExtendedAction object generated
	 */
	public static ExtendedAction insertAction(String objectKey, Locale locale,
			ExtendedResourceBundle bundle, Object invocationTargetObject,
			Object[] arguments, Hashtable<String, ExtendedAction> hash,
			JToolBar toolBar, JMenu menu, Translator translator) {
		return insertAction(objectKey, locale, bundle, invocationTargetObject,
				arguments, hash, '\n', toolBar, menu, translator);
	}

	/**
	 * Build a new object for invoking targetCall on invocationTargetObject
	 * 
	 * @param objectKey
	 *          the key for the element
	 * @param locale
	 *          the locale to be used for the label
	 * @param bundle
	 *          the animalResourceBundle used for translating the label
	 * @param invocationTargetObject
	 *          the object on which the method will be invoked
	 * @param arguments
	 *          the arguments for the method invocation
	 * @param hash
	 *          the Hashtable storing the actions or null if none is used
	 * @param mnemonic
	 *          the mnemonic to be used for the element
	 * @param toolBar
	 *          the JToolBar to which the element is added
	 * @param menu
	 *          the JMenu to which the element is added
	 * @param translator the Translator used for I18N         
	 * 
	 * @return the ExtendedAction object generated
	 */
	public static ExtendedAction insertAction(String objectKey, 
			Locale locale,
			ExtendedResourceBundle bundle, Object invocationTargetObject,
			Object[] arguments, Hashtable<String, ExtendedAction> hash,
			char mnemonic, JToolBar toolBar, JMenu menu, Translator translator) {
		return insertAction(bundle.getMessage(objectKey + ".label"), bundle
				.getMessage(objectKey + ".iconName"), bundle.getMessage(objectKey
				+ ".toolTipText"), bundle.getMessage(objectKey + ".targetCall"),
				invocationTargetObject, arguments, objectKey, hash, mnemonic, toolBar,
				menu, translator);
	}

	// ======================================================================
	// ATTRIBUTE ACCESS
	// ======================================================================

	/**
	 * Returns the arguments of the method call
	 * 
	 * @return the arguments for the method call
	 */
	public Object[] getArguments() {
		return arguments.clone();
	}

	/**
	 * Return the icon to display
	 * 
	 * @return the icon used in the display
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * Return the label of this element
	 * 
	 * @return the label of this element
	 */
	public String getLabel() {
		return (String) getValue(Action.NAME);
	}

	/**
	 * Return the method to be invoked
	 * 
	 * @return the method to be invoked
	 */
	public String getMethodToCall() {
		return methodToCall;
	}

	public Object getSmallIcon() {
		return getValue(Action.SMALL_ICON);
	}

	/**
	 * Return the target object of the method invocation
	 * 
	 * @return the target object for the method invocation
	 */
	public Object getTargetObject() {
		return targetObject;
	}

	/**
	 * Return the tool tip text
	 * 
	 * @return the text displayed on the tool tip of this Action element
	 */
	public String getToolTipText() {
		return (String) getValue(Action.SHORT_DESCRIPTION);
	}

	/**
	 * Return the Translator instance
	 * 
	 * @return the Translator instance used for this context
	 */
	public Translator getTranslator() {
		/*
		 * if (trans == null) trans = new Translator();
		 */
		return trans;
	}

	/**
	 * Sets the arguments of the method call
	 * 
	 * @param args
	 *          the arguments for the method call
	 */
	public void setArguments(Object[] args) {
		arguments = args.clone();
	}

	/**
	 * Change the icon to display
	 * 
	 * @param theIcon
	 *          the icon to use
	 */
	public void setIcon(Icon theIcon) {
		icon = theIcon;
	}

	/**
	 * Change the icon to display
	 * 
	 * @param iconName
	 *          the name of the icon to use
	 */
	public void setIcon(String iconName) {
		icon = getTranslator().getGenerator().getImageIcon(iconName);
	}

	/**
	 * Change the label to display
	 * 
	 * @param label
	 *          the new label to use
	 */
	public void setLabel(String label) {
		putValue(Action.NAME, label);
	}

	/**
	 * Set the method to call
	 * 
	 * @param methodName
	 *          the name of the method to invoke
	 */
	public void setMethod(String methodName) {
		setTargetCall(methodName, targetObject);
	}

	/**
	 * Set the object whose method shall be invoked
	 * 
	 * @param target
	 *          the object whose method is to be called
	 */
	public void setObject(Object target) {
		setTargetCall(methodToCall, target);
	}

	/**
	 * Set the object on which the method of the given name shall be invoked
	 * 
	 * @param methodName
	 *          the name of the method to invoke
	 * @param target
	 *          the object whose method is to be called
	 */
	public void setTargetCall(String methodName, Object target) {
		methodToCall = methodName;
		targetObject = target;
	}

	/**
	 * Set the tool tip text
	 * 
	 * @param toolTipText
	 *          the text displayed on the tool tip of this Action element
	 */
	public void setToolTipText(String toolTipText) {
		putValue(Action.SHORT_DESCRIPTION, toolTipText);
	}

	// ======================================================================
	// EVENT HANDLING
	// ======================================================================

	/**
	 * Handle the ActionEvent resulting once the object is activated This will
	 * try(!) to invoke the method on the target object
	 * 
	 * @param actionEvent
	 *          the ActionEvent object
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		try {
			if (arguments == null || arguments.length == 0) {
				targetObject.getClass().getDeclaredMethod(methodToCall, 
						(Class[]) null).invoke(targetObject, (Object[]) null);
			} else {
				Object[] args = getArguments();
				Class<?>[] classTypes = new Class<?>[args.length];
				for (int i = 0; i < classTypes.length; i++)
					// {
					classTypes[i] = args[i].getClass();

				targetObject.getClass().getDeclaredMethod(methodToCall, 
						classTypes).invoke(targetObject, args);
			}
		} catch (Exception exception) {
			Debug.printlnMessage(getTranslator().translateMessage(
					"verboseException",
					new Object[] { methodToCall, targetObject.getClass().getName(),
							exception.getMessage(), actionEvent.getSource().toString(),
							exception.getClass().getName() }));
			if (arguments != null)
				for (int i = 0; i < arguments.length; i++)
					Debug.printlnMessage("arg #" + i + ": " + arguments[i]);
		}
	}

	/**
	 * Provide a String representation of this object
	 * 
	 * @return the String representation of this object
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ExtendedAction { ");
		Object label = getValue(Action.NAME);
		if (label == null)
			sb.append("no label");
		else
			sb.append("label='").append(label).append("'");
		sb.append(", toolTipText: '").append(getValue(Action.SHORT_DESCRIPTION));
		sb.append("', targetObject: '").append(targetObject.getClass().getName())
				.append("', method name: '");
		sb.append(methodToCall).append("' }");
		return sb.toString();
	}
}
