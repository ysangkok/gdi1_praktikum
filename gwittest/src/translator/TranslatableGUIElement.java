package translator;

import java.awt.Component;
import java.awt.MediaTracker;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;

/**
 * Provides a common interface for translatable GUI element generation Requires
 * an appropriate resource file containing the message translations.
 * 
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 *         roessling@acm.org</a>)
 */
public class TranslatableGUIElement {
  // ======================================================================
  // Attributes
  // ======================================================================

  /**
   * The path where the graphics are, searched from the CLASSPATH
   */
  protected String                     GRAPHICS_PATH          = "/graphics/";

  /**
   * The Hashtable containing the translatable elements
   */
  protected HashMap<Component, String> translatableComponents = new HashMap<Component, String>(
                                                                  4097);
  // private HashMap<String, Object> translatableElements = new HashMap<String,
  // Object>(
  // 1023);

  private Translator                   translator             = null;

  /**
   * Generate a new GUI generator using the concrete Translator passed in
   * 
   * @param t
   *          the current Translator for this object
   */
  public TranslatableGUIElement(Translator t) {
    translator = t;
  }

  // ======================================================================
  // Image retrieval
  // ======================================================================

  protected Class<?> animalImageDummy;

  /**
   * returns the imageIcon with the given name. Note: The preffered approach is
   * to create a directory "graphics", which includes a class
   * "AnimalGraphicsDummy" (which may be empty), and place all icons there. In
   * this way, they will also be loaded if the program is packaged into a JAR
   * file.
   * 
   * @return <b>null</b> if the Icon could not be found or read, <br>
   *         the Icon otherwise.
   */
  public ImageIcon getImageIcon(String name) {
    if (name == null || name.length() == 0)
      return null;
    ImageIcon icon = null;
    URL url;
    if (animalImageDummy == null)
      try {
        animalImageDummy = Class.forName("graphics.AnimalImageDummy");
      } catch (ClassNotFoundException cfe) {
        // System.err.println("@TGUI: AnimalImageDummy could not be found!");
      }
    // Get current classloader
    if (animalImageDummy != null) {
      ClassLoader cl = animalImageDummy.getClassLoader();
      if (cl != null) {

        url = cl.getResource("graphics/" + name);
        if (url != null) {
          icon = new ImageIcon(url);
          if (icon != null)
            return icon;
        }
      } else
        System.err.println("@TGUI: ClassLoader failed, null!");
    }

    if (animalImageDummy != null) {
      url = animalImageDummy.getResource(GRAPHICS_PATH + name);
    } else {
      url = this.getClass().getResource(GRAPHICS_PATH + name);
    }
    if (url == null)
      System.err.println(getTranslator().translateMessage("iconNotFound",
          name + "@TGUI: "));
    else {
      icon = new ImageIcon(url);
      // if (icon == null)
      // System.err.println(getTranslator().translateMessage("iconNotFound",
      // name +"@TGUI: "));
      if (icon.getImageLoadStatus() == MediaTracker.ERRORED)
        System.err.println(getTranslator().translateMessage("iconNotFound",
            name + "@TGUI: "));
    }
    return icon;
  }

  public void setGraphicsPath(String path) {
    GRAPHICS_PATH = path;
  }

  // ======================================================================
  // Element Generation
  // ======================================================================

  public void setTranslator(Translator t) {
    translator = t;
  }

  protected Translator getTranslator() {
    return translator;
  }

  /**
   * Method for generating a new ExtendedAction insertable to a ToolBar or Menu
   * 
   * @param key
   *          the key for the ExtendedAction
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param isButton
   *          if true, generate without label ("button" semantics)
   * 
   * @return the generated ExtendedAction
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public ExtendedAction generateAction(String key, Object[] params,
      Object invocationTargetObject, Object[] args, boolean isButton) {
    ExtendedAction theAction = new ExtendedAction((isButton) ? null
        : getTranslator().translateMessage(key + ".label", params),
        getTranslator().translateMessage(key + ".iconName", params),
        getTranslator().translateMessage(key + ".toolTipText", params),
        getTranslator().translateMessage(key + ".targetCall", params),
        invocationTargetObject, args, getTranslator());
    return theAction;
  }

  /**
   * Generate a button encapsulating a predefined Action element
   * 
   * @param key
   *          the key for this button
   * @param theAction
   *          the action to be encapsulated
   * @return the created object
   */
  public ExtendedActionButton generateActionButton(String key, Action theAction) {
    return generateActionButton(key, null, theAction);
  }

  /**
   * Generate a button encapsulating a predefined Action element
   * 
   * @param key
   *          the key for this button
   * @param params
   *          the parameters needed for formatting the text of the button
   * @param theAction
   *          the action to be encapsulated
   * @return the created object
   */
  public ExtendedActionButton generateActionButton(String key, Object[] params,
      Action theAction) {
    return generateActionButton(key, params, theAction, true);
  }

  /**
   * Generate a button encapsulating a predefined Action element
   * 
   * @param key
   *          the key for this button
   * @param params
   *          the parameters needed for formatting the text of the button
   * @param theAction
   *          the action to be encapsulated
   * @param register
   *          if true, register the component for on-the-fly translation
   * @return the created object
   */
  public ExtendedActionButton generateActionButton(String key, Object[] params,
      Action theAction, boolean register) {
    String labelString = getTranslator().translateMessage(key + ".label",
        params);
    String mnemonicString = getTranslator().translateMessage(key + ".mnemonic");
    char mnemonic = (mnemonicString != null) ? mnemonicString.charAt(0) : key
        .charAt(0);
    ExtendedActionButton theButton = new ExtendedActionButton(theAction,
        mnemonic);
    theButton.setText(labelString);
    String msg = getTranslator().translateMessage(key + ".toolTipText", params,
        false);
    if (msg != null && msg.length() > 0)
      theButton.setToolTipText(msg);
    if (theButton != null && register)
      registerComponent(key, theButton);
    return theButton;
  }

