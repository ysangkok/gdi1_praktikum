package testpackage.client;

import java.util.HashMap;
import java.util.Map;

import testpackage.shared.ship.AI;
import testpackage.shared.ship.BadAI;
import testpackage.shared.ship.Engine;
import testpackage.shared.ship.Loser;
import testpackage.shared.ship.TemplateImages;
import testpackage.shared.ship.Util;
import testpackage.shared.ship.exceptions.InvalidInstruction;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientOnlySinglePlayer implements EntryPoint {
	
	Engine engine;
	AI computer;
	
	public ClientOnlySinglePlayer() {
		engine = new Engine();
		computer = new BadAI();
		computer.setEngine(engine);
	}
	
	private Label lbl = new Label();
	
	private Map<Image, Coord> buttocoord;
	private Image[][][] buttons;
	
	class Coord {
		int x;
		int y;
		int p;
		
		Coord(int p, int x, int y) {
			this.p = p;
			this.x = x;
			this.y = y;
		}
	}
	
	private ClickHandler fieldclick = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			
			//GWT.log(buttocoord.get(event.getSource()).toString());
			Coord point = buttocoord.get(event.getSource());
			
			if (point.p == 0) return;
			
			char c;
			
			//if (!engine.getState().isPlayerTurn()) engine.getState().changeTurn();
			
			try {
				engine.attack(Engine.otherPlayer(point.p), point.x, point.y);
			} catch (InvalidInstruction e) {
				userError(e.getMessage());
				return;
			}
			
			c = engine.getState().getLevel().getPlayerBoard(point.p)[point.x][point.y];
			
			updateField(point, c);
			
			aiAttackAs(point.p);
			
		}
	};
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		buttocoord = new HashMap<Image, Coord>();
		
		buttons = new Image[2][engine.getxWidth()][engine.getyWidth()];
		
		for (int p = 0; p <= 1; p++) {

			for (int i=0; i<engine.getxWidth(); i++) {
				HorizontalPanel hpanel = new HorizontalPanel();
				for (int j=0; j<engine.getyWidth(); j++) {

					final Image image = new Image();
					buttocoord.put(image, new Coord(p,i,j));
					
					buttons[p][i][j] = image;

					image.addErrorHandler(new ErrorHandler() {
						public void onError(ErrorEvent event) {
							lbl.setText("An error occurred while loading: " + event.toDebugString());
						}
					});
					
					image.addClickHandler(fieldclick);

					hpanel.add(image);

				}
				RootPanel.get("board" + p).add(hpanel);
			}
			refresh(p);
		}
		
		RootPanel.get("errorLabelContainer").add(lbl);
		

	}

	protected void aiAttackAs(int player) {
		int i = 0;
		while (!engine.getState().isPlayerTurn() && !engine.isFinished()) {
			GWT.log(i++ + " AI plays as " + player); // debug output to detect runaway AI
			computer.playAs(player);
			engine.checkWin();
		}
		refresh(Engine.otherPlayer(player));
		
		if (engine.isFinished()) { GameOver(); return; }
	}

	private void refresh(int p) {
		Character[][] board = null;
		
		if (p == 0) board = engine.getPlayerArray();
		if (p == 1) board = engine.getVisibleOpponentArray();

		for (int i=0; i<engine.getxWidth(); i++) {
			for (int j=0; j<engine.getyWidth(); j++) {
				updateField(new Coord(p,i,j), board[i][j]);
			}
		}
	}

	private void GameOver() {
		Loser loser = engine.checkWin();
		String message = Util.format("Loser: %d, Reason: %s", loser.playernr, loser.reason);
		userError("Game over! " + message);
	}

	protected void userError(String message) {
		lbl.setText(System.currentTimeMillis() + ": " + message);
		
	}

	protected void updateField(Coord point, char c) {
		buttons[point.p][point.x][point.y].setUrl(TemplateImages.webimagesdir + TemplateImages.fieldToIcon(c) + ".png");
		
	}
}
