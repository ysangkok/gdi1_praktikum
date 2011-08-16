package testpackage.shared.ship;

/**
 * class for storing game rules
 */
public class Rules {
	public static final int standardSpeerfeuerTime = 5000;

	public static final boolean enableShotsPerShip = false;
	public static final boolean defaultReichweiteVonSchiffenEnabled = false;
	/**
	 * Necessary for test compliance
	 */
	public static int defaultWidth = 10;
	/**
	 * Necessary for test compliance
	 */
	public static int defaultHeight = 10;
	/**
	 * Necessary for test compliance
	 */
	public static boolean defaultAllowMultipleShotsPerTurn = false;
	
	/**
	 * default ship counts. first: ship length; second: ship count
	 * these values are necessary for test compliance
	 */
	public static int[][] ships = {
		{2,4, Engine.decideShipRange(defaultWidth, defaultHeight, 2, 4)}, // 4 schnellbote
		{3,3, Engine.decideShipRange(defaultWidth, defaultHeight, 3, 3)}, // TODO dynamisch
		{4,2, Engine.decideShipRange(defaultWidth, defaultHeight, 4, 2)},
		{5,1, Engine.decideShipRange(defaultWidth, defaultHeight, 5, 1)}
	};
	
	/**
	 * number of shots per each part of not-sunken ship part. value necessary for test compliance
	 */
	public static int shotsPerShipPart = 5;
	
}
