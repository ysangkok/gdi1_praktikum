package testpackage.server;

import java.util.ArrayList;
import java.util.List;

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
	private Engine engine;
	private BadAI ai;
	private List<Game> games;
	private List<Player> waiting;

	public EngineCommunicationServiceImpl() {
		games = new ArrayList<Game>();
		waiting = new ArrayList<Player>();
	}
	
	@Override
	public Loser getWinner() {
		return getEngine().checkWin();
	}

	private Engine getEngine() {
		return (Engine) get("engine");
	}
	private AI getAI() {
		return (AI) get("ai");
	}
	private Object get(String ting) {
		return this.getThreadLocalRequest().getSession().getAttribute(ting);
	}
	
	@Override
	public Character[][] getVisibleOpponentArray() {
		return ((Engine) get("engine")).getVisibleOpponentArray();
	}

	@Override
	public Character[][] getPlayerArray() {
		Engine engine = getEngine();
		return engine.getPlayerArray();
	}

	@Override
	public Level getCurrentLevel() {
		return getEngine().getState().getLevel();
	}

	@Override
	public void tryAttackPoint(int x, int y) throws InvalidInstruction {
		if ((Boolean) get("multiplayer")) {
			getEngine().attack((Integer) get("player"), x, y);
		} else {
			getEngine().attack(0, x, y);
			getAI().playAs(1);
		}
		
	}

	@Override
	public Integer[] initEngine(boolean multiplayer) {
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		
		if (multiplayer) {
			if (waiting.size() > 0) {
				Player p = waiting.get(0);
				synchronized (p) {
					p.notify();
				}
				games.add(new Game(p, new Player(session.getId())));
				session.setAttribute("player", 1);
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
				session.setAttribute("player", 0);
			}
			engine = new Engine();
		} else {
			engine = new Engine();
			ai = new BadAI();
			ai.setEngine(engine);
			session.setAttribute("ai", ai);
			games.add(new Game(new Player(session.getId())));
		}
		session.setAttribute("multiplayer", multiplayer);
		session.setAttribute("engine", engine);
		return new Integer[] {engine.getxWidth(), engine.getyWidth()};
	}

	class Game {
		public Game(Player p, Player player) {
			System.err.println("New game");
		}

		public Game(Player player) {
			//single player
		}
	}
	class Player {
		Player() {
		}
		
		public Player(String id) {
			System.err.println("New Player: " + id);
		}
	}
}
