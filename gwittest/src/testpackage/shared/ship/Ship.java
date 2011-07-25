package testpackage.shared.ship;

import java.util.ArrayList;
import java.util.List;

/**
 * class for representing an abstract ship, i.e. not drawn or anything
 */
public class Ship {
	/**
	 * used for specifying ship orientation on map
	 */
	public enum Orientation {
		/**
		 * ship lies vertically on map
		 */
		VERTICAL,
		/**
		 * ship lies horizontally on map
		 */
		HORIZONTAL
	}

	/**
	 * used for specifying ship direction on map. vertical always corresponds to down, but had we supported ships sailing north, it's nice to have the distinction
	 */
	public enum Direction {
		/**
		 * ship points downwards
		 */
		DOWN,
		/**
		 * ship points eastwards
		 */
		RIGHT
	}
	
	/** x coord */
	int x;
	/** y coord */
	int y;
	/** ship length */
	int len;
	/** ship orientation */
	Orientation o;
	
	/**
	 * constructs ship. coord format agnostic but getalloccupiedcoords() assumes horizontal ships to lie along second (y coord)
	 * @param x coord
	 * @param y coord
	 * @param length length
	 * @param o ship orientation
	 */
	public Ship(int x, int y, int length, Orientation o) {
		this.x = x;
		this.y = y;
		this.len = length;
		this.o = o;
	}
	
	public String toString() {
		return len + "-ship at (" + x + "," + y + "), orientation " + o;
	}
	
	/**
	 * get all fields that this ship occupies
	 * @return list of integer arrays with 2 entries: x coord and y coord.
	 */
	public List<Integer[]> getAllOccupiedCoords() {
		List<Integer[]> coords = new ArrayList<Integer[]>();
		
		for (int i=0; i<len; i++) {
			if (o == Orientation.HORIZONTAL) {
				coords.add(new Integer[] { x, y+i });
			} else if (o == Orientation.VERTICAL) {
				coords.add(new Integer[] { x+i, y });					
			} else {
				assert false;
			}
		}
		
		return coords;
	}
}