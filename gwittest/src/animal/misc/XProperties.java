package animal.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * An extension of Properties, providing methods for reading
 * and writing <code>boolean</code>s, <code>int</code>s and
 * <code>Color</code>s.
 * Just to avoid casting.
 *
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
public class XProperties extends Properties {
  /**
         * Comment for <code>serialVersionUID</code>
         */
  private static final long serialVersionUID = 5464880768879063690L;
  public static final Point POINT_OF_ORIGIN = new Point(0, 0);
//  private PropertyNameMapper nameMapper = null;

  /**
   * initialize the <code>XProperties</code>.
   * 
   * @param defaultProperties a property list that contains default values for
   * any keys not found in this property list.
   * @see Properties
   */
  public XProperties(Properties defaultProperties) {
    super(defaultProperties);
  }

  public XProperties() {
    super();
  }

  /**
   * Merge two XProperties
   */
  public void addAllElements(XProperties otherResource) {
    otherResource.list(System.err);
    Enumeration<?> keys = otherResource.propertyNames();
    while (keys.hasMoreElements()) {
      String key = (String)keys.nextElement();
      String value = otherResource.getProperty(key);
      System.err.println(key +"=" +value);
      put(key, value);
    }
   }
  
  /**
    * get a Boolean property.
    * @return true if and only if <code>key</code> can be found and is
    * true
    */
  public boolean getBoolProperty(String key) {
    return getBoolProperty(key, false);
  }

  /**
    * get a Boolean property with default value
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. <code>defaultValue</code> otherwise.
    * @param key the key to look for
    * @param defaultValue the value to use as return value if the key
    * doesn't exist.
    */
  public boolean getBoolProperty(String key, boolean defaultValue) {
    String result = getProperty(key);

    return (result == null) ? defaultValue
                            : Boolean.valueOf(result).booleanValue();
  }

  /**
    * get a <code>double</code> property.
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. Otherwise, <code>Double.MIN_VALUE</code>
    * is returned and a message printed on the error-stream.
    */
  public double getDoubleProperty(String key) {
    return getDoubleProperty(key, Double.MIN_VALUE);
  }

  /**
    * get a <code>double</code> property with default value
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. <code>defaultValue</code> otherwise.
    */
  public double getDoubleProperty(String key, double defaultValue) {
    String result = getProperty(key);
    double value = defaultValue;

    if (result != null) {
      try {
        value = Double.parseDouble(result);
      }
      catch (NumberFormatException noNumber) {
        value = defaultValue;
      }
    }

    return value;
  }

  /**
    * get an <code>int</code> property.
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. Otherwise, <code>Integer.MIN_VALUE</code>
    * is returned and a message printed on the error-stream.
    */
  public int getIntProperty(String key) {
    return getIntProperty(key, Integer.MIN_VALUE);
  }

  /**
    * get an <code>int</code> property with default value
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. <code>defaultValue</code> otherwise.
    */
  public int getIntProperty(String key, int defaultValue) {
    String result = getProperty(key);
    int value = defaultValue;

    if (result != null) {
      try {
        value = Integer.parseInt(result);
      }
      catch (NumberFormatException noNumber) {
        value = defaultValue;
      }
    }

    return value;
  }

  /**
    * get an <code>int[]</code> property.
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. Otherwise, <code>null</code>
    * is returned and a message printed on the error-stream.
    */
  public int[] getIntArrayProperty(String key) {
    String entry = getProperty(key);
    int[] result = null;

    StringTokenizer stok = new StringTokenizer(entry);
    int size = stok.countTokens();
    result = new int[size];

    for (int i = 0; i < size; i++)
      result[i] = Integer.parseInt(stok.nextToken());

//    if (result == null) {
//      MessageDisplay.errorMsg("noIntArrayProp", key, 
//      		MessageDisplay.RUN_ERROR);
//    }

    return result;
  }

  /**
    * get a <code>Color</code> property.
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. <code>defaultColor</code> otherwise.
    */
  public Color getColorProperty(String key) {
    return getColorProperty(key, Color.black);
  }