  /**
   * Convenience wrapper for generating a new JButton Internally invokes
   * generateJButton(key, null, false)
   * 
   * @see #generateJButton(String, Object[], boolean)
   * 
   * @param key
   *          the key for the JButton
   * 
   * @return the generated AbstractButton
   */
  public AbstractButton generateJButton(String key) {
    return generateJButton(key, null, false, null, false, true);
  }

  /**
   * Method for generating a new JButton
   * 
   * @param key
   *          the key for the JButton
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isToggleButton
   *          if true, button is toggleable
   * 
   * @return the generated AbstractButton
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton generateJButton(String key, Object[] params,
      boolean isToggleButton) {
    return generateJButton(key, params, isToggleButton, null, false, true);
  }

  /**
   * Method for generating a new JButton
   * 
   * @param key
   *          the key for the JButton
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isToggleButton
   *          if true, button is toggleable
   * @param listener
   *          the ActionListener to be registered with the component
   * 
   * @return the generated AbstractButton
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton generateJButton(String key, Object[] params,
      boolean isToggleButton, ActionListener listener) {
    return generateJButton(key, params, isToggleButton, listener, false, true);
  }

  /**
   * Method for generating a new JButton
   * 
   * @param key
   *          the key for the JButton
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isToggleButton
   *          if true, button is toggleable
   * @param listener
   *          the ActionListener to be registered with the component
   * @param hideLabel
   *          if true, hide the label even if it exists (e.g., for a Toolbar)
   * 
   * @return the generated AbstractButton
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton generateJButton(String key, Object[] params,
      boolean isToggleButton, ActionListener listener, boolean hideLabel) {
    return generateJButton(key, params, isToggleButton, listener, hideLabel,
        true);
  }

  /**
   * Method for generating a new JButton
   * 
   * @param key
   *          the key for the JButton
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isToggleButton
   *          if true, button is toggleable
   * @param listener
   *          the ActionListener to be registered with the component
   * @param hideLabel
   *          if true, hide the label even if it exists (e.g., for a Toolbar)
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated AbstractButton
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton generateJButton(String key, Object[] params,
      boolean isToggleButton, ActionListener listener, boolean hideLabel,
      boolean register) {
    String translatedMessage = getTranslator().translateMessage(key + ".label",
        params);
    AbstractButton localButton = null;
    if (translatedMessage != null) {
      String iconName = getTranslator().translateMessage(key + ".iconName",
          params);
      if (!isToggleButton) {
        if (iconName != null && iconName.length() > 0) {
          localButton = new JButton(getImageIcon(iconName));
          if (!hideLabel)
            localButton.setText(translatedMessage);
        } else
          localButton = new JButton(translatedMessage);
      } else {
        if (iconName != null)
          localButton = new JToggleButton(getImageIcon(iconName));
        else
          localButton = new JToggleButton(translatedMessage);
      }
      String mnemonicString = getTranslator().translateMessage(
          key + ".mnemonic", params, true);
      localButton.setMnemonic((mnemonicString != null) ? mnemonicString
          .charAt(0) : translatedMessage.charAt(0));
      String msg = getTranslator().translateMessage(key + ".toolTipText",
          params, false);
      if (msg != null && msg.length() > 0)
        localButton.setToolTipText(msg);
    }
    if (localButton != null) {
      if (register)
        registerComponent(key, localButton);
      if (listener != null)
        localButton.addActionListener(listener);
    }
    return localButton;
  }

  /**
   * Convenience wrapper for generating a new JButton Internally invokes
   * generateJButton(key, null, false)
   * 
   * @see #generateJButton(String, Object[], boolean)
   * 
   * @param key
   *          the key for the JButton
   * 
   * @return the generated AbstractButton
   */
  public JCheckBox generateJCheckBox(String key) {
    return (JCheckBox) generateJToggleButton(key, null, null, false, true);
  }

