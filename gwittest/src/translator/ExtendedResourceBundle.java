package translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Vector;

/**
 * A special type of PropertyResourceBundle support
 * 
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 *         roessling@acm.org</a>)
 */
public class ExtendedResourceBundle {
  public static final String ASCII_FORMAT = "ascii";

  public static final String PROPERTY_FORMAT = "properties";

  private Translator translator = null;

  /**
   * The property resource bundle used for storing the current resources
   */
  PropertyResourceBundle bundle = null;

  Vector<ExtendedResourceBundle> componentBundles = new Vector<ExtendedResourceBundle>(22);

  /**
   * Generate a new bundle from the given name and language code
   * 
   * @param filename
   *          the file name
   */
  public ExtendedResourceBundle(String filename) {
    this(filename, PROPERTY_FORMAT);
  }

  /**
   * Generate a new bundle from the given name and language code
   * 
   * @param filename
   *          the file name
   * @param formatName
   *          the name of the chosen format
   */
  public ExtendedResourceBundle(String filename, String formatName) {
    try {
      InputStream in = ResourceLocator.getResourceLocator().getResourceStream(
          filename);
      if (PROPERTY_FORMAT.equalsIgnoreCase(formatName))
        bundle = new PropertyResourceBundle(new BufferedReader(new InputStreamReader(in))); //new BufferedInputStream(in));
      if (bundle != null) {
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
          String key = keys.nextElement();
          if (key.startsWith("@import")) {
            ExtendedResourceBundle newBundle = 
              new ExtendedResourceBundle(bundle.getString(key));
            componentBundles.add(newBundle);
          }
        }
      }
//      if (bundle != null && bundle.handleGetObject("@import") != null) {
//        backupBundle = new ExtendedResourceBundle(bundle.getString("@import"));
//      }
      // System.err.println("loaded in: " +filename);
      // insert other format support here...
    } catch (IOException ioException) {
//      if (translator != null)
        Debug.printlnMessage(getTranslator().translateMessage(
            "verboseException",
            new Object[] { "(String) [constructor]", getClass().getName(),
                ioException.getMessage(), "(no information available)",
                ioException.getClass().getName() }));
//      else
//        System.err
//            .println("Sorry, but there was a problem in accessing the resource "
//                + filename);
    } catch (IllegalArgumentException illArg) {
      System.err.println(illArg.getMessage());
    }
  }

  // ======================================================================
  // Attribute access
  // ======================================================================

  /**
   * Retrieve the keys of this resource
   * 
   * @return a String[] of all keys in this resource
   */
  public String[] getKeys() {
    Vector<String> v = new Vector<String>(128);
    String key = null;
    if (componentBundles != null) {
      for (ExtendedResourceBundle partBundle : componentBundles) {
        String[] defaultKeys = partBundle.getKeys();
        for (String localKey : defaultKeys)
          v.addElement(localKey);
      }
    }
    Enumeration<String> e = bundle.getKeys();
    while (e.hasMoreElements()) {
      key = e.nextElement();
      if (!v.contains(key))
        v.addElement(key);
    }
    String[] result = new String[v.size()];
    for (int i = 0; i < result.length; i++)
      result[i] = v.elementAt(i);
    return result;
  }

  /**
   * Convenvience wrapper for retrieving the message for key 'key' Internally
   * invokes getMessage(key, true)
   * 
   * @param key
   *          the key of the message to retrieve
   * @return the retrieved message or null, if no message was found
   * @see #getMessage(String, boolean)
   */
  public String getMessage(String key) {
    return getMessage(key, true);
  }

  /**
   * Method for retrieving the message for key 'key'
   * 
   * @param key
   *          the key of the message to retrieve
   * @param warnOnError
   *          if true, display a warning on System.out if no appropriate
   *          resource was found.
   * @return the retrieved message or null, if no message was found
   */
  public String getMessage(String key, boolean warnOnError) {
    String message = null;
    try {
      if (bundle != null && bundle.handleGetObject(key) != null)
        message = bundle.getString(key);
      if (message == null && componentBundles != null) {
        for (ExtendedResourceBundle additionalBundle : componentBundles) {
          message = additionalBundle.getMessage(key, false);
          if (message != null)
            break;
        }
      }
    } catch (MissingResourceException missingResourceException) {
      if (bundle != null && warnOnError
          && !("noSuchKeyException".equalsIgnoreCase(key)))
        Debug.printlnMessage("A:" +getTranslator().translateMessage(
            "noSuchKeyException", new Object[] { key }));
      // else
      // System.err.println("No such key: " + key);
    }
    return message;
  }

  /**
   * retrieves the actual Translator instance to be used
   */
  public Translator getTranslator() {
    if (translator == null)
      translator = new Translator("en", Locale.US);
    return translator;
  }

  /**
   * Print the properties stored in the bundle to System.out
   */
  public void printProperties() {
    printProperties(System.out);
  }

  /**
   * Print the properties stored in the bundle to System.out
   */
  public void printProperties(PrintStream outputStream) {
    if (bundle == null)
      return;

    String[] keys = getKeys();
    String key = null;
    if (keys != null) {
      int i, nrKeys = keys.length;
      for (i = 0; i < nrKeys; i++) {
        key = keys[i];
        outputStream.println("key: '" + key + "' entry: '" + getMessage(key)
            + "'");
        // + bundle.getObject(key) + "'");
      }
    }
  }

  /**
   * assigns the current Translator for this resource bundle
   * 
   * @param trans
   *          the translator to be used. If null, a default translator is used.
   */
  public void setTranslator(Translator trans) {
    if (trans == null)
      translator = new Translator();
    else
      translator = trans;
  }
}