  /**
    * get a <code>Color</code> property.
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists. <code>defaultColor</code> otherwise.
    */
  public Color getColorProperty(String key, Color defaultColor) {
    String result = getProperty(key);

    return (result == null) ? defaultColor : ColorChoice.getColor(result);
  }

  public Font getFontProperty(String key) {
    return getFontProperty(key, new Font("SansSerif", 16, Font.PLAIN));
  }

  public Font getFontProperty(String key, Font defaultFont) {
    String value = getProperty(key);

    if (value == null) {
      return defaultFont;
    }

    int size = 16;
    int style = Font.PLAIN;
    StringTokenizer stringTokenizer = new StringTokenizer(value);
    String fontName = stringTokenizer.nextToken();

    if (stringTokenizer.hasMoreTokens()) {
      try {
        size = Integer.parseInt(stringTokenizer.nextToken());
        style = Integer.parseInt(stringTokenizer.nextToken());
      }
      catch (NumberFormatException nfe) {
        size = 16;
        style = Font.PLAIN;
      }
    }

    return new Font(fontName, style, size);
  }

  /**
    * get a <code>Image</code> property.
    *
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists,  else the point of origin
    */
  public Image getImageProperty(String key) {
    return (Image) get(key);
  }

  /**
    * get a <code>Point</code> property.
    *
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists,  else the point of origin
    */
  public Point getPointProperty(String key) {
    return getPointProperty(key, POINT_OF_ORIGIN);
  }

  /**
    * get a <code>Point</code> property.
    *
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists,  <code>defaultPoint</code> otherwise.
    */
  public Point getPointProperty(String key, Point defaultPoint) {
    Object result = get(key);

    if (result != null) {
      if (result instanceof Point) {
        return (Point) result;
      }

      if (result instanceof String) {
        StringTokenizer stringTokenizer = new StringTokenizer((String) result);

        try {
          int x = Integer.parseInt(stringTokenizer.nextToken());
          int y = Integer.parseInt(stringTokenizer.nextToken());

          return new Point(x, y);
        }
        catch (NumberFormatException e) {
          MessageDisplay.errorMsg("nfe", stringTokenizer.toString(),
          		MessageDisplay.RUN_ERROR);
        }
      }
    }

    return defaultPoint;
  }

  /**
    * get a <code>Point</code> property.
    *
    * @return the value of the property assigned to <code>key</code>,
    * if this property exists,  <code>defaultPoint</code> otherwise.
    */
  public Point[] getPointArrayProperty(String key) {
    Object result = get(key);

    if (result != null) {
      if (result instanceof Point) {
        return new Point[] { (Point) result };
      }

      if (result instanceof String) {
        StringTokenizer stringTokenizer = new StringTokenizer((String) result);
        Point[] points = new Point[stringTokenizer.countTokens() >> 1];
        int currentPointNr = 0;

        while (stringTokenizer.hasMoreTokens()) {
          try {
            int x = Integer.parseInt(stringTokenizer.nextToken());
            int y = Integer.parseInt(stringTokenizer.nextToken());
            points[currentPointNr++] = new Point(x, y);
          }
          catch (NumberFormatException e) {
            MessageDisplay.errorMsg("nfe", stringTokenizer.toString(),
            		MessageDisplay.RUN_ERROR);
          }
        }

        Point[] resultPoints = new Point[currentPointNr];
        System.arraycopy(points, 0, resultPoints, 0, currentPointNr);

        return resultPoints;
      }
    }

    return new Point[] { new Point(0, 0) };
  }

  /**
   * Retrieves an array of Strings separated by whitespace (' ').
   *
   * @param key the key for looking up the array
   * @return the String array retrieved from the properties.
   */
  public String[] getStringArrayProperty(String key) {
    return getStringArrayProperty(key, " \t\n\r\f");
  }

