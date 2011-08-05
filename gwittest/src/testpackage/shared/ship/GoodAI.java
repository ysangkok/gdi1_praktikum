package testpackage.shared.ship;

import testpackage.shared.ship.exceptions.InvalidInstruction;

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
	
	
	boolean dirSouth = false;
	boolean dirNorth = false;
	boolean dirEast = false;
	boolean dirWest = false;
	
	int currentX = -1;
	int currentY = -1;
	char currentChar = '\0';
	
	char bottom = '\0';
	char top = '\0';
	
	public void setEngine(Engine engine) {
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
		}
		else {	
			direction(player);
		}
	}
		
	public void direction(int player){
		if (dirSouth){
			dirSouth(player);
		}
		else if (dirNorth){
			dirNorth(player);
		}
		else if (dirEast){
			dirEast(player);
		}
		else if (dirWest){
			dirWest(player);
		}
		else {
		
		if (lastX != 9){
		try {
			currentChar = engine.attack(player, currentX = lastX + 1, lastY);
			if (hitShip(currentChar)){
				dirSouth = true;
			}
			else {
				currentX = lastX;
				currentY = lastY;
				dirNorth = true;
			}
		}
		catch (InvalidInstruction e) {
			dirNorth = true;
			dirSouth = false;
			currentX = lastX;
			currentY = lastY;
			dirNorth(player);
		}}
		else if (lastX == 9){
			dirNorth = true;
			dirNorth(player);
		}
		}
		
		
		
	}
	
	
	public void dirSouth(int player){
		
		try {
			currentChar = engine.attack(player, currentX = currentX + 1, lastY);
			if (!hitShip(currentChar)){
				dirSouth = false;
				dirNorth = true;
				currentX = lastX;
				currentY = lastY;
			}
			
		}
		catch (InvalidInstruction e) {
			dirNorth = true;
			dirSouth = false;
			currentX = lastX;
			currentY = lastY;
			dirNorth(player);
		}
		
		
	}
	
	public void dirNorth(int player){
		
		try {
			currentChar = engine.attack(player, currentX = currentX - 1, lastY);
			
			if (!hitShip(currentChar)){
				dirNorth = false;
				dirEast = true;
				currentChar = lastChar;
			}
			
			if (currentChar == 'T'){
				dirNorth = false;
				randomMode = true;
				currentChar = '\0';
			}
			
		}
		catch (InvalidInstruction e) {
			randomMode = false;
			dirNorth = false;
			dirEast = true;
			
		}
		
	}
	
	public void dirEast (int player){
		
		try {
			currentChar = engine.attack(player, lastX, currentY = currentY + 1);
			if (!hitShip(currentChar)){
				dirEast = false;
				dirWest = true;
				currentChar = lastChar;
				currentY = lastY;
			}
		}
		catch (InvalidInstruction e) {
			randomMode = false;
			dirEast = false;
			dirWest = true;
			currentY = lastY;
			dirWest(player);
		}
		
		
		
	}
	
	public void dirWest (int player){
		
		try {
			currentChar = engine.attack(player, lastX, currentY = currentY - 1);
			
			if (!hitShip(currentChar)){
				dirWest = false;
				randomMode = true;
				currentChar = lastChar;
			}
			
			if (currentChar == 'L'){
				dirWest = false;
				randomMode = true;
				currentChar = '\0';
			}
			
		}
		catch (InvalidInstruction e) {
			dirWest = false;
			randomMode = true;
			currentChar = '\0';
		}
		}
		
	}