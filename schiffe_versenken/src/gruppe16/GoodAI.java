package gruppe16;

import gruppe16.exceptions.InvalidInstruction;
import java.lang.Character;
import java.util.Random;

public class GoodAI extends AI {

	Random gen;
	int lastX = -1;
	int lastY = -1;
	char lastChar = '\0';
	Engine engine;
	boolean randomMode = true;

	int xwidth;
	int ywidth;

	GoodAI(Engine engine) {
		gen = new Random();
		xwidth = engine.getxWidth();
		ywidth = engine.getyWidth();
		this.engine = engine;
	}

	public static boolean hitShip(char c) {
		return Character.isUpperCase(c);
	}

	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	public void shootRestVert(int p, int x, int y, Direction d) {
		try {
			engine.attack(p, x, y);
		} catch (InvalidInstruction e) {
			return;
		}

		if ((d == Direction.SOUTH || d == Direction.NORTH) && lastChar != 'T')
			return;
		if ((d == Direction.EAST || d == Direction.WEST) && lastChar != 'R')
			return;
		if ((d == Direction.NORTH || d == Direction.SOUTH) && lastChar != 'B')
			return;
		if ((d == Direction.WEST || d == Direction.EAST) && lastChar != 'L')
			return;

		if (d == Direction.NORTH)
			shootRestVert(p, x - 1, y, d);
		if (d == Direction.SOUTH)
			shootRestVert(p, x + 1, y, d);
		if (d == Direction.EAST)
			shootRestVert(p, x, y - 1, d);
		if (d == Direction.SOUTH)
			shootRestVert(p, x, y + 1, d);
	}

	@Override
	public void playAs(int player) {
		if (randomMode) {
			try {
				lastX = gen.nextInt(xwidth);
				lastY = gen.nextInt(ywidth);
				lastChar = engine.attack(player, lastX, lastY);

				if (hitShip(lastChar))
					randomMode = false;
			} catch (InvalidInstruction e) {
				playAs(player);
			}
		} else {
			int chosenX = -1;
			int chosenY = -1;

			if (lastChar == 'L') {
				shootRestVert(player, lastX, lastY + 1, Direction.EAST);
			}
			

			if (lastChar == 'R') {
				shootRestVert(player, lastX, lastY - 1, Direction.WEST);
			}
			randomMode = true;
			if (lastChar == 'T') {
				shootRestVert(player, lastX + 1, lastY, Direction.SOUTH);

			} 
			else if (lastChar == 'B') {
				shootRestVert(player, lastX - 1, lastY, Direction.NORTH);
			}
			randomMode = true;

			if (lastChar == 'V') {

				shootRestVert(player, lastX - 1, lastY, Direction.NORTH);
				shootRestVert(player, lastX + 1, lastY, Direction.SOUTH);

			}
			
			if (lastChar == 'H') {

				shootRestVert(player, lastX, lastY + 1, Direction.EAST);
				shootRestVert(player, lastX, lastY - 1, Direction.WEST);

			}

			try {
				engine.attack(player, chosenX, chosenY);
			} catch (InvalidInstruction e) {

			}
			randomMode = true;

		}
	}
}