  /**
   * Retrieves an array of Strings separated by 'sep'.
   *
   * @param key the key for looking up the array
   * @param sep the separator used for separating the array entries
   * @return the String array retrieved from the properties.
   */
  public String[] getStringArrayProperty(String key, String sep) {
    String[] sa = makeArrayFromString(getProperty(key), sep);

    if (sa == null) {
      MessageDisplay.errorMsg("noStringArrayProp", key,
      		MessageDisplay.RUN_ERROR);
    }

    return sa;
  }

  /**
    * overwrites Properties.put(String,String) to avoid
    * NullPointerExceptions when value is null.
    * In this case, nothing is inserted.
    * /
  public void put(String key, String value)
  {
    if (value == null)
      return;
    super.put(key, value);
  }
  */
  /**
    * overwrites Properties.put(String,String) to avoid
    * NullPointerExceptions when value is null.
    * In this case, nothing is inserted.
    */
  public void put(String key, String[] values) {
    if ((values == null) || (values.length == 0)) {
      return;
    }

    StringBuilder localBuffer = new StringBuilder(values.length << 4);

    for (int i = 0; i < values.length; i++)
      localBuffer.append(values[i]).append(MessageDisplay.LINE_FEED);

    put(key, localBuffer.toString());
  }

  /**
    * put an <code>int</code> into the properties.
    */
  public void put(int key, int value) {
    put(String.valueOf(key), String.valueOf(value));
  }

  /**
    * put a <code>double</code> into the properties.
    */
  public void put(String key, double value) {
    put(key, String.valueOf(value));
  }

  /**
    * put an <code>int</code> into the properties.
    */
  public void put(String key, int value) {
    put(key, String.valueOf(value));
  }

  /**
   * put a <code>int[]</code> into the properties by concatenating the values
   */
  public void put(String key, int[] values) {
    put(key, arrayAsString(values));
  }

  /**
   * put a <code>int[]</code> into the properties by concatenating the values
   */
  public void put(String key, int[] values, int from, int until) {
    put(key, arrayAsString(values, from, until));
  }

  /**
    * put a <code>boolean</code> into the properties
    */
  public void put(String key, boolean value) {
    put(key, String.valueOf(value));
  }

  /**
    * put a <code>Color</code> into the properties
    */
  public void put(String key, Color color) {
    put(key, ColorChoice.getColorName(color));
  }

  public void put(String key, Font font) {
    put(key, font.getName() + " " + font.getSize() + " " + font.getStyle());
    put(key + "Name", font.getName());
    put(key + "Size", font.getSize());
    put(key + "Style", font.getStyle());
  }

  /**
    * put a <code>Point</code> into the properties
    */
  public void put(String key, Point point) {
    put(key, point.x + " " + point.y);
  }

  /**
    * put a <code>Point</code> array into the properties
    */
  public void put(String key, Point[] points) {
    int i;
//    System.err.print("Storing a point array: ");

    for (i = 0; i < points.length; i++)
      if (points[i] != null) {
        System.err.print(points[i].toString() + " ");
      }

    StringBuilder pointsString = new StringBuilder();

    for (i = 0; i < points.length; i++)
      pointsString.append(points[i].x).append(" ").append(points[i].y).append(" ");

    put(key, pointsString.toString().substring(0, pointsString.length() - 1));
//    System.err.println("\nStored as: " + get(key));
  }

  /**
   * Retrieve all keys ending with the suffix passed in
   *
   * @param suffix the suffix to look for
   * @return a String array of keys, or null if no keys are found.
   */
  public String[] getKeysWithSuffix(String suffix) {
    Enumeration<?> e = propertyNames();
    int suffixLength = suffix.length();
    int nrHits = 0;
    Vector<String> helper = new Vector<String>(100);

    while (e.hasMoreElements()) {
      String currentKey = (String) e.nextElement();

      if (currentKey.regionMatches(true, currentKey.length() - suffixLength,
            suffix, 0, suffixLength)) {
        helper.addElement(currentKey);
      }
    }

    nrHits = helper.size();

    if (nrHits == 0) {
      return null;
    }

    String[] keys = new String[nrHits];

    for (int i = 0; i < nrHits; i++)
      keys[i] = helper.elementAt(i);

    helper.removeAllElements();
    helper = null;

    return keys;
  }

