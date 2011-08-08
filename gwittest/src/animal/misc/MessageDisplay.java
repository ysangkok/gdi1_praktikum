/*
 * Created on 22.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package animal.misc;

import java.awt.Toolkit;
import java.io.PrintStream;

import javax.swing.JTextArea;

import translator.AnimalTranslator;

/**
 * This class displays output messages for debug, information, or error reporting
 * 
 * @author Guido
 * @version 0.7 20050422
 */
public class MessageDisplay {
	/**
	 * constant for <code>errorMsg</code>. The message to be displayed
	 * indicates an error in the environment, which can be fixed by adapting
	 * paths etc. without having to work with the sources.
	 */
	public static final int CONFIG_ERROR = 8;

	/**
	 * constant for <code>errorMsg</code>. The message to be displayed
	 * indicates a debug message.
	 */
	public static final int DEBUG_MESSAGE = 1;

	/**
	 * constant for <code>errorMsg</code>. The message to be displayed
	 * indicates an information for the user or an "error", which can be fixed
	 * by Animal itself, e.g. the properties file does not exist.
	 */
	public static final int INFO = 2;

	/**
	 * constant for <code>errorMsg</code>. The message to be displayed
	 * indicates an error in the implementation which has to be fixed by editing
	 * the sources and recompiling.
	 */
	public static final int PROGRAM_ERROR = 16;

	public static final String LINE_FEED = "\n";

	
	/**
	 * constant for <code>errorMsg</code>. The message to be displayed
	 * indicates an error which is due to a wrong user input like an
	 * inaccessible file path etc.
	 */
	public static final int RUN_ERROR = 4;

	/**
	 * determines if DEBUG_MODE is used. If true, all incoming messages
	 * will be displayed. Otherwise, only messages of at least "INFO" level
	 * are displayed.
	 */
	private static boolean DEBUG_MODE = true; //Boolean.getBoolean("debug");

	/**
	 * the logging instance used for logging messages
	 */
	private static DebugLogger LOGGER = null;

  /**
   * The text area on which all output is written
   */
	private static JTextArea OUTPUT_AREA;
	
	
	/**
	 * Creates a new message display instance
	 * 
	 * @param outputDisplayArea the text area on which output is generated
	 * @param debugMode determines if debug mesasges are shown or not.
	 */
	public static void initialize(JTextArea outputDisplayArea, boolean debugMode,
				XProperties props) {
		OUTPUT_AREA = outputDisplayArea;
		DEBUG_MODE = debugMode;
		instantiateLogger(props);
	}

	
	/**
	 * displays a message in the output TextArea of Animal's main window.
	 * 
	 * @param msg
	 *            the message to be displayed
	 * @param priority
	 *            one of <code>PROGRAM_ERROR, CONFIG_ERROR, RUN_ERROR, INFO
	 *            </code><br>
	 *            <code>PROGRAM_ERRORS</code> are only displayed if the debug
	 *            flag is set, <br>
	 *            <code>CONFIG_ERRORS</code> and <code>PROGRAM_ERRORS</code>
	 *            are highlighted and a beep is emitted.
	 */
	public static void errorMsg(String msg, int priority) {
		errorMsg(msg, priority, true);
	}

	/**
	 * Display the message as an error message, formatted with the parameters
	 * Invokes the errorMsg method using the extracted formatted message as a
	 * RUN_ERROR.
	 * 
	 * @see #errorMsg(String, int)
	 * @param key
	 *            the key of the message to display
	 * @param params
	 *            the parameters needed for formatting the message
	 */
	public static void errorMsg(String key, Object[] params) {
		errorMsg(AnimalTranslator.translateMessage(key, params), 
				MessageDisplay.RUN_ERROR);
	}

	/**
	 * Display the message as an error message, formatted with the parameters
	 * 
	 * @see #errorMsg(String, int)
	 * @param key
	 *            the key of the message to display
	 * @param params
	 *            the parameters needed for formatting the message
	 * @param priority
	 *            the priority of the message
	 */
	public static void errorMsg(String key, Object params, int priority) {
		errorMsg(AnimalTranslator.translateMessage(key, 
				new Object[] { params }), priority);
	}