  /**
   * Method for generating a new JCheckBox
   * 
   * @param key
   *          the key for the JCheckBox
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param listener
   *          the ActionListener to be registered with the component
   * 
   * @return the generated JCheckBox
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JCheckBox generateJCheckBox(String key, Object[] params,
      ActionListener listener) {
    return (JCheckBox) generateJToggleButton(key, params, listener, false, true);
  }

  /**
   * Method for generating a new JCheckBox
   * 
   * @param key
   *          the key for the JCheckBox
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param listener
   *          the ActionListener to be registered with the component
   * @param isRadioButton
   *          determines if this is a radio button or a check box
   * 
   * @return the generated JCheckBox
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JToggleButton generateJToggleButton(String key, Object[] params,
      ActionListener listener, boolean isRadioButton) {
    return generateJToggleButton(key, params, listener, isRadioButton, true);
  }

  /**
   * Method for generating a new JCheckBox
   * 
   * @param key
   *          the key for the JCheckBox
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param listener
   *          the ActionListener to be registered with the component
   * @param isRadioButton
   *          determines if this is a radio button or a check box
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JCheckBox
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JToggleButton generateJToggleButton(String key, Object[] params,
      ActionListener listener, boolean isRadioButton, boolean register) {

    String translatedMessage = getTranslator().translateMessage(key + ".label",
        params);
    JToggleButton localButton = null;
    if (translatedMessage != null) {
      String iconName = getTranslator().translateMessage(key + ".iconName",
          params);

      if (isRadioButton) {
        if (iconName != null && iconName.length() > 0)
          localButton = new JRadioButton(getImageIcon(iconName));
        else
          localButton = new JRadioButton(translatedMessage);
      } else {
        if (iconName != null && iconName.length() > 0)
          localButton = new JCheckBox(getImageIcon(iconName));
        else
          localButton = new JCheckBox(translatedMessage);
      }
      String mnemonicString = getTranslator().translateMessage(
          key + ".mnemonic", params, true);
      localButton.setMnemonic((mnemonicString != null) ? mnemonicString
          .charAt(0) : translatedMessage.charAt(0));
      String msg = getTranslator().translateMessage(key + ".toolTipText",
          params, false);
      if (msg != null && msg.length() > 0)
        localButton.setToolTipText(msg);

      // localButton.setToolTipText(getTranslator().translateMessage(
      // key + ".toolTipText", params, true));
    }
    if (localButton != null) {
      if (register)
        registerComponent(key, localButton);
      if (listener != null)
        localButton.addActionListener(listener);
    }
    return localButton;
  }

  /**
   * Convenience wrapper for generating a new JComboBox Internally invokes
   * generateJComboBox(key, params, labels, labels[0])
   * 
   * @param key
   *          the key for the JComboBox
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param labels
   *          the labels for the JComboBox
   * 
   * @return the generated JComboBox
   * 
   * @see #generateJComboBox(String, Object[], String[], String)
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JComboBox generateJComboBox(String key, Object[] params,
      String[] labels) {
    return generateJComboBox(key, params, labels, labels[0], true);
  }

  /**
   * Method for generating a new JComboBox
   * 
   * @param key
   *          the key for the JComboBox
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param labels
   *          the labels for the JComboBox
   * @param selectedItem
   *          the default item
   * 
   * @return the generated JComboBox
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JComboBox generateJComboBox(String key, Object[] params,
      String[] labels, String selectedItem) {
    return generateJComboBox(key, params, labels, selectedItem, true);
  }

  /**
   * Method for generating a new JComboBox
   * 
   * @param key
   *          the key for the JComboBox
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param labels
   *          the labels for the JComboBox
   * @param selectedItem
   *          the default item
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JComboBox
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JComboBox generateJComboBox(String key, Object[] params,
      String[] labels, String selectedItem, boolean register) {
    JComboBox internalComboBox = new JComboBox();
    String effectiveItem = selectedItem;
    if (effectiveItem == null)
      effectiveItem = labels[0];
    String msg = getTranslator().translateMessage(key + ".toolTipText", params,
        false);
    if (msg != null && msg.length() > 0)
      internalComboBox.setToolTipText(msg);
    // internalComboBox.setToolTipText(getTranslator().translateMessage(
    // key + ".toolTipText", params));
    for (int i = 0, n = labels.length; i < n; i++)
      internalComboBox.addItem(labels[i]);
    internalComboBox.setSelectedItem(effectiveItem);
    if (register)
      registerComponent(key, internalComboBox);
    return internalComboBox;
  }

  /**
   * Method for generating a new JPopupMenu
   * 
   * @param key
   *          the key for the JPopupMenu
   * @return the generated JPopupMenu
   */
  public JPopupMenu generateJPopupMenu(String key) {
    return generateJPopupMenu(key, true);
  }

  /**
   * Method for generating a new JPopupMenu
   * 
   * @param key
   *          the key for the JPopupMenu
   * @param register
   *          if true, register the component for on-the-fly translation
   * @return the generated JPopupMenu
   */
  public JPopupMenu generateJPopupMenu(String key, boolean register) {
    JPopupMenu popup = new JPopupMenu(key);
    if (register)
      registerComponent(key, popup);
    return popup;
  }

  /**
   * Method for generating a new JFrame
   * 
   * @param key
   *          the key for the JFrame
   * @return the generated JFrame
   */
  public JFrame generateJFrame(String key) {
    return generateJFrame(key, true);
  }

  /**
   * Method for generating a new JFrame
   * 
   * @param key
   *          the key for the JFrame
   * @param register
   *          if true, register the component for on-the-fly translation
   * @return the generated JFrame
   */
  public JFrame generateJFrame(String key, boolean register) {
    JFrame newFrame = new JFrame(getTranslator().translateMessage(key));
    if (register)
      registerComponent(key, newFrame);
    return newFrame;
  }

  /**
   * Convenience wrapper for generating a new JLabel Internally invokes
   * generateJLabel(key, null)
   * 
   * @see #generateJLabel(String, Object[])
   * @param key
   *          the key for the JLabel
   * @return the generated JLabel
   */
  public JLabel generateJLabel(String key) {
    return generateJLabel(key, null, true);
  }

  /**
   * Method for generating a new JLabel
   * 
   * @param key
   *          the key for the JLabel
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * 
   * @return the generated JLabel
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JLabel generateJLabel(String key, Object[] params) {
    return generateJLabel(key, params, true);
  }

  /**
   * Method for generating a new JLabel
   * 
   * @param key
   *          the key for the JLabel
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JLabel
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JLabel generateJLabel(String key, Object[] params, boolean register) {
    String translatedMessage = getTranslator().translateMessage(key, params);
    JLabel localLabel = null;
    if (translatedMessage != null) {
      localLabel = new JLabel(translatedMessage);
    }
    if (localLabel != null && register)
      registerComponent(key, localLabel);
    return localLabel;
  }

  /**
   * Method for generating a new JList
   * 
   * @param key
   *          the key for the JList
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param labels
   *          the labels for the JComboBox
   * @param selectionMode
   *          the selection mode for the list
   * @param listener
   *          the ListSelectionListener for the events
   * @param selectedIndex
   *          the index of the default item
   * 
   * @return the generated JList
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JList generateJList(String key, Object[] params, Object[] labels,
      int selectionMode, ListSelectionListener listener, int selectedIndex) {
    return generateJList(key, params, labels, selectionMode, listener,
        selectedIndex, true);
  }

  /**
   * Method for generating a new JList
   * 
   * @param key
   *          the key for the JList
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param labels
   *          the labels for the JComboBox
   * @param selectionMode
   *          the selection mode for the list
   * @param listener
   *          the ListSelectionListener for the events
   * @param selectedIndex
   *          the index of the default item
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JList
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JList generateJList(String key, Object[] params, Object[] labels,
      int selectionMode, ListSelectionListener listener, int selectedIndex,
      boolean register) {
    JList internalList = new JList();
    internalList.setSelectionMode(selectionMode);
    String msg = getTranslator().translateMessage(key + ".toolTipText", params,
        false);
    if (msg != null && msg.length() > 0)
      internalList.setToolTipText(msg);
    // internalList.setToolTipText(getTranslator().translateMessage(
    // key + ".toolTipText", params));
    internalList.setListData(labels);
    if (selectedIndex != -1)
      internalList.setSelectedIndex(selectedIndex);
    internalList.addListSelectionListener(listener);
    if (register)
      registerComponent(key, internalList);
    return internalList;
  }

  /**
   * Convenience wrapper for generating a new JMenu Internally invokes
   * generateJMenu(key, null)
   * 
   * @see #generateJMenu(String, Object[])
   * 
   * @param key
   *          the key for the JMenu
   * 
   * @return the generated JMenu
   */
  public JMenu generateJMenu(String key) {
    return generateJMenu(key, null, true);
  }

