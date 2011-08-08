package translator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.TreeMap;

/**
 * A special type of PropertyResourceBundle support
 * 
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 *         roessling@acm.org</a>)
 */
public class ExtendedResourceManagement {
  public static final String      ASCII_FORMAT    = "ascii";

  public static final String      PROPERTY_FORMAT = "properties";

  private Translator              translator      = null;

  /**
   * The property resource bundle used for storing the current resources
   */
  private TreeMap<String, String> resources       = null;

  /**
   * Generate a new bundle from the given name and language code
   * 
   * @param filename
   *          the file name
   */
  public ExtendedResourceManagement(String filename) {
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
  public ExtendedResourceManagement(String filename, String formatName) {
    resources = new TreeMap<String, String>();
    PropertyResourceBundle bundle = null;
    try {
      bundle = loadBundle(filename, formatName);
    } catch (FileNotFoundException fnfe) {
      System.err.println(fnfe.getMessage());
    }
    addKeys(bundle, formatName);
    // check if other resources have been listed!
    // if (filename.indexOf('.') > 0) {
    // String targetLocaleKey = filename.substring(filename.lastIndexOf('.')+1);
    // String localKey = null;
    // for (String resourceKey : resources) {
    // localKey = resourceKey.substring(0, resourceKey.indexOf('.'));
    // try {
    // System.err.println("trying to read resource '" +localKey +"."
    // +targetLocaleKey);
    // addPropertyResource(localKey +"." +targetLocaleKey);
    // } catch(FileNotFoundException fnfe) {
    // System.err.println("File not found: " +localKey +"."+targetLocaleKey);
    // }
    // }
    // }

    // PropertyResourceBundle bundle = null;
    // try {
    // InputStream in = ResourceLocator.getResourceLocator().getResourceStream(
    // filename);
    // if (PROPERTY_FORMAT.equalsIgnoreCase(formatName))
    // bundle = new PropertyResourceBundle(new BufferedInputStream(in));
    // } catch (IOException ioException) {
    // Debug.printlnMessage(getTranslator().translateMessage(
    // "verboseException",
    // new Object[] { "(String) [constructor]", getClass().getName(),
    // ioException.getMessage(), "(no information available)",
    // ioException.getClass().getName() }));
    // } catch (IllegalArgumentException illArg) {
    // System.err.println(illArg.getMessage());
    // }
    // if (bundle != null) {
    // // add the keys
    // addKeys(bundle, formatName);
    // }
  }

  private PropertyResourceBundle loadBundle(String filename, String formatName)
      throws FileNotFoundException {
    PropertyResourceBundle bundle = null;
    try {
      InputStream in = ResourceLocator.getResourceLocator().getResourceStream(
          filename);
      // TODO GR
      InputStreamReader isr = new InputStreamReader(in); // , "UTF8");
      if (PROPERTY_FORMAT.equalsIgnoreCase(formatName))
        bundle = new PropertyResourceBundle(new BufferedReader(isr));
      // if (PROPERTY_FORMAT.equalsIgnoreCase(formatName))
      // // bundle = new PropertyResourceBundle(new BufferedInputStream(in));
      // //TODO Check how to best support Unicode!
      // bundle = new PropertyResourceBundle(new BufferedReader(new
      // InputStreamReader(in)));
    } catch (IOException ioException) {
      throw new FileNotFoundException("Resource file '" + filename
          + "' could not be read.");
    } catch (IllegalArgumentException illArg) {
      System.err.println(illArg.getMessage());
    }
    return bundle;
  }

  private void addKeys(PropertyResourceBundle bundle, final String formatName) {
    if (bundle == null)
      return;

    Enumeration<String> keys = bundle.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      if (!key.startsWith("@import")) {
        // copy value
        if (resources.containsKey(key)
            && !resources.get(key).equals(bundle.getString(key)))
          System.err.println("The key '" + key + "' already in list: '"
              + resources.get(key) + "', ignoring new value '"
              + bundle.getString(key));
        else
          try {
            String value = bundle.getString(key);
            byte[] bytes = value.getBytes("UTF-8");
            String result = new String(bytes, "UTF-8");
            resources.put(key, result);
          } catch (UnsupportedEncodingException e) {
            resources.put(key, bundle.getString(key));
          }
      } else {
        // load new bundle
        PropertyResourceBundle newBundle = null;
        try {
          newBundle = loadBundle(bundle.getString(key), formatName);
        } catch (FileNotFoundException fnfe) {
          System.err.println(fnfe.getMessage());
        }
        // add new keys
        addKeys(newBundle, formatName);
      }
    }
  }

  protected void addPropertyResource(String filename)
      throws FileNotFoundException {
    PropertyResourceBundle bundle = loadBundle(filename,
        ExtendedResourceManagement.PROPERTY_FORMAT);
    // if (bundle != null) {
    // Enumeration<String> newKeys = bundle.getKeys();
    // while (newKeys.hasMoreElements()) {
    // System.err.println(newKeys.nextElement());
    // }
    // }
    addKeys(bundle, ExtendedResourceManagement.PROPERTY_FORMAT);
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
    Set<String> keySet = resources.keySet();
    String[] stringKeys = new String[keySet.size()];
    keySet.toArray(stringKeys);
    return stringKeys;
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
    if (resources == null)
      return " ";
    
    String message = null;
    if (resources != null && resources.containsKey(key))
      message = resources.get(key);
    
    if (message != null)
      return message;
    
    if (warnOnError) {
      if (message == null && resources.containsKey("noSuchKeyException"))
        Debug.printlnMessage(getTranslator().translateMessage(
            "noSuchKeyException", new Object[] { key })
            + "@"
            + getTranslator().resourceBaseName
            + "."
            + getTranslator().getCurrentLocale());
      else
        System.err.println("Missing key: " + key + " @ " + getTranslator().resourceBaseName
            + "."  + getTranslator().getCurrentLocale());
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
    if (resources == null)
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
