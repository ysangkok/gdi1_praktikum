package translator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Vector;


/**
 * A special type of PropertyResourceBundle support
 *
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 * roessling@acm.org</a>)
 */
public class StaticExtendedResourceBundle {
  /**
   * the default name for the property format
   */
  public static final String PROPERTY_FORMAT = "properties";

  /**
   * the default file format
   */
  public static final String ASCII_FORMAT = "ascii";

  /**
   * The property resource bundle used for storing the current resources
   */
  PropertyResourceBundle bundle = null;

  /**
   * Generate a new bundle from the given name and language code
   *
   * @param filename the file name
   */
  public StaticExtendedResourceBundle(String filename) {
    this(filename, PROPERTY_FORMAT);
  }

  /**
   * Generate a new bundle from the given name and language code
   *
   * @param filename the file name
   * @param formatName the name of the format file
   */
  public StaticExtendedResourceBundle(String filename, String formatName) {
    try {
      InputStream in = ResourceLocator.getResourceLocator().getResourceStream(filename);
      if (PROPERTY_FORMAT.equalsIgnoreCase(formatName)) {
        bundle = new PropertyResourceBundle(new BufferedInputStream(in));
      }

      // insert other format support here...
    }
    catch (IOException ioException) {
      Debug.printlnMessage(AnimalTranslator.translateMessage("verboseException",
          new Object[] {
            "(String) [constructor]", getClass().getName(),
            ioException.getMessage(), "(no information available)",
            ioException.getClass().getName()
          }));
    }
  }

  // ======================================================================
  //                        Attribute access
  // ======================================================================

  /**
   * Retrieve the keys of this resource
   *
   * @return a String[] of all keys in this resource
   */
  public String[] getKeys() {
    Vector<String> v = new Vector<String>(128);
    Enumeration<String> e = bundle.getKeys();

    while (e.hasMoreElements())
      v.addElement(e.nextElement());

    String[] result = new String[v.size()];

    for (int i = 0; i < result.length; i++)
      result[i] = v.elementAt(i);

    return result;
  }

  /**
   * Convenvience wrapper for retrieving the message for key 'key'
   * Internally invokes getMessage(key, true)
   *
   * @param key the key of the message to retrieve
   * @return the retrieved message or null, if no message was found
   * @see #getMessage(String, boolean)
   */
  public String getMessage(String key) {
    return getMessage(key, true);
  }

  /**
   * Method for retrieving the message for key 'key'
   *
   * @param key the key of the message to retrieve
   * @param warnOnError if true, display a warning on System.out if
   * no appropriate resource was found.
   * @return the retrieved message or null, if no message was found
   */
  public String getMessage(String key, boolean warnOnError) {
    try {
      if (bundle != null) {
        return bundle.getString(key);
      }
    }
    catch (MissingResourceException missingResourceException) {
      if (warnOnError) {
        Debug.printlnMessage(AnimalTranslator.translateMessage("noSuchKeyException", 
        		new Object[] { key }));
      }
    }

    return null;
  }

  /**
   * Print the properties stored in the bundle to System.out
   */
  public void printProperties() {
    printProperties(System.out);
  }

  /**
   * Print the properties stored in the bundle to the given output stream
   *
   * @param outputStream the OutputStream on which the results
   * are to be written
   */
  public void printProperties(PrintStream outputStream) {
    if (bundle == null) {
      return;
    }

    String[] keys = getKeys();
    String key = null;

    if (keys != null) {
      int i;
      int nrKeys = keys.length;

      for (i = 0; i < nrKeys; i++) {
        key = keys[i];
        outputStream.println("key: '" + key + "' entry: '" +
          bundle.getObject(key) + "'");
      }
    }
  }
}
