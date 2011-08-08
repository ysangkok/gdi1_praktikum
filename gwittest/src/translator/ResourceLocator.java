package translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This class encapsulates the loading of (almost) arbitrary resources. Resource
 * loading is performed by checking the following approaches:
 * 
 * <ol>
 * <li>Loading using <code>Class.getResourceAsStream()</code></li>
 * <li>Loading using <code>ClassLoader.getSystemResourceAsStream()</code></li>
 * <li>Loading using the <em>codebase</em> as a URL, if it Applet mode</li>
 * <li>Loading using a <code>FileInputStream</code> (not possible for Applets
 * and WebStart applications)</li>
 * </ol>
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 0.7 10.06.2005
 */
public class ResourceLocator {
	private static ResourceLocator locator = null;

	/**
	 * (empty) default constructor
	 */
	public ResourceLocator() {
		// do nothing
	}

	/**
	 * returns the current (internal) locator, creating a new one first if needed
	 * 
	 * @return the current ResourceLocator; if none is present, generates one
	 *         first.
	 */
	public static ResourceLocator getResourceLocator() {
		if (locator == null)
			locator = new ResourceLocator();
		return locator;
	}

	/**
	 * Retrieve a resource by its name in String format
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName) {
		return getResourceStream(resourceName, null, false, null);
	}

	/**
	 * Retrieve a resource by its name and extension in String format
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param extension
	 *          the filename extension, important e.g. for I18N applications where
	 *          the filename encodes the locale.
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName, String extension) {
		return getResourceStream(resourceName, extension, false, null);
	}

	/**
	 * Retrieve a resource by its name in String format, a boolean indicating if
	 * the current environment is an Applet, and the codebase, if so.
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param runsAsApplet
	 *          indicates if this is an Application (=<code>false</code>) or
	 *          an Applet (=<code>true</code>)
	 * @param codeBaseName
	 *          the URL pointing towards the Applet's codebase, if this is
	 *          actually an Applet (<code>runsAsApplet == true</code>)
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName,
			boolean runsAsApplet, URL codeBaseName) {
		return getResourceStream(resourceName, null, runsAsApplet, codeBaseName);
	}

	/**
	 * Retrieve a resource by its name and extension plus directory in String
	 * format.
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param extension
	 *          the filename's extension (if any - else <code>null</code>)
	 * @param directoryName
	 *          the path for the file
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName, String extension,
			String directoryName) {
		String filename = null;
		if (directoryName == null)
			filename = resourceName;
		else if (directoryName.endsWith(File.separator))
			filename = directoryName + resourceName;
		else
			filename = directoryName + File.separator + resourceName;
		InputStream inStream = getResourceStream(filename, extension);
		if (inStream == null && directoryName != null)
			inStream = getResourceStream(resourceName, extension);
		return inStream;
	}

	/**
	 * Retrieve a resource by its name and extension in String format, a boolean
	 * indicating if the current environment is an Applet, and the codebase, if
	 * so.
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param extension
	 *          the filename's extension (if any - else <code>null</code>)
	 * @param runsAsApplet
	 *          indicates if this is an Application (=<code>false</code>) or
	 *          an Applet (=<code>true</code>)
	 * @param codeBase
	 *          the URL pointing towards the Applet's codebase, if this is
	 *          actually an Applet (<code>runsAsApplet == true</code>)
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 */
	public InputStream getResourceStream(String resourceName, String extension,
			boolean runsAsApplet, URL codeBase) {
		InputStream inputStream = null;
		StringBuilder filenameBuffer = new StringBuilder(255);
		if (runsAsApplet && codeBase != null) {
			filenameBuffer.append(codeBase.toString());
		}
		filenameBuffer.append(resourceName);
		if (extension != null)
			filenameBuffer.append('.').append(extension);
		// String filename = (extension == null) ? resourceName : resourceName +
		// "." +extension;
		String filename = filenameBuffer.toString();
		// did not work -- check 'resource as Stream'!
		inputStream = getClass().getResourceAsStream("/" + filename);
		// if (inputStream != null)
		// System.err.println("loaded by Class: "+resourceName +" / " +inputStream);
		// else {
		if (inputStream == null)
			inputStream = ClassLoader.getSystemResourceAsStream(filename);
		// if (inputStream != null)
		// System.err.println("loaded by ClassLoader: "+resourceName +" / "
		// +inputStream);
		// }
		if (runsAsApplet && inputStream == null) {
			try {
				URL targetURL = new URL(filename);
				// System.err.println("created URL: " +targetURL);
				inputStream = targetURL.openStream();
				// if (inputStream != null)
				// System.err.println("loaded by URL: "+resourceName +" / "
				// +inputStream);
			} catch (IOException ioExceptionApplet) {
				Debug
						.printlnMessage("Sorry, the applet cannot open the resource file for "
								+ filename);
			}
		}
		if (inputStream == null) {
			try {
				// First, look for file in local directory!
				inputStream = new FileInputStream(filename);
				// System.err.println("loaded by FileInputStream: "+resourceName +" / "
				// +inputStream);
			} catch (IOException ioExceptionLocal) {
				// did not work -- check 'resource as SystemStream'!
				if (inputStream == null)
					Debug
							.printlnMessage("Class, ClassLoader and local file IO cannot allocate file `"
									+ filename + "'");
			}
		}
		// if (inputStream == null)
		// System.err.println("not loaded at all: "+resourceName +" / "
		// +inputStream);
		return inputStream;
	}
}