	/**
	 * Display the message as an error message, formatted with the parameters
	 * 
	 * @see #errorMsg(String, int)
	 * @param key
	 *            the key of the message to display
	 * @param params
	 *            the parameters needed for formatting the message
	 * @param priority
	 *            the priority of the message
	 */
	public static void errorMsg(String key, Object[] params, int priority) {
		errorMsg(AnimalTranslator.translateMessage(key, params), priority);
	}

	
	/**
	 * displays a message in the output TextArea of Animal's main window.
	 * 
	 * @param msg
	 *            the message to be displayed
	 * @param priority
	 *            one of <code>PROGRAM_ERROR, CONFIG_ERROR, RUN_ERROR, INFO
	 *            </code><br>
	 *            <code>PROGRAM_ERRORS</code> are only displayed if the debug
	 *            flag is set, <br>
	 *            <code>CONFIG_ERRORS</code> and <code>PROGRAM_ERRORS</code>
	 *            are highlighted and a beep is emitted.
	 * @param appendNewLine
	 *            if true, adds a newline character at the end of the message
	 */
	public static void errorMsg(String msg, int priority, boolean appendNewLine) {
		// always display INFO-messages. Display DEBUG messages only if
		// the debug flag is set.
	  String theMsg = msg;
		if ((priority > DEBUG_MESSAGE) || DEBUG_MODE) {
			if (OUTPUT_AREA != null) {
				// only show error messages and INFOs; DEBUG only if DEBUG_MODE
				// is "on"
				// if error messages, "emphasize" them
				if (priority >= CONFIG_ERROR) {
				  theMsg = "!!!" + theMsg + "!!!";
					Toolkit.getDefaultToolkit().beep();
				}
				OUTPUT_AREA.append(theMsg);
				if (appendNewLine) {
					OUTPUT_AREA.append(MessageDisplay.LINE_FEED);
				}

				OUTPUT_AREA.setCaretPosition(OUTPUT_AREA.getDocument()
						.getLength());
			} else { // this should not happen, since the output exists!

				PrintStream out = (priority > INFO) ? System.err : System.out;

				if (appendNewLine) {
					out.println("Animal: " + theMsg);
				} else {
					out.print(theMsg);
				}
			}

			if (LOGGER != null) {
				LOGGER.logMessage(theMsg, priority); //DebugLogger.INFO);
			}
		}
	}

	/**
	 * send a message to our current logger
	 * 
	 * @param msg the message to be logged
	 */
	public static void addDebugMessage(String msg) {
		if (LOGGER != null)
			LOGGER.logMessage(msg, DEBUG_MESSAGE);
	}
	
  /**
   * displays an information message in the output TextArea in Animal's
   * main window's center. This is just a shortcut for
   * <code>errorMsg(msg, INFO)</code>.
   *
   * @see #errorMsg(String, int)
   * @param msg the text to be displayed
   */
  public static void message(String msg) {
  	errorMsg(msg, INFO);
  }

  
  /**
   * displays an information message in the output TextArea in Animal's
   * main window's center. This is just a shortcut for
   * <code>errorMsg(msg, INFO)</code>.
   *
   * @see #errorMsg(String, int)
   * @param msg the text to be displayed
   */
  public static void message(String msg, Object param) {
  	errorMsg(msg, new Object[] { param }, INFO);
  }
  
  /**
   * displays an information message in the output TextArea in Animal's
   * main window's center. This is just a shortcut for
   * <code>errorMsg(msg, INFO)</code>.
   *
   * @see #errorMsg(String, int)
   * @param msg the text to be displayed
   */
  public static void message(String msg, Object[] params) {
  	errorMsg(msg, params, INFO);
  }

	
	/**
	 * create a login instance based on the settings in the properties
	 * 
	 * @param props the properties describing user settings
	 */
	@SuppressWarnings("unchecked")
	private static void instantiateLogger(XProperties props) {
		String defaultLogger = props.getProperty("animal.logger",
				"animal.misc.DefaultLogger");

		try {
//		  System.err.println("try to load in logging class " + defaultLogger);
			Class<DebugLogger> c = 
				(Class<DebugLogger>)Class.forName(defaultLogger);
//			System.err.println("Logger is now " +c +": instantiating...");
			LOGGER = c.newInstance();
//			System.err.println("created OK; LOGGER: " +LOGGER);
			LOGGER.init();
//			System.err.println("Logger initialized.");
		} catch (ClassNotFoundException e) {
			MessageDisplay.errorMsg("Class " + e.getMessage() + " not found!",
					MessageDisplay.CONFIG_ERROR);
		} catch (InstantiationException e) {
			MessageDisplay.errorMsg("Error instantiating " + e.getMessage(),
					MessageDisplay.PROGRAM_ERROR);
		} catch (IllegalAccessException e) {
			MessageDisplay.errorMsg("Illegal Access to " + e.getMessage()
					+ ". Perhaps no public constructor.", 
					MessageDisplay.PROGRAM_ERROR);
		} catch (IllegalArgumentException e) {
			MessageDisplay.errorMsg("Illegal Class " + e.getMessage(),
					MessageDisplay.PROGRAM_ERROR);
		}
	}
}