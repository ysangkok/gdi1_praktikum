package translator;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import animal.misc.MessageDisplay;

/**
 * Provides internationalization by translating messages. Requires an
 * appropriate resource file containing the message translations.
 * 
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 *         roessling@acm.org</a>)
 */
public class Translator {
	// ======================================================================
	// Attributes
	// ======================================================================

	/**
	 * The current locale, responsible for element translation
	 */
	public final static Locale DEFAULT_LOCALE = new Locale("en", "US");

	/**
	 * The current locale, responsible for element translation
	 */
	private Locale currentLocale = new Locale("en", "US");

	/**
	 * The object used for formatting messages
	 */
	private MessageFormat messageFormat = new MessageFormat("");

	/**
	 * The base name of the resource to load
	 */
	String resourceBaseName = "AnimalResources";

	/**
	 * Store all language resources we know about
	 */
	private Hashtable<String, ExtendedResourceManagement> resources =
		new Hashtable<String, ExtendedResourceManagement>(17);

    private ExtendedResourceManagement currentBundle = null;
    
	private TranslatableGUIElement generator = null;

	/**
	 * Store all unknown resource keywords
	 */
	private Hashtable<String, String> unknownResources = new Hashtable<String, String>(
			41);

	// ======================================================================
	// Attribute Access / Setting
	// ======================================================================

	/**
	 * Retrieve the current locale. May be used to check whether elements have to
	 * be translated, e.g. this is not needed if getCurrentLocale().equals(new
	 * Locale)
	 * 
	 * @return the current locale object
	 */
	public Locale getCurrentLocale() {
		return currentLocale;
	}

	/**
	 * returns the current TranslatableGUIElement instance for this Translator
	 * 
	 * @return the current TranslatableGUIElement handler
	 */
	public TranslatableGUIElement getGenerator() {
		if (generator == null)
			generator = new TranslatableGUIElement(this);
		return generator;
	}

	/**
	 * Return the current ExtendedResourceBundle for translation purposes
	 * 
	 * @return the current resource bundle
	 */
	public ExtendedResourceManagement getResourceBundle() {
      // TODO Check if this ok
      if (currentBundle == null)
        currentBundle = resources.get(currentLocale.toString());
      return currentBundle;
//		return resources.get(currentLocale.toString());
	}

