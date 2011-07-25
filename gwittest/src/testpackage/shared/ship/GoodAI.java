package testpackage.shared.ship;

import testpackage.shared.ship.exceptions.InvalidInstruction;

import java.awt.List;
import java.lang.Character;
import java.util.LinkedList;
import java.util.Random;

public class GoodAI extends AI {

	Random gen;
	int lastX = -1;
	int lastY = -1;
	char lastChar = '\0';
	Engine engine;
	boolean randomMode = true;
	int[][] coor;

	int xwidth;
	int ywidth;
	
	int [] listY;
	
	GoodAI(Engine engine) {
		gen = new Random();
		xwidth = engine.getxWidth();
		ywidth = engine.getyWidth();
		this.engine = engine;
	}

	public static boolean hitShip(char c) {
		return Character.isUpperCase(c);
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
				
			}
		} else {
			int chosenX = lastX;
			int chosenY = lastY;

			if (lastChar == 'L' || lastChar == 'R') {
				Hor(player, chosenX, chosenY, lastChar);
			}

			else if (lastChar == 'T' || lastChar == 'B'){
				Ver(player, chosenX, chosenY, lastChar);
			}
			
			else if (lastChar == 'H'){
				
				firstHitHorEast(player, lastX, lastY);
				
			}
			
			else if (lastChar == 'V'){
				
				firstHitVerSouth(player, lastX, lastY, lastChar);
				
			}
			/*
			 * 
			 * 
			 * try { engine.attack(player, chosenX, chosenY); } catch
			 * (InvalidInstruction e) {
			 * 
			 * } randomMode = true;
			 */

		}
	}
	
	
	
	public void firstHitHorEast(int player, int lastX, int lastY){
		char current = '\0';
		int currentY = lastY;
		
		try {
			current = engine.attack(player, lastX, currentY = currentY + 1);

		} catch (InvalidInstruction e) {
		}
		
		if (current == 'H'){
			firstHitHorEast(player, lastX, currentY);
		}
		else if (current == 'R'){
			firstHitHorWest(player, lastX, lastY);
		}
	}
	
	
	public void firstHitHorWest(int player, int lastX, int lastY){
		char current = '\0';
		try {
			current = engine.attack(player, lastX, lastY = lastY - 1);

		} catch (InvalidInstruction e) {
		}
		
		if (current == 'H'){
			firstHitHorWest(player, lastX, lastY);
		}
		else if (current == 'L'){
			randomMode = true;
			playAs(player);
		}
	}
	
	public void firstHitVerSouth(int player, int lastX, int lastY, char lastChar){
		char current = '\0';
		int currentX = lastX;
		
		try {
			current = engine.attack(player, currentX = currentX + 1, lastY);

		} catch (InvalidInstruction e) {
		}
		
		if (current == 'V'){
			firstHitVerSouth(player, currentX, lastY, lastChar);
		}
		else if (current == 'B'){
			firstHitVerNorth(player, lastX, lastY, lastChar);
		}
		
	}
	
	public void firstHitVerNorth(int player, int lastX, int lastY, int lastChar){
		char current = '\0';
		try {
			current = engine.attack(player, lastX = lastX - 1, lastY);

		} catch (InvalidInstruction e) {
		}
		
		if (current == 'V'){
			firstHitVerNorth(player, lastX, lastY, lastChar);
		}
		else if (current == 'T'){
			randomMode = true;
			playAs(player);
		}
	}

	public void Hor(int player, int lastX, int lastY, char lastChar) {
		if (lastChar == 'R') {

			try {
				lastChar = engine.attack(player, lastX, lastY = lastY - 1);

			} catch (InvalidInstruction e) {
			}
			if (lastChar == 'H') {
				HorMidRight(player, lastX, lastY, lastChar);
			} else if (lastChar == 'L') {
				randomMode = true;
				playAs(player);
			}

		}

		if (lastChar == 'L') {
			try {
				lastChar = engine.attack(player, lastX, lastY = lastY + 1);

			} catch (InvalidInstruction e) {
			}
			if (lastChar == 'H') {
				HorMidLeft(player, lastX, lastY, lastChar);
			} else if (lastChar == 'R') {
				randomMode = true;
				playAs(player);
			}

		}
	}
	
	public void Ver(int player, int lastX, int lastY, char lastChar) {
		if (lastChar == 'T') {

			try {
				lastChar = engine.attack(player, lastX = lastX + 1, lastY);

			} catch (InvalidInstruction e) {
			}
			if (lastChar == 'V') {
				VerMidBottom(player, lastX, lastY, lastChar);
			} else if (lastChar == 'B') {
				randomMode = true;
				playAs(player);
			}

		}

		if (lastChar == 'B') {
			try {
				lastChar = engine.attack(player, lastX = lastX - 1, lastY);

			} catch (InvalidInstruction e) {
			}
			if (lastChar == 'V') {
				VerMidTop(player, lastX, lastY, lastChar);
			} else if (lastChar == 'T') {
				randomMode = true;
				playAs(player);
			}

		}
	}

	public void HorMidLeft(int player, int lastX, int lastY, char lastChar) {

		try {
			lastChar = engine.attack(player, lastX, lastY = lastY + 1);

		} catch (InvalidInstruction e) {
		}
		if (lastChar == 'H') {
			HorMidLeft(player, lastX, lastY, lastChar);
		}

		else if (lastChar == 'R') {
			randomMode = true;
			playAs(player);
		}

	}
	
	public void VerMidBottom(int player, int lastX, int lastY, char lastChar) {

		try {
			lastChar = engine.attack(player, lastX = lastX + 1, lastY);

		} catch (InvalidInstruction e) {
		}
		if (lastChar == 'V') {
			VerMidBottom(player, lastX, lastY, lastChar);
		}

		else if (lastChar == 'B') {
			randomMode = true;
			playAs(player);
		}

	}
	public void HorMidRight(int player, int lastX, int lastY, char lastChar) {

		try {
			lastChar = engine.attack(player, lastX, lastY = lastY - 1);

		} catch (InvalidInstruction e) {
		}
		if (lastChar == 'H') {
			HorMidRight(player, lastX, lastY, lastChar);
		}

		else if (lastChar == 'L') {
			randomMode = true;
			playAs(player);
		}

	}

public void VerMidTop(int player, int lastX, int lastY, char lastChar) {

	try {
		lastChar = engine.attack(player, lastX = lastX - 1, lastY);

	} catch (InvalidInstruction e) {
	}
	if (lastChar == 'V') {
		VerMidTop(player, lastX, lastY, lastChar);
	}

	else if (lastChar == 'T') {
		randomMode = true;
		playAs(player);
	}

}
}
