package testpackage.shared.ship.gui;

import java.util.HashMap;
import java.util.Map;

public class TemplateImages {
	static String[] icons = { "border", "fogofwar", "ship_bottom",
		"ship_hit_bottom", "ship_hit_horizontal", "ship_hit_left",
		"ship_hit", "ship_hit_right", "ship_hit_top",
		"ship_hit_vertical", "ship_horizontal", "ship_left",
		"ship_right", "ship_top", "ship_vertical", "water_hit",
	"water" };

	static String fieldToIcon(Character c) {
		Map<Character, String> m = new HashMap<Character, String>();
		for (String n : icons) {
			if (n == "fogofwar") m.put('#',n);
			if (n == "ship_bottom") m.put('b',n);
			if (n == "ship_hit_bottom") m.put('B',n);
			if (n == "ship_hit_horizontal") m.put('H',n);
			if (n == "ship_hit_left") m.put('L',n);
			if (n == "ship_hit_right") m.put('R',n);
			if (n == "ship_hit_top") m.put( 'T', n);
			if (n == "ship_hit_vertical") m.put('V', n);
			if (n == "ship_horizontal") m.put('h', n);
			if (n == "ship_left") m.put('l', n);
			if (n == "ship_right") m.put('r', n);
			if (n == "ship_top") m.put('t', n);
			if (n == "ship_vertical") m.put('v', n);
			if (n == "water_hit") m.put('*', n);
			if (n == "water") m.put('-', n);
		}
		return m.get(c);
	}
}