  /**
   * Method for generating a new JMenu
   * 
   * @param key
   *          the key for the JMenu
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * 
   * @return the generated JMenu
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JMenu generateJMenu(String key, Object[] params) {
    return generateJMenu(key, params, true);
  }

  /**
   * Method for generating a new JMenu
   * 
   * @param key
   *          the key for the JMenu
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JMenu
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JMenu generateJMenu(String key, Object[] params, boolean register) {

    String translatedMessage = getTranslator().translateMessage(key + ".label",
        params);
    JMenu localMenu = null;
    if (translatedMessage != null) {
      localMenu = new JMenu(translatedMessage);
      String mnemonicString = getTranslator().translateMessage(
          key + ".mnemonic", params, true);
      localMenu.setMnemonic((mnemonicString != null) ? mnemonicString.charAt(0)
          : translatedMessage.charAt(0));
      String msg = getTranslator().translateMessage(key + ".toolTipText",
          params, false);
      if (msg != null && msg.length() > 0)
        localMenu.setToolTipText(msg);
      // localMenu.setToolTipText(getTranslator().translateMessage(
      // key + ".toolTipText", params, true));
    }
    if (localMenu != null && register)
      registerComponent(key, localMenu);
    return localMenu;
  }

  /**
   * Convenience wrapper for generating a new JMenuItem Internally invokes
   * generateJMenuItem(key, null)
   * 
   * @see #generateJMenuItem(String, Object[])
   * 
   * @param key
   *          the key for the JMenuItem
   * 
   * @return the generated JMenuItem
   */
  public JMenuItem generateJMenuItem(String key) {
    return generateJMenuItem(key, null, false, true);
  }

  /**
   * Convenience wrapper for generating a new JMenuItem Internally invokes
   * generateJMenuItem(key, null)
   * 
   * @see #generateJMenuItem(String, Object[])
   * 
   * @param key
   *          the key for the JMenuItem
   * @param useIcon
   *          if true, show an associated icon
   * 
   * @return the generated JMenuItem
   */
  public JMenuItem generateJMenuItem(String key, boolean useIcon) {
    return generateJMenuItem(key, null, true, true);
  }

  /**
   * Method for generating a new JMenuItem
   * 
   * @param key
   *          the key for the JMenuItem
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * 
   * @return the generated JMenuItem
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JMenuItem generateJMenuItem(String key, Object[] params) {
    return generateJMenuItem(key, params, true, true);
  }

  /**
   * Method for generating a new JMenuItem
   * 
   * @param key
   *          the key for the JMenuItem
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param useIcon
   *          if true, show an associated icon
   * 
   * @return the generated JMenuItem
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JMenuItem generateJMenuItem(String key, Object[] params,
      boolean useIcon) {
    return generateJMenuItem(key, params, useIcon, true);
  }

  /**
   * Method for generating a new JMenuItem
   * 
   * @param key
   *          the key for the JMenuItem
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param useIcon
   *          if true, show an associated icon
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JMenuItem
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JMenuItem generateJMenuItem(String key, Object[] params,
      boolean useIcon, boolean register) {

    String translatedMessage = getTranslator().translateMessage(key + ".label",
        params);
    JMenuItem localMenuItem = null;
    if (translatedMessage != null) {
      localMenuItem = new JMenuItem(translatedMessage);
      String mnemonicString = getTranslator().translateMessage(
          key + ".mnemonic", params, true);
      localMenuItem.setMnemonic((mnemonicString != null && mnemonicString
          .length() > 0) ? mnemonicString.charAt(0) : translatedMessage
          .charAt(0));
      String msg = getTranslator().translateMessage(key + ".toolTipText",
          params, false);
      if (msg != null && msg.length() > 0)
        localMenuItem.setToolTipText(msg);
      // localMenuItem.setToolTipText(getTranslator().translateMessage(
      // key + ".toolTipText", params, true));
      KeyStroke acc = KeyStroke.getKeyStroke(getTranslator().translateMessage(
          key + ".accelerator", null, false));
      if (acc != null)
        localMenuItem.setAccelerator(acc);
      if (useIcon) {
        String iconName = getTranslator().translateMessage(key + ".iconName",
            params);
        if (iconName != null) {
          localMenuItem.setIcon(getImageIcon(iconName));
        }
      }
    }
    if (localMenuItem != null && register)
      registerComponent(key, localMenuItem);
    return localMenuItem;
  }

  /**
   * Generates a bordered JPanel with the proper label
   * 
   * @param key
   *          the key for looking up the title of the bordered panel
   * @return a bordered JPanel with the proper title
   */
  public JPanel generateBorderedJPanel(String key) {
    return generateBorderedJPanel(key, null, true);
  }

  /**
   * Generates a bordered JPanel with the proper label
   * 
   * @param key
   *          the key for looking up the title of the bordered panel
   * @param params
   *          optional parameters (may be null) for defining the title
   * @return a bordered JPanel with the proper title
   */
  public JPanel generateBorderedJPanel(String key, Object[] params) {
    return generateBorderedJPanel(key, params, true);
  }