  /**
   * Retrieve all keys starting with the prefix passed in
   *
   * @param prefix the prefix to look for
   * @return a String array of keys, or null if no keys are found.
   */
  public String[] getKeys(String prefix) {
    Enumeration<?> e = propertyNames();
    int prefixLength = prefix.length();
    int nrHits = 0;
    Vector<String> helper = new Vector<String>(100);

    while (e.hasMoreElements()) {
      String currentKey = (String) e.nextElement();

      if (currentKey.regionMatches(true, 0, prefix, 0, prefixLength)) {
        helper.addElement(currentKey);
      }
    }

    nrHits = helper.size();

    if (nrHits == 0) {
      return null;
    }

    String[] keys = new String[nrHits];

    for (int i = 0; i < nrHits; i++)
      keys[i] = helper.elementAt(i);

    helper.removeAllElements();
    helper = null;

    return keys;
  }

  /**
   * Return a XProperties object with all elements associated with a key starting with 'prefix'
   *
   * @param prefix the prefix for the keys(case-insensitive)
   * @return an XProperties object containing all elements with key prefix 'prefix'
   */
  public XProperties getElementsForPrefix(String prefix) {
    // first, retrieve the matching keys
    String[] keys = getKeys(prefix);

    // if no keys found, return empty set
    if (keys == null) {
      return new XProperties();
    }

    // otherwise, iterate over all keys found
    XProperties xprops = new XProperties();

    int nrKeys = keys.length;
    int prefixLength = prefix.length();

    for (int i = 0; i < nrKeys; i++) {
      // ensure proper case!
      String targetKey = prefix + keys[i].substring(prefixLength);
      xprops.put(targetKey, getProperty(keys[i]));
    }

    return xprops;
  }

  public static String arrayAsString(int[] values) {
    if (values != null)
      return arrayAsString(values, 0, values.length - 1);

    return "";
  }

  public static String arrayAsString(int[] values, int from, int until) {
    if ((values == null) || (values.length == 0)) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    for (int i = from; (i <= until) && (i < values.length); i++)
      sb.append(values[i]).append(' ');

    return sb.toString();
  }

  /**
   * Parse the first string into an array of Strings separated by 'sep'
   *
   * @param array the String containing the array entities
   * @param sep the separator(s) that delimit the individual entries
   * @return the array of retrieved Strings
   */
  public static String[] makeArrayFromString(String array, String sep) {
    if ((array != null) && (sep != null)) {
      StringTokenizer st = new StringTokenizer(array, sep);
      String[] s = new String[st.countTokens()];
      int i = 0;

      while (st.hasMoreTokens())
        s[i++] = st.nextToken();

      return s;
    }

    return null;
  }

  /*
  public String getProperty(String key)
  {
    if (!key.equals(nameMapper.lookupMapping(key)))
      System.err.println("Mapped get-key \"" +key +"\" to \""
                         +nameMapper.lookupMapping(key) +"\"");
    return super.getProperty(nameMapper.lookupMapping(key));
  }
  */
  public void put(String key, String value) {
    if (value == null) {
      return;
    }

    super.put(key, value);

    /*
    if (!key.equals(nameMapper.lookupReverseMapping(key)))
      System.err.println("Mapped put-key \"" +key +"\" to \""
                         +nameMapper.lookupReverseMapping(key) +"\"");
    super.put(nameMapper.lookupReverseMapping(key), value);
    */
  }

  /*
  public String getNameMap(String key)
  {
    if (nameMapper == null || !nameMapper.containsKey(key))
      return key;
    else
      return nameMapper.getProperty(key);
  }

  public String getReverseNameMap(String key)
  {
    if (reverseNameMapper == null || !reverseNameMapper.containsKey(key))
      return key;
    else
      return reverseNameMapper.getProperty(key);
  }
  */
  /*
  public void setNameMapper(PropertyNameMapper mapper)
  {
    nameMapper = mapper;
  } */
} // XProperties
