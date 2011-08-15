package testpackage.shared.ship;

import testpackage.shared.ship.exceptions.InvalidInstruction;

import java.lang.Character;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * AI that tries to shoot ships until they die. doesn't support ammo. tries to avoid code duplication like in GoodAI. TODO: prefer shooting fields that aren't adjacent to ships, or in front/behind.
 */
public class IntelligentAI extends AI {
	/**
	 * for representing an offset relative to a coordinate
	 */
	static class Offset {
		int x, y;
		Offset(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * for representing the direction in which the AI is moving on the map (to try and shoot a ship until it's completely sunken)
	 */
	static enum Direction {
		NORTH, EAST, WEST, SOUTH, UNKNOWN; // unknown is used when a ship is hit but we don't know which orientation (not direction!) it is yet
	
		/**
		 * gets a random direction that is 90 degrees left or right of given direction. currently unused, but could be useful.
		 * @param gen random gen to use
		 * @param d direction is consider
		 * @return east/west when d=north/south, and reverse
		 */
		static Direction randomAngular(Random gen, Direction d) {
			boolean b = gen.nextBoolean();
			switch (d) {
				case NORTH:
				case SOUTH:
					d = Direction.EAST;
					break;
				case EAST:
				case WEST:
					d = Direction.NORTH;
					break;
			}
			return (b ? opposite(d) : d);
		}
	
		/**
		 * get opposite direction
		 * @param d direction to consider 
		 * @return opposite direction of d
		 */
		static Direction opposite(Direction d) {
			switch (d) {
				case NORTH:
					return Direction.SOUTH;
				case SOUTH:
					return Direction.NORTH;
				case EAST:
					return Direction.WEST;
				case WEST:
					return Direction.EAST;
				default:
					throw new RuntimeException("can't make opposite of " + d);
			}
		}
	
	}

	private Set<Direction> tried ;

	private Random gen;
	private Engine engine;
	private boolean randomMode;
	private boolean triedOtherDirection;
	private Direction currentDirection;
	
	private int xwidth;
	private int ywidth;

	private int originalShipX, originalShipY, lastX, lastY = -1;

	private Map<Direction, Offset> doffset;
	
	public boolean supportsAmmo() { return false; }
	public boolean supportsRange() { return false; }
	
	private static void Debug(String str) {
		//System.err.println(str);
	}

	public IntelligentAI() {
		doffset = new HashMap<Direction, Offset>();
		doffset.put(Direction.NORTH, new Offset(-1, 0));
		doffset.put(Direction.SOUTH, new Offset( 1, 0));
		doffset.put(Direction.WEST , new Offset( 0,-1));
		doffset.put(Direction.EAST , new Offset( 0, 1));
		tried = new HashSet<Direction>();
		randomMode();
	}

	public void setEngine(Engine engine) {
		super.setEngine(engine);
		gen = new Random();
		xwidth = engine.getxWidth();
		ywidth = engine.getyWidth();
		this.engine = engine;
	}

	private static boolean hitShip(char c) {
		return Character.isUpperCase(c); // only uppercase characters used are ships anyway, we dont need the same level of correctness as in Level.java
	}

	/**
	 * go in random mode, i.e. shoot randomly until we hit something
	 */
	private void randomMode() {
		tried.clear();
		triedOtherDirection = false;
		randomMode = true;
		currentDirection = Direction.UNKNOWN;
	}

	@Override
	public void playAs(int player) {
		char hit = '\0';
		if (randomMode) {
			Debug("  randomMode: TRUE");
			try {
				lastX = gen.nextInt(xwidth);
				lastY = gen.nextInt(ywidth);
				
				hit = engine.attack(player, lastX, lastY);

				if (hitShip(hit)) {
					Debug("      hit: TRUE");
					randomMode = false;
					originalShipX = lastX;
					originalShipY = lastY;
				}
				
			} catch (InvalidInstruction e) {
				return;
			}
		} else {
			Debug("  randomMode: FALSE");
			Debug("    CDir: " + currentDirection);
			if (currentDirection == Direction.UNKNOWN) {
				assert !triedOtherDirection;
				if (tried.size() >= 4) { randomMode(); return; }

				int nextInt = 0;
				if (tried.size() < 4) nextInt = gen.nextInt(4 - tried.size());

				Set<Direction> dset = new HashSet<Direction>();

				dset.addAll(new HashSet<Direction>(Arrays.asList(new Direction[] {Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH})));
				dset.removeAll(tried);

				Direction chosenDirection = (Direction) (dset.toArray())[nextInt];
				tried.add(chosenDirection);

				try {
					hit = engine.attack(player, lastX+doffset.get(chosenDirection).x, lastY+doffset.get(chosenDirection).y);
				} catch (InvalidInstruction e) {
					Debug("      hit: COULDNT SHOOT");
					Debug("      lastX: " + lastX);
					Debug("      lastY: " + lastY);
					Debug("      nextInt: " + nextInt);
					if (tried.size() == 4) {
						randomMode();
					} else {
						Debug("        " + Arrays.toString(tried.toArray()));
					}
					return;
				}
				if (hitShip(hit)) {
					Debug("      hit: TRUE");
					lastX = lastX+doffset.get(chosenDirection).x;
					lastY = lastY+doffset.get(chosenDirection).y;
					currentDirection = chosenDirection;
				} else {
					Debug("      hit: FALSE");
				}
			} else {
				try {
					lastX = lastX+doffset.get(currentDirection).x;
					lastY = lastY+doffset.get(currentDirection).y;
					hit = engine.attack(player, lastX, lastY);
				} catch (InvalidInstruction e) {
					Debug(e.getMessage());
					if (triedOtherDirection)
						randomMode();
					else
						tryOtherDirection();
						//LOLOL
					return;
				}
				if (!hitShip(hit)) {
					Debug("      hit: FALSE");
					if (triedOtherDirection) {
						Debug("        tried other: TRUE");
						randomMode();
					} else {
						Debug("        tried other: FALSE");
						tryOtherDirection();
					}
				}
			}
			if (hit == 'X') {
				Debug("Shot ship");
				randomMode();
			}
		}
	}
	
	/**
	 * when a ship is found and completely shot in one direction, we still need to shoot the other direction from the initial hit coord, since the ship continues in that direction too.
	 */
	private void tryOtherDirection() {
		Debug("Trying other direction");
		triedOtherDirection = true;
		currentDirection = Direction.opposite(currentDirection);
		lastX = originalShipX;
		lastY = originalShipY;
	}
}
