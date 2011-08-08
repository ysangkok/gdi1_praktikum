package animal.misc;

import java.awt.Color;
import java.util.StringTokenizer;

import javax.swing.JComboBox;

/**
 * a ComboBox to select a Color.
 * It contains all the java standard colors as defined in java.awt.Color.
 *
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
public class ColorChoice extends JComboBox {
  /**
         * Comment for <code>serialVersionUID</code>
         */
  private static final long serialVersionUID = -7612429913525980839L;

  /**
   * Animal standard colors and their names
   * changed by Guido 1999-03-29
   */
  private static NamedColor[] STANDARD_COLORS = {
      new NamedColor(Color.black, "black"), new NamedColor(Color.blue, "blue"),
      new NamedColor(Color.cyan, "cyan"),
      new NamedColor(Color.darkGray, "dark Gray"),
      new NamedColor(Color.gray, "gray"), new NamedColor(Color.green, "green"),
      new NamedColor(Color.lightGray, "light Gray"),
      new NamedColor(Color.magenta, "magenta"),
      new NamedColor(Color.orange, "orange"),
      new NamedColor(Color.pink, "pink"), new NamedColor(Color.red, "red"),
      new NamedColor(Color.white, "white"),
      new NamedColor(Color.yellow, "yellow"),
      new NamedColor(new Color(0, 0, 0x90), "blue4"),
      new NamedColor(new Color(0, 0, 0xb0), "blue3"),
      new NamedColor(new Color(0, 0, 0xd0), "blue2"),
      new NamedColor(new Color(0x87, 0xce, 0xff), "light_blue"),
      new NamedColor(new Color(0, 0x90, 0), "green4"),
      new NamedColor(new Color(0, 0xb0, 0), "green3"),
      new NamedColor(new Color(0, 0xd0, 0), "green2"),
      new NamedColor(new Color(0, 0x90, 0x90), "cyan4"),
      new NamedColor(new Color(0, 0xb0, 0xb0), "cyan3"),
      new NamedColor(new Color(0, 0xd0, 0xd0), "cyan2"),
      new NamedColor(new Color(0x90, 0, 0), "red4"),
      new NamedColor(new Color(0xb0, 0, 0), "red3"),
      new NamedColor(new Color(0xd0, 0, 0), "red2"),
      new NamedColor(new Color(0x90, 0, 0x90), "magenta4"),
      new NamedColor(new Color(0xb0, 0, 0xb0), "magenta3"),
      new NamedColor(new Color(0xd0, 0, 0xd0), "magenta2"),
      new NamedColor(new Color(0x80, 0x30, 0), "brown4"),
      new NamedColor(new Color(0xa0, 0x40, 0), "brown3"),
      new NamedColor(new Color(0xc0, 0x60, 0), "brown2"),
      new NamedColor(new Color(0xff, 0x80, 0x80), "pink4"),
      new NamedColor(new Color(0xff, 0xa0, 0xa0), "pink3"),
      new NamedColor(new Color(0xff, 0xc0, 0xc0), "pink2"),
      new NamedColor(new Color(0xff, 0xe0, 0xe0), "pink"),
      new NamedColor(new Color(0xff, 0xd7, 0), "gold")
    };

  /**
   * construct a ComboBox containing the names of all standard colors.
   */
  public ColorChoice() {
    String[] colors = getAllColorNames();

    for (int a = 0; a < colors.length; a++)
      addItem(colors[a]);
  }

  /**
     * returns a name for the standard Java colors
     * (and for any color, but then no "human-readable" form).
     */
  public static String getColorName(Color color) {
    for (int a = 0; a < STANDARD_COLORS.length; a++)
      if (STANDARD_COLORS[a].color.equals(color)) {
        return STANDARD_COLORS[a].name;
      }

    return (color == null) ? "none"
                           : ("(" + color.getRed() + ", " + color.getGreen() +
    ", " + color.getBlue() + ")");
  }

  /**
    * returns the names of all standard colors.
    * Required for building the ComboBox.
    */
  public static String[] getAllColorNames() {
    String[] result = new String[STANDARD_COLORS.length];

    for (int a = 0; a < STANDARD_COLORS.length; a++)
      result[a] = STANDARD_COLORS[a].name;

    return result;
  }

  /**
    * returns the color associated with <i>name</i>.
    * @return Color.black if no Color matching <i>name</i> is found.
    */
  public static Color getColor(String name) {
    for (int a = 0; a < STANDARD_COLORS.length; a++)
      if (STANDARD_COLORS[a].name.equals(name)) {
        return STANDARD_COLORS[a].color;
      }

    if (name.startsWith("(")) {
      StringTokenizer stringTokenizer = new StringTokenizer(name.substring(1,
            name.length() - 1), ", ");
      int red = Integer.valueOf(stringTokenizer.nextToken()).intValue();
      int green = Integer.valueOf(stringTokenizer.nextToken()).intValue();
      int blue = Integer.valueOf(stringTokenizer.nextToken()).intValue();

      return new Color(red, green, blue);
    }

    MessageDisplay.errorMsg("unknownColorNowBlack",
    		new String[] {name, "black" }, MessageDisplay.RUN_ERROR);

    return Color.black;
  }

  /**
    * returns whether the color associated with <em>name</em> is defined.
    * @return Color.black if no Color matching <em>name</em> is found.
    */
  public static boolean validColorName(String name) {
    for (int a = 0; a < STANDARD_COLORS.length; a++)
      if (STANDARD_COLORS[a].name.equalsIgnoreCase(name)) {
        return true;
      }

    return false;
  }

  /**
    * returns the color currently selected.
    */
  public Color getColor() {
    return getColor((String) getSelectedItem());
  }

  /**
    * selectes the color.
    */
  public void setColor(Color c) {
    setSelectedItem(getColorName(c));
  }
} // ColorChoice
