package gruppe16;

import gruppe16.exceptions.InvalidInstruction;

import java.util.Random;

public class BadAI extends AI {

	Random gen;
	int xwidth;
	int ywidth;
	Engine engine;
	
	BadAI(Engine engine) {
		gen = new Random();
		xwidth = engine.getxWidth();
		ywidth = engine.getyWidth();
		this.engine = engine;
	}
	
	@Override
	public void playAs(int player) {
		assert !engine.isFinished();
		try {
			engine.attack(player, gen.nextInt(xwidth), gen.nextInt(ywidth));
		} catch (InvalidInstruction e) {
			playAs(player);
		}
		
	}

	
}