  /**
   * Generates a bordered JPanel with the proper label
   * 
   * @param key
   *          the key for looking up the title of the bordered panel
   * @param params
   *          optional parameters (may be null) for defining the title
   * @param register
   *          if true, register the component for on-the-fly translation
   * @return a bordered JPanel with the proper title
   */
  public JPanel generateBorderedJPanel(String key, Object[] params,
      boolean register) {

    String borderTitle = getTranslator().translateMessage(key, params);
    JPanel targetPanel = new JPanel();
    targetPanel.setBorder(new TitledBorder(null, borderTitle,
        TitledBorder.LEADING, TitledBorder.TOP));
    if (register)
      registerComponent(key, targetPanel);
    return targetPanel;
  }

  public Border generateTitledBorder(String key) {
    return generateBorder(key, null);
  }

  public Border generateBorder(String key, Object[] params) {
    Border border = BorderFactory.createTitledBorder(getTranslator()
        .translateMessage(key, params));
    return border;
  }

  /**
   * Generates a bordered JPanel with the proper label
   * 
   * @param key
   *          the key for looking up the title of the bordered panel
   * @return a bordered JPanel with the proper title
   */
  public Box generateBorderedBox(int alignment, String key) {
    return generateBorderedBox(alignment, key, null, true);
  }

  /**
   * Generates a bordered JPanel with the proper label
   * 
   * @param key
   *          the key for looking up the title of the bordered panel
   * @param params
   *          optional parameters (may be null) for defining the title
   * @return a bordered JPanel with the proper title
   */
  public Box generateBorderedBox(int alignment, String key, Object[] params) {
    return generateBorderedBox(alignment, key, params, true);
  }

  /**
   * Generates a bordered JPanel with the proper label
   * 
   * @param key
   *          the key for looking up the title of the bordered panel
   * @param params
   *          optional parameters (may be null) for defining the title
   * @param register
   *          if true, register the component for on-the-fly translation
   * @return a bordered JPanel with the proper title
   */
  public Box generateBorderedBox(int alignment, String key, Object[] params,
      boolean register) {

    String borderTitle = getTranslator().translateMessage(key, params);
    Box box = new Box(alignment);
    box.setBorder(new TitledBorder(null, borderTitle, TitledBorder.LEADING,
        TitledBorder.TOP));
    if (register)
      registerComponent(key, box);
    return box;
  }

  /**
   * Convenience wrapper for generating a new JSlider Internally invokes
   * generateJSlider(key, params, min, max, min, (max-min)/5, (max-min)/20,
   * false, listener)
   * 
   * @see #generateJSlider(String, Object[], int, int, int, int, int, boolean,
   *      ChangeListener)
   * 
   * @param key
   *          the key for the JSlider
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param min
   *          the minimum value for the JSlider
   * @param max
   *          the maximum value for the JSlider
   * @param listener
   *          the ChangeListener for the JSlider
   * 
   * @return the generated JSlider
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JSlider generateJSlider(String key, Object[] params, int min, int max,
      ChangeListener listener) {
    return generateJSlider(key, params, min, max, min, (max - min) / 5,
        (max - min) / 20, false, listener, true);
  }

  /**
   * Convenience wrapper for generating a new JSlider Internally invokes
   * generateJSlider(key, params, min, max, defaultValue, (max-min)/5,
   * (max-min)/20, false, listener)
   * 
   * @see #generateJSlider(String, Object[], int, int, int, int, int, boolean,
   *      ChangeListener)
   * 
   * @param key
   *          the key for the JSlider
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param min
   *          the minimum value for the JSlider
   * @param max
   *          the maximum value for the JSlider
   * @param defaultValue
   *          the default value for the JSlider
   * @param snapMode
   *          determines if the "snap" is on: user can only select ticks
   * @param listener
   *          the ChangeListener for the JSlider
   * 
   * @return the generated JSlider
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JSlider generateJSlider(String key, Object[] params, int min, int max,
      int defaultValue, boolean snapMode, ChangeListener listener) {
    return generateJSlider(key, params, min, max, defaultValue,
        (max - min) / 5, (max - min) / 20, snapMode, listener, true);
  }

  /**
   * Method for generating a new JSlider
   * 
   * @param key
   *          the key for the JSlider
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param min
   *          the minimum value for the JSlider
   * @param max
   *          the maximum value for the JSlider
   * @param defaultValue
   *          the default value for the JSlider
   * @param majorSpacing
   *          the spacing for 'major' ticks
   * @param minorSpacing
   *          the spacing for 'minor' ticks
   * @param snapMode
   *          determines if the "snap" is on: user can only select ticks
   * @param listener
   *          the ChangeListener for the JSlider
   * 
   * @return the generated JSlider
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JSlider generateJSlider(String key, Object[] params, int min, int max,
      int defaultValue, int majorSpacing, int minorSpacing, boolean snapMode,
      ChangeListener listener) {
    return generateJSlider(key, params, min, max, defaultValue, majorSpacing,
        minorSpacing, snapMode, listener, true);
  }

  /**
   * Method for generating a new JSlider
   * 
   * @param key
   *          the key for the JSlider
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param min
   *          the minimum value for the JSlider
   * @param max
   *          the maximum value for the JSlider
   * @param defaultValue
   *          the default value for the JSlider
   * @param majorSpacing
   *          the spacing for 'major' ticks
   * @param minorSpacing
   *          the spacing for 'minor' ticks
   * @param snapMode
   *          determines if the "snap" is on: user can only select ticks
   * @param listener
   *          the ChangeListener for the JSlider
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JSlider
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JSlider generateJSlider(String key, Object[] params, int min, int max,
      int defaultValue, int majorSpacing, int minorSpacing, boolean snapMode,
      ChangeListener listener, boolean register) {

    JSlider slider = new JSlider(min, max, defaultValue);
    slider.addChangeListener(listener);
    slider.setLabelTable(slider.createStandardLabels(majorSpacing));
    slider.setPaintTicks(true);
    slider.setMajorTickSpacing(majorSpacing);
    slider.setMinorTickSpacing(minorSpacing);
    slider.setPaintLabels(true);
    slider.setSnapToTicks(snapMode);
    String toolTipText = getTranslator().translateMessage(key + ".toolTipText",
        params);
    if (toolTipText != null && toolTipText.length() > 0) {
      slider.setToolTipText(toolTipText);
      if (register)
        registerComponent(key, slider);
    }
    return slider;
  }

  /**
   * Method for generating a new JTextField
   * 
   * @param key
   *          the key for the JTextField
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param width
   *          the width of the text field
   * @param defaultText
   *          the default text
   * 
   * @return the generated JTextField
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JTextField generateJTextField(String key, Object[] params, int width,
      String defaultText) {
    return generateJTextField(key, params, width, defaultText, true);
  }

  /**
   * Method for generating a new JTextField
   * 
   * @param key
   *          the key for the JTextField
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param width
   *          the width of the text field
   * @param defaultText
   *          the default text
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JTextField
   * 
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public JTextField generateJTextField(String key, Object[] params, int width,
      String defaultText, boolean register) {

    int theWidth = width;
    if (theWidth < 0)
      theWidth = 3;
    JTextField textField = new JTextField(theWidth);
    String msg = getTranslator().translateMessage(key + ".toolTipText", params,
        false);
    if (msg != null && msg.length() > 0)
      textField.setToolTipText(msg);
    // textField.setToolTipText(getTranslator().translateMessage(
    // key + ".toolTipText", params));
    if (defaultText != null)
      textField.setText(defaultText);
    else
      textField.setText(getTranslator().translateMessage(key + ".default",
          params));
    if (register)
      registerComponent(key, textField);
    return textField;
  }

  public void insertTranslatableTab(String tabKey, Component component,
      JTabbedPane tabbedPane) {
    insertTranslatableTab(tabKey, null, component, tabbedPane, true);
  }

  public void insertTranslatableTab(String tabKey, Object[] params,
      Component component, JTabbedPane tabbedPane) {
    insertTranslatableTab(tabKey, params, component, tabbedPane, true);
  }

  public void insertTranslatableTab(String tabKey, Object[] params,
      Component component, JTabbedPane tabbedPane, boolean register) {

    if (tabbedPane != null && component != null) {
      String label = getTranslator().translateMessage(tabKey, params);
      if (label == null)
        label = tabKey;
      tabbedPane.addTab(label, component);
      if (register)
        registerComponent(tabKey, tabbedPane);
    }
  }

  /**
   * Convenience wrapper for generating a new switchable JMenuItem Internally
   * invokes generateToggleableJMenuItem(key, params, isCheckBox, false)
   * 
   * @see #generateToggleableJMenuItem(String, Object[], boolean, boolean)
   * 
   * @param key
   *          the key for the JMenuItem
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isCheckBox
   *          if true, use Checkbox semantics, otherwise use radio button
   *          semantics
   * 
   * @return the generated JMenuItem
   */
  public JMenuItem generateToggleableJMenuItem(String key, Object[] params,
      boolean isCheckBox) {
    return generateToggleableJMenuItem(key, params, isCheckBox, false, true);
  }

