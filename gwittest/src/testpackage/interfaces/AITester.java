package testpackage.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import testpackage.shared.ship.AI;
import testpackage.shared.ship.BadAI;
import testpackage.shared.ship.Engine;
import testpackage.shared.ship.GoodAI;
import testpackage.shared.ship.IntelligentAI;
import testpackage.shared.ship.LevelGenerator;
import testpackage.shared.ship.Rules;
import testpackage.shared.ship.exceptions.InvalidLevelException;

/**
 * class for testing AI's. plays lots of matches between two ai's and outputs on stderr the amount of matches won by each ai
 */
public class AITester {
	boolean range;
	boolean ammo;
	
	private List<Class<? extends AI>> competitors; 

	/**
	 * takes the two competing ai's
	 * @param arg1 competing ai
	 * @param arg2 another competing ai
	 */
	public AITester(boolean ammo, boolean range, final Class<? extends AI> arg1, final Class<? extends AI> arg2) {
		this.ammo=ammo;
		this.range=range;
		
		long start = System.currentTimeMillis();
		
		System.err.println(String.format("New Tester: %s vs %s", arg1.getName(), arg2.getName()));
		competitors = new ArrayList<Class<? extends AI>>();
		competitors.add(arg1);
		competitors.add(arg2);
		System.err.println("AI1 as white");
		run();
		Collections.reverse(competitors);
		System.err.println("AI2 as white");
		run();
		
		System.err.format("Time taken: %d\n", System.currentTimeMillis() - start);
	}

	private void run2() {
		int ai1wins = 0;
		int ai2wins = 0;
		for (int i = 0; i<40; i++) {
		int winner = doMatch();
		if (winner == 0)
			ai1wins++;
		else if (winner == 1)
			ai2wins++;
		else
			System.err.println("Draw!");
		}
		System.err.println(competitors.get(0).getName() + ": " + ai1wins);
		System.err.println(competitors.get(1).getName() + ": " + ai2wins);
		System.err.flush();
	}
	
	/**
	 * plays a lot of matches and outputs result
	 */
	private void run() {
		//System.err.println(doMatch());
		//return;
		
		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	    try {
	        for (int i = 1; i <= 50; i++) {
	            exec.submit(new Runnable() {
	                @Override
	                public void run() {
	            		int winner = doMatch();
	            		if (winner == 0)
	            			System.err.println("AI1 win!");
	            		else if (winner == 1)
	            			System.err.println("AI2 win!");
	            		else
	            			System.err.println("Draw!");
	            		System.err.flush();

	                }
	            });
	        }
	    } finally {
	        exec.shutdown();
	    }
	    try {
			exec.awaitTermination(100000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			System.err.println("Timeout");
		}

	}

	/**
	 * plays a single match
	 * @return the number of the winning player (0 or 1)
	 */
	private int doMatch() {
		Engine engine ;
		AI ai1;
		AI ai2;
		
		engine = new Engine(new LevelGenerator(Rules.defaultHeight+20,Rules.defaultWidth-5).getLevel());
		if (ammo) engine.enableShotsPerShip(5);
		if (range) engine.enableRange();
		try {
			ai1 = competitors.get(0).getConstructor().newInstance();
			ai2 = competitors.get(1).getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ai1.setEngine(engine);
		ai2.setEngine(engine);
		int winner = -1;

		while (!engine.isFinished()) {
			try {
				if (engine.getState().isPlayerTurn()) {
					ai1.playAs(0);
				} else {
					ai2.playAs(1);
				}
			} catch (InvalidLevelException e) {
				e.printBoard();
				System.err.println(e.getMessage());
				//System.err.println(engine.getLevelStringForPlayer(0));
				e.printStackTrace();
				System.exit(0);
			}
			winner = engine.checkWin().playernr;
		}

		return winner;

	}

	public static void main(final String[] args) {
		//new AITester(GoodAI.class, BadAI.class);
		new AITester(true, true, BadAI.class, BadAI.class);
		new AITester(false, false, BadAI.class, IntelligentAI.class);
	}

}
