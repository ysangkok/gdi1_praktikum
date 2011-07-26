package testpackage.interfaces;

/**
 * implement this interface to receive coordinates from BoardPanel
 */
public interface BoardUser {
	/**
	 * @param p owner of board clicked
	 * @param x coordinate (traditional format)
	 * @param y coordinate (traditional format)
	 */
	void bomb(int p, int x, int y);
}