  /**
   * Method for generating a new toggleable JMenuItem
   * 
   * @param key
   *          the key for the JMenuItem
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isCheckBox
   *          if true, use Checkbox semantics, otherwise use radio button
   *          semantics
   * @param isSelected
   *          if true, mark as selected.
   * 
   * @return the generated JMenuItem
   */
  public JMenuItem generateToggleableJMenuItem(String key, Object[] params,
      boolean isCheckBox, boolean isSelected) {
    return generateToggleableJMenuItem(key, params, isCheckBox, isSelected,
        true);
  }

  /**
   * Method for generating a new toggleable JMenuItem
   * 
   * @param key
   *          the key for the JMenuItem
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param isCheckBox
   *          if true, use Checkbox semantics, otherwise use radio button
   *          semantics
   * @param isSelected
   *          if true, mark as selected.
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated JMenuItem
   */
  public JMenuItem generateToggleableJMenuItem(String key, Object[] params,
      boolean isCheckBox, boolean isSelected, boolean register) {

    String translatedMessage = getTranslator().translateMessage(key + ".label",
        params);
    JMenuItem localMenuItem = null;
    if (translatedMessage != null) {
      String iconName = getTranslator().translateMessage(key + ".iconName",
          params);
      if (iconName != null) {

        if (isCheckBox)
          localMenuItem = new JCheckBoxMenuItem(translatedMessage,
              getImageIcon(iconName));
        else
          localMenuItem = new JRadioButtonMenuItem(translatedMessage,
              getImageIcon(iconName));
      } else {
        if (isCheckBox)
          localMenuItem = new JCheckBoxMenuItem(translatedMessage);
        else
          localMenuItem = new JRadioButtonMenuItem(translatedMessage);
      }
      String mnemonicString = getTranslator().translateMessage(
          key + ".mnemonic", params, true);
      localMenuItem.setMnemonic((mnemonicString != null) ? mnemonicString
          .charAt(0) : translatedMessage.charAt(0));
      String msg = getTranslator().translateMessage(key + ".toolTipText",
          params, false);
      if (msg != null && msg.length() > 0)
        localMenuItem.setToolTipText(msg);
      // localMenuItem.setToolTipText(getTranslator().translateMessage(
      // key + ".toolTipText", params, true));
      KeyStroke acc = KeyStroke.getKeyStroke(getTranslator().translateMessage(
          key + ".accelerator", null, false));
      if (acc != null)
        localMenuItem.setAccelerator(acc);
      localMenuItem.setSelected(isSelected);
    }
    if (localMenuItem != null && register)
      registerComponent(key, localMenuItem);
    return localMenuItem;
  }

