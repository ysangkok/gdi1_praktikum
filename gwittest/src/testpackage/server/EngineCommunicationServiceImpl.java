package testpackage.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import testpackage.client.EngineCommunicationService;
import testpackage.shared.ship.AI;
import testpackage.shared.ship.BadAI;
import testpackage.shared.ship.Engine;
import testpackage.shared.ship.Level;
import testpackage.shared.ship.Loser;
import testpackage.shared.ship.exceptions.InvalidInstruction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EngineCommunicationServiceImpl  extends RemoteServiceServlet implements
EngineCommunicationService {

	private static final long serialVersionUID = 1L;
	private Map<String,Game> games;
	private List<Player> waiting;

	public EngineCommunicationServiceImpl() {
		games = new HashMap<String,Game>();
		waiting = new ArrayList<Player>();
		
		new Thread(new Runnable(){public void run() {
			for (;;) {
			System.err.println("===");
			System.err.println("Games:");
			for (Game g : games.values()) {
				System.err.println(g.toString());
			}
			System.err.println("Waiting:");
			for (Player p : waiting) {
				System.err.println(p.toString());
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		}}).start();
	}
	
	public Loser getWinner() {
		return getEngine().checkWin();
	}
	
	private boolean isMultiplayer() {
		return getGame().isMultiplayer();
	}

	private Game getGame() {
		return games.get(this.getThreadLocalRequest().getSession(true).getId());
	}
	
	private Engine getEngine() {
		return getGame().getEngine();
	}
	private AI getAI() {
		//if (isMultiplayer()) throw new RuntimeException("cant get ai, not multiplayer");
		return ((SinglePlayerGame) getGame()).getAI();
	}
	
	@Override
	public Character[][] getVisibleOpponentArray() {
		if (!isMultiplayer())
			return getEngine().getVisibleOpponentArray();
		else
			return getUserBoards()[1];
	}

	@Override
	public Character[][] getPlayerArray() {
		Engine engine = getEngine();
		if (!isMultiplayer())
			return engine.getPlayerArray();
		else
			return getUserBoards()[0];
	}
	
	private Character[][][] getUserBoards() {
		return getEngine().getCurrentBoards(((MultiPlayerGame) getGame()).getPlayerNum(this.getThreadLocalRequest().getSession(true)),true);
	}

	@Override
	public Level getCurrentLevel() {
		return getEngine().getState().getLevel();
	}

	@Override
	public void tryAttackPoint(int x, int y) throws InvalidInstruction {
		if (isMultiplayer()) {
			getEngine().attack(((MultiPlayerGame) getGame()).getPlayerNum(this.getThreadLocalRequest().getSession(true)), x, y);
		} else {
			getEngine().attack(0, x, y);
			getAI().playAs(1);
		}
	}

	@Override
	public Integer[] initEngine(boolean multiplayer) {
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		
		Game game;
		
		if (multiplayer) {
			if (waiting.size() > 0) {
				Player p = waiting.get(0);
				waiting.remove(0);
				Player thisplayer = new Player(session.getId());
				game = new MultiPlayerGame(p, thisplayer);
				p.setGame(game);
				thisplayer.setGame(game);
				synchronized (p) {
					p.notify();
				}
			} else {
				Player p = new Player(session.getId());
				waiting.add(p);
				try {
					synchronized (p) {
						p.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game = p.getGame();
			}
		} else {
			game = new SinglePlayerGame(new Player(session.getId()));
			
		}
		
		games.put(session.getId(),game);
		return new Integer[] {game.getEngine().getxWidth(), game.getEngine().getyWidth()};
	}

	interface Game {
		Engine getEngine();
		boolean isMultiplayer();
	}
	
	class SinglePlayerGame implements Game {

		AI ai;
		Engine engine;
		
		public boolean isMultiplayer() { return false; }
		
		public SinglePlayerGame(Player player) {
			engine = new Engine();
			ai = new BadAI();
			ai.setEngine(engine);	
		}

		public AI getAI() {
			return ai;
		}

		public Engine getEngine() {
			return engine;
		}
		
	}
	
	class MultiPlayerGame implements Game {
		Engine engine;
		Player p1;
		Player p2;
		
		public boolean isMultiplayer() { return true; }
		
		public Engine getEngine() { return engine; }
		
		public MultiPlayerGame(Player p1, Player p2) {
			System.err.println("New game");
			this.engine = new Engine();
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public int getPlayerNum(HttpSession session) {
			String id = session.getId();
			if (id == this.p1.id)
				return 0;
			else if (id == this.p2.id)
				return 1;
			else
				throw new RuntimeException("player not in game");
		}
		public String toString() {
			return getClass().getName() + ": " + p1.toString() + " vs " + p2.toString() + ". Turn: " + engine.getState().getTurn();
		}
	}
	class Player {
		public String id;
		public Game game;
		
		public Player(String id) {
			System.err.println("New Player: " + id);
			this.id = id;
		}

		public void setGame(Game game) {
			this.game = game;
		}

		public Game getGame() {
			return game;
		}
		public String toString() {
			return "Player(" + id + ")";
		}
	}
}
