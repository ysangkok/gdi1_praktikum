package testpackage.shared.ship.gui;

import java.util.HashMap;
import java.util.Map;

import testpackage.shared.ship.SoundHandler.Sound;

public class TemplateImages {
	private static final String soundspath = "/sounds";

	public static final String translationspath = "/translations/";

	public static final String levelspath = "/resources/levels/defaultlevels/";
	public static final String imagesdir = "/resources/images/";
	
	public static final String weblevelspath = "template/" + levelspath;
	public static final String webimagesdir = "template/" + imagesdir + "defaultskin/";
	
	public static String[] allSkins = {"defaultskin", "smallskin"};

	public static String[] icons = { "fogofwar", "ship_bottom",
		"ship_hit_bottom", "ship_hit_horizontal", "ship_hit_left",
		"ship_hit", "ship_hit_right", "ship_hit_top",
		"ship_hit_vertical", "ship_horizontal", "ship_left",
		"ship_right", "ship_top", "ship_vertical", "water_hit",
	"water" };

	private static Map<Character, String> m = null;

	public static String getSoundPath(Sound sound) {
		return soundspath + "/" + sound.toString().replace('_', '.');
	}

	public static String fieldToIcon(Character c) {
		if (m == null) {
			m = new HashMap<Character, String>();
			m.put('#', "fogofwar") ;
			m.put('B', "ship_hit_bottom") ;
			m.put('b', "ship_bottom") ;
			m.put('H', "ship_hit_horizontal") ;
			m.put('h', "ship_horizontal") ;
			m.put('L', "ship_hit_left") ;
			m.put('l', "ship_left") ;
			m.put('R', "ship_hit_right") ;
			m.put('r', "ship_right") ;
			m.put('T', "ship_hit_top") ;
			m.put('t', "ship_top") ;
			m.put('V', "ship_hit_vertical") ;
			m.put('v', "ship_vertical") ;
			m.put('*', "water_hit");
			m.put('-', "water");
		}
		return m.get(c);
	}
}
