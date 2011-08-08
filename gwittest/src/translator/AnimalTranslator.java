package translator;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Vector;


public class AnimalTranslator {
  private static Vector<String> resourceFiles = new Vector<String>(30);
  //  private static StaticTranslator TRANSLATOR; // = new Translator();
  private static Translator translator = new Translator("AnimalResources",
  		Locale.US);
//  private static TranslatableGUIElement guiBuilder = new TranslatableGUIElement(translator); 
  public static void setTranslatorLocale(Locale targetLocale) {
//	  TRANSLATOR.setTranslatorLocale(targetLocale);
  	translator.setTranslatorLocale(targetLocale, resourceFiles);
  }
  
  public static Translator getTranslator() {
  	return translator;
  }
  /**
   * Return the current ExtendedResourceBundle for translation purposes
   *
   * @return the current resource bundle
   */
  public static ExtendedResourceManagement getResourceBundle() {
    return translator.getResourceBundle();
  }


  /**
   * Convenience wrapper the translated message of key messageKey.
   * Internally invokes generateMessage(messageKey, null);
   *
   * @see #translateMessage(String, Object[], boolean)
   * @param messageKey the key of the message to be displayed
   * @return the translated String object
   */
  public static String translateMessage(String messageKey) {
    return translateMessage(messageKey, null, true);
  }
  
  /**
   * Convenience wrapper the translated message of key messageKey.
   * Internally invokes generateMessage(messageKey, null);
   *
   * @see #translateMessage(String, Object[], boolean)
   * @param messageKey the key of the message to be displayed
   * @return the translated String object
   */
  public static String translateMessage(String messageKey, Object o) {
    return translateMessage(messageKey, new Object[] { o }, true);
  }


  /**
   *  Return the translated message of key messageKey using the message params
   * Internally invokes generateMessage(messageKey, null);
   *
   * @see #translateMessage(String, Object[], boolean)
   *
   * @param messageKey the key of the message to be displayed
   * @param messageParams the values to be substituted into the message.
   * @return the translated String object
   */
  public static String translateMessage(String messageKey,
    Object[] messageParams) {
    return translateMessage(messageKey, messageParams, true);
  }

  /**
   *  Return the translated message of key messageKey using the message params
   *
   * @param messageKey the key of the message to be displayed
   * @param messageParams the values to be substituted into the message.
   * @param warnOnError if true, log warnings due to invalid accesses or
   * unavailable keys.
   * @return the translated message as a String object
   */
  public static String translateMessage(String messageKey,
    Object[] messageParams, boolean warnOnError) {
  	return translator.translateMessage(messageKey, messageParams, warnOnError);
  }
  
  public static TranslatableGUIElement getGUIBuilder() {
  	return translator.getGenerator();
  }
  
  public static void addResource(String filename) throws FileNotFoundException {
    getResourceBundle().addPropertyResource(filename);
    resourceFiles.add(filename.substring(0, filename.lastIndexOf('.')));
  }
}