  /**
   * Convenience method for adding a new element to a menu Internally invokes
   * insertMenuToolBar(key, params, invocationTargetObject, args, menu, null)
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param menu
   *          the JMenu to which the element is to be added
   * 
   * @return the generated AbtractButton
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton insertToMenu(String key, Object[] params,
      Object invocationTargetObject, Object[] args, JMenu menu) {
    return insertMenuToolBar(key, params, invocationTargetObject, args, menu,
        null, true);
  }

  /**
   * Convenience method for adding a new element to a popup menu Internally
   * invokes insertMenuToolBar(key, params, invocationTargetObject, args, menu,
   * null)
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param menu
   *          the JPopupMenu to which the element is to be added
   * 
   * @return the generated AbstractButton
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton insertToPopupMenu(String key, Object[] params,
      Object invocationTargetObject, Object[] args, JPopupMenu menu) {
    return insertMenuToolBar(key, params, invocationTargetObject, args, menu,
        null, true);
  }

  /**
   * Convenience method for adding a new element to a toolbar Internally invokes
   * insertMenuToolBar(key, params, invocationTargetObject, args, null, toolBar)
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param toolBar
   *          the JToolBar to which the element is to be added
   * 
   * @return the generated AbstractButton
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton insertToToolBar(String key, Object[] params,
      Object invocationTargetObject, Object[] args, JToolBar toolBar) {
    return insertMenuToolBar(key, params, invocationTargetObject, args, null,
        toolBar, true);
  }

  /**
   * Convenience method for adding a new element to a menu and toolbar
   * Internally invokes insertMenuToolBar(key, params, invocationTargetObject,
   * args, menu, toolBar)
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param menu
   *          the JMenu to which the element is to be added
   * @param toolBar
   *          the JToolBar to which the element is to be added
   * 
   * @return the generated AbstractButton
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton insertToMenuAndToolBar(String key, Object[] params,
      Object invocationTargetObject, Object[] args, JMenu menu, JToolBar toolBar) {
    return insertMenuToolBar(key, params, invocationTargetObject, args, menu,
        toolBar, true);
  }

  /**
   * Convenience method for adding a new element to a popup menu and toolbar
   * Internally invokes insertMenuToolBar(key, params, invocationTargetObject,
   * args, menu, toolBar)
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param menu
   *          the JPopupMenu to which the element is to be added
   * @param toolBar
   *          the JToolBar to which the element is to be added
   * 
   * @return the generated AbstractButton
   * @see translator.Translator#translateMessage(String, Object[])
   */
  public AbstractButton insertToMenuAndToolBar(String key, Object[] params,
      Object invocationTargetObject, Object[] args, JPopupMenu menu,
      JToolBar toolBar) {
    return insertMenuToolBar(key, params, invocationTargetObject, args, menu,
        toolBar, true);
  }

  /**
   * Internal Method for adding a new element to a menu and toolbar The method
   * is internal to prevent misuse with incorrect MenuElement parameters.
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param menu
   *          the MenuElement to which the element is to be added
   * @param toolBar
   *          the JToolBar to which the element is to be added
   * 
   * @return the generated AbstractButton
   * @see translator.Translator#translateMessage(String, Object[])
   * @see #insertMenuToolBar(String, Object[], Object, Object[], JMenu,
   *      JToolBar)
   */
  private AbstractButton insertMenuToolBar(String key, Object[] params,
      Object invocationTargetObject, Object[] args, MenuElement menu,
      JToolBar toolBar) {
    return insertMenuToolBar(key, params, invocationTargetObject, args, menu,
        toolBar, true);
  }

  /**
   * Internal Method for adding a new element to a menu and toolbar The method
   * is internal to prevent misuse with incorrect MenuElement parameters.
   * 
   * @param key
   *          the key for the element
   * @param params
   *          the objects used for determining the message -- see
   *          Translator.translateMessage(String, Object[])
   * @param invocationTargetObject
   *          the object on which the method defined in the resource file is to
   *          be executed
   * @param args
   *          the arguments for the method call
   * @param menu
   *          the MenuElement to which the element is to be added
   * @param toolBar
   *          the JToolBar to which the element is to be added
   * @param register
   *          if true, register the component for on-the-fly translation
   * 
   * @return the generated AbstractButton
   * @see translator.Translator#translateMessage(String, Object[])
   * @see #insertMenuToolBar(String, Object[], Object, Object[], JMenu,
   *      JToolBar)
   */
  private AbstractButton insertMenuToolBar(String key, Object[] params,
      Object invocationTargetObject, Object[] args, MenuElement menu,
      JToolBar toolBar, boolean register) {

    // first generate menu item
    String translatedMessage = getTranslator().translateMessage(key + ".label",
        params);
    String mnemonicString = getTranslator().translateMessage(key + ".mnemonic",
        params, true);
    ExtendedAction theAction = null;
    AbstractButton wrapper = null;
    if (menu != null
        && ((menu instanceof JMenu) || (menu instanceof JPopupMenu))) {
      theAction = generateAction(key, params, invocationTargetObject, args,
          false);
      if (menu instanceof JMenu)
        wrapper = ((JMenu) menu).add(theAction);
      else
        wrapper = ((JPopupMenu) menu).add(theAction);
      String msg = getTranslator().translateMessage(key + ".toolTipText",
          params, false);
      if (msg != null && msg.length() > 0)
        wrapper.setToolTipText(msg);

      // wrapper.setToolTipText(getTranslator().translateMessage(
      // key + ".toolTipText", params));
      wrapper.setMnemonic((mnemonicString != null) ? mnemonicString.charAt(0)
          : translatedMessage.charAt(0));
      if (register)
        registerComponent(key + ".item", wrapper);
    }

    if (toolBar != null) {
      // now generate JButton in JToolBar
      theAction = generateAction(key, params, invocationTargetObject, args,
          true);
      wrapper = toolBar.add(theAction);
      wrapper.setMnemonic((mnemonicString != null) ? mnemonicString.charAt(0)
          : translatedMessage.charAt(0));
      if (register)
        registerComponent(key + ".button", wrapper);
    }
    return wrapper;
  }