	/**
	 * Lists the unknown resources for all known locales to the standard output
	 */
	public void listUnknownResources() {
		StringBuilder buffer = new StringBuilder(5000);
		Enumeration<String> keys = unknownResources.keys();
		if (keys.hasMoreElements())
			buffer.append("Unknown resources in format 'resource name -- locale':").append(MessageDisplay.LINE_FEED);
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			buffer.append(key).append(": ").append(unknownResources.get(key));
			buffer.append("\n");
		}
		if (buffer.length() > 0)
			System.out.print(buffer.toString());
	}

	/**
	 * Set the current locale to the one passed in This will also translate all
	 * GUI components if the state of the Translator is 'initialized'
	 * 
	 * @param targetLocale
	 *          the new locale for the application. If null, uses Locale.US
	 *          instead
	 */
	public void setTranslatorLocale(Locale targetLocale) {
      setTranslatorLocale(targetLocale, new Vector<String>(1));
/*
      if (targetLocale != null) {
	    messageFormat.setLocale(targetLocale);
	    String localeCode = targetLocale.toString();
	    if (!resources.containsKey(localeCode)) {
          ExtendedResourceManagement resourceBundle = new ExtendedResourceManagement(
              resourceBaseName + "." + localeCode);
//	      ExtendedResourceBundle resourceBundle = new ExtendedResourceBundle(
//	          resourceBaseName + "." + localeCode);
	      resourceBundle.setTranslator(this);
	      resources.put(localeCode, resourceBundle);
	    }
	    currentLocale = targetLocale;
	    getGenerator().translateGUIElements();
	  }
      */
	}

    /**
     * Set the current locale to the one passed in This will also translate all
     * GUI components if the state of the Translator is 'initialized'
     * 
     * @param targetLocale
     *          the new locale for the application. If null, uses Locale.US
     *          instead
     * @param additionalResources the file names (without ".xx_YY") for additional
     * language resources to be read in
     */
    public void setTranslatorLocale(Locale targetLocale, 
        Vector<String> additionalResources) {
//      System.err.println("set locale to " + targetLocale.toString() +", #add: " +additionalResources.size());
      if (targetLocale != null) {
        ExtendedResourceManagement resourceBundle = null;
        messageFormat.setLocale(targetLocale);
        String localeCode = targetLocale.toString();
//        System.err.println("already 'in' for " + localeCode +"? " + resources.containsKey(localeCode));
//        if (!resources.containsKey(localeCode)) {
          resourceBundle = new ExtendedResourceManagement(
              resourceBaseName + "." +localeCode);
          resourceBundle.setTranslator(this);
          for (String key: additionalResources) {
//            System.err.println("Trying to add resource: " + key +'.' +localeCode);
            try {
              resourceBundle.addPropertyResource(key+'.'+localeCode);
            } catch (FileNotFoundException fnfe) {
              System.err.println("Adding resource ' "+key+'.'+localeCode +"' failed...");
            }
//          }
          resources.put(localeCode, resourceBundle);
        }
        currentLocale = targetLocale;
        currentBundle = resourceBundle;
//        System.err.println(translateMessage("GenericEditor.nameBL"));
        getGenerator().translateGUIElements();
      }
    }

	public void setResourceBaseName(String resourceName) {
		resourceBaseName = resourceName;
	}

	// ======================================================================
	// Message and Element Translation
	// ======================================================================

	public Translator() {
		// this("resources", new Locale("en", "US"));
		try {
			throw new IllegalArgumentException(
					"Please do not use the empty constructor for Translator instances!");
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public Translator(String resourceName, Locale targetLocale) {
	  setResourceBaseName(resourceName);
	  setTranslatorLocale(targetLocale);
	}

	/**
	 * Convenience wrapper the translated message of key messageKey. Internally
	 * invokes generateMessage(messageKey, null);
	 * 
	 * @see #translateMessage(String, Object[], boolean)
	 * @param messageKey
	 *          the key of the message to be displayed
	 */
	public String translateMessageWithoutParameterExpansion(String messageKey) {
		String messageSource = getResourceBundle().getMessage(messageKey,
				true);
		if (messageSource != null) {
			return messageSource;
		}
		if (unknownResources.containsKey(messageKey)) {
			String entry = unknownResources.get(messageKey);
			if (entry.indexOf(currentLocale.getCountry()) == -1)
				unknownResources.put(messageKey, 
						unknownResources.get(messageKey)
						+ ", " + currentLocale.getCountry());
		} else
			unknownResources.put(messageKey, currentLocale.getCountry());
		return "Invalid Message Key '" + messageKey + "'@"
		  +resourceBaseName +currentLocale.toString();
	}

	/**
	 * Convenience wrapper the translated message of key messageKey. Internally
	 * invokes generateMessage(messageKey, null);
	 * 
	 * @see #translateMessage(String, Object[], boolean)
	 * @param messageKey
	 *          the key of the message to be displayed
	 */
	public String translateMessage(String messageKey) {
		return translateMessage(messageKey, null, true);
	}

	/**
	 * Return the translated message of key messageKey using the message params
	 * Internally invokes generateMessage(messageKey, null);
	 * 
	 * @see #translateMessage(String, Object[], boolean)
	 * 
	 * @param messageKey
	 *          the key of the message to be displayed
	 * @param messageParams
	 *          the values to be substituted into the message.
	 */
	public String translateMessage(String messageKey, Object[] messageParams) {
		return translateMessage(messageKey, messageParams, true);
	}

	/**
	 * Return the translated message of key messageKey using the message params
	 * Internally invokes generateMessage(messageKey, null);
	 * 
	 * @see #translateMessage(String, Object[], boolean)
	 * 
	 * @param messageKey
	 *          the key of the message to be displayed
	 * @param params
	 *          the values to be substituted into the message.
	 */
	public String translateMessage(String messageKey, String... params) {
		return translateMessage(messageKey, params, true);
	}

	/**
	 * Return the translated message of key messageKey using the message params
	 * 
	 * @param messageKey
	 *          the key of the message to be displayed
	 * @param messageParams
	 *          the values to be substituted into the message.
	 * @param warnOnError
	 *          if true, log warnings due to invalid accesses or unavailable keys.
	 */
	public String translateMessage(String messageKey, Object[] messageParams,
			boolean warnOnError) {
		String messageSource = getResourceBundle().getMessage(messageKey,
				warnOnError);
		if (messageSource != null) {
			try {
				messageFormat.applyPattern(messageSource);
			} catch(IllegalArgumentException illegal) {
				System.err.println(illegal.toString() +" for key '" +messageKey +"'");
			}
			return messageFormat.format(messageParams);
		}
		if (unknownResources.containsKey(messageKey)) {
			String entry = unknownResources.get(messageKey);
			if (entry.indexOf(currentLocale.getCountry()) == -1)
				unknownResources.put(messageKey, 
						unknownResources.get(messageKey)
						+ ", " + currentLocale.getCountry());
		} else
			unknownResources.put(messageKey, currentLocale.getCountry());
		return "Invalid Message Key '" + messageKey + "'";
	}
}
