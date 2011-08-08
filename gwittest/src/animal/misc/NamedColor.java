package animal.misc;

import java.awt.Color;

/**
 * an auxiliary class for ColorChoice, each containing a Color and its name.
 */
class NamedColor {
	// accessed from ColorChoice
	Color color;

	String name;

	NamedColor(Color aColor, String aName) {
		color = aColor;
		name = aName;
	}
} // NamedColor