  @SuppressWarnings("unchecked")
  protected void registerComponent(String key, Component component) {
    if (!translatableComponents.containsKey(component))
      translatableComponents.put(component, key);
    else {
      String existingEntry = translatableComponents.get(component);
      // check if not identical and not substring
      if (!key.equals(existingEntry) && existingEntry.indexOf(key + "¤¤") == -1)
        if (existingEntry.indexOf("¤¤") == -1)
          translatableComponents.put(component, existingEntry + "¤¤" + key
              + "¤¤");
        else
          translatableComponents.put(component, existingEntry + key + "¤¤");
    }
    // if (translatableElements.containsKey(key)) {
    // Object o = translatableElements.get(key);
    // if (o instanceof Vector) { // is already a vector of entries
    // Vector v = (Vector) o;
    // if (!(v.contains(component))) // check to avoid duplex
    // v.addElement(component);
    // } else {
    // Vector<Component> v = new Vector<Component>(50, 15);
    // v.addElement((Component) o); // must be in vector, else it is lost!
    // v.addElement(component);
    // translatableElements.put(key, v);
    // }
    // } else
    // translatableElements.put(key, component);
  }

  @SuppressWarnings("unchecked")
  public void unregisterComponent(String key, Component component) {
    if (translatableComponents.containsKey(component)) {
      String existingEntry = translatableComponents.get(component);
      if (existingEntry.equals(key))
        translatableComponents.remove(component); // just delete direct match
      else if (existingEntry.indexOf(key) != -1) {// "key¤¤"
        int start = existingEntry.indexOf(key);
        int end = start + key.length();
        StringBuilder entry = new StringBuilder(256);
        entry.append(existingEntry.substring(start, end));
        if (existingEntry.length() > end + 2)
          entry.append(existingEntry.substring(end + 2));
        translatableComponents.put(component, entry.toString());
      }
    }
    // else already unregistered or never registered - we're done!
    // if (translatableElements.containsKey(key)) {
    // Object o = translatableElements.get(key);
    // if (o instanceof Vector) { // is already a vector of entries
    // Vector<Object> v = (Vector<Object>) o;
    // if (v.contains(component)) {// check to avoid duplex
    // v.remove(component);
    // } else {
    // // System.err.println("Element not found in vector for key "+key);
    // }
    // } else {
    // translatableElements.remove(key);
    // }
    // }
  }

  // ======================================================================
  // Element translation
  // ======================================================================

  /**
   * Translate all registered components using the resource file
   */
  @SuppressWarnings("unchecked")
  public void translateGUIElements() {
    Set<Component> components = translatableComponents.keySet();
    for (Component c : components)
      updateComponent(translatableComponents.get(c), c);
    // Set<String> e = translatableElements.keySet();
    // for (String key : e) {
    // // String key = e.nextElement();
    // Object currentElement = translatableElements.get(key);
    // if (currentElement != null) {
    // if (currentElement instanceof JComponent)
    // updateComponent(key, (JComponent) currentElement);
    // else if (currentElement instanceof Vector)
    // updateVectorElements(key, (Vector) currentElement);
    // }
    // }
  }

  protected void updateComponent(String key, Component component) {
    String effectiveKey = key;
    if (component != null) {
      if (component instanceof AbstractButton) {
        if (effectiveKey.endsWith(".item") || effectiveKey.endsWith(".button"))
          effectiveKey = effectiveKey.substring(0,
              effectiveKey.lastIndexOf('.'));
        AbstractButton element = (AbstractButton) component;
        String value = element.getText();
        if (value != null && value.length() > 0)
          element.setText(getTranslator().translateMessage(
              effectiveKey + ".label"));
        value = getTranslator().translateMessage(effectiveKey + ".toolTipText");
        if (value != null && value.length() > 0)
          element.setToolTipText(value);
        value = getTranslator().translateMessage(effectiveKey + ".mnemonic");
        if (value != null && value.length() > 0)
          element.setMnemonic(value.charAt(0));
        if (component instanceof JMenuItem && !(component instanceof JMenu)) {
          JMenuItem item = (JMenuItem) component;
          KeyStroke acc = KeyStroke.getKeyStroke(getTranslator()
              .translateMessage(key + ".accelerator", null, false));
          item.setAccelerator(acc);
        }
      } else if (component instanceof ExtendedActionButton) {
        ExtendedActionButton element = (ExtendedActionButton) component;
        String value = element.getText();
        if (value != null && value.length() > 0)
          element.setText(getTranslator().translateMessage(key + ".label"));
        value = getTranslator().translateMessage(key + ".toolTipText");
        if (value != null && value.length() > 0)
          element.setToolTipText(value);
        value = getTranslator().translateMessage(key + ".mnemonic");
        if (value != null && value.length() > 0)
          element.setMnemonic(value.charAt(0));
      } else if (component instanceof JLabel) {
        ((JLabel) component).setText(getTranslator().translateMessage(key));
      } else if (component instanceof JTabbedPane) {
        JTabbedPane pane = (JTabbedPane) component;
        int indexOfTab = pane.indexOfTab(key);
        String newLabel = getTranslator().translateMessage(key);
        if (newLabel == null || newLabel.length() == 0)
          newLabel = key;
        if (indexOfTab != -1)
          pane.setTitleAt(indexOfTab, newLabel);
      } else if (component instanceof Box) {
        TitledBorder b = (TitledBorder) (((Box) component).getBorder());
        b.setTitle(getTranslator().translateMessage(key));

        // box.setBorder(new TitledBorder(null, borderTitle,
        // TitledBorder.LEADING, TitledBorder.TOP));
      } else if (component instanceof JComponent) {
        ((JComponent) component).setToolTipText(getTranslator()
            .translateMessage(key + ".toolTipText", null, false));
      }
    }
  }

  protected void updateVectorElements(String key, Vector<Object> elements) {
    int nrElements = (elements == null) ? -1 : elements.size();
    Object currentElement = null;
    for (int elemPos = 0; elemPos < nrElements; elemPos++) {
      currentElement = elements.elementAt(elemPos);
      if (currentElement instanceof JComponent)
        updateComponent(key, (JComponent) currentElement);
    }
  }
}
