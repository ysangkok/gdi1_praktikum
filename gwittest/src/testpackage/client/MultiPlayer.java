package testpackage.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testpackage.shared.ship.Loser;
import testpackage.shared.ship.exceptions.InvalidInstruction;
import testpackage.shared.ship.Level;
import testpackage.shared.ship.TemplateImages;
import testpackage.shared.ship.Util;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MultiPlayer implements EntryPoint {
	
	static int xwidth;
	static int ywidth;
	
	private final EngineCommunicationServiceAsync ecs = GWT
			.create(EngineCommunicationService.class);
	
	public MultiPlayer() {
		((ServiceDefTarget) ecs).setServiceEntryPoint( GWT.getModuleBaseURL() + "/ECS");
	}
	
	private Label lbl = new Label();
	
	private Map<Image, Coord> buttocoord;
	private Image[][][] buttons;
	
	static class Coord {
		int x;
		int y;
		int p;
		
		Coord(int p, int x, int y) {
			this.p = p;
			this.x = x;
			this.y = y;
		}
		
		public String toString() {
			return Util.format("(%d,%d,%d)",p,x,y);
		}
	}
	
	private ClickHandler fieldclick = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			
			//GWT.log(buttocoord.get(event.getSource()).toString());
			final Coord point = buttocoord.get(event.getSource());
			
			if (point.p == 0) return;
			
			//if (!engine.getState().isPlayerTurn()) engine.getState().changeTurn();
			
				try {
					ecs.tryAttackPoint(point.x, point.y, new AsyncCallback<Void>() {

						@Override
						public void onFailure(final Throwable caught) {
							userError(caught.getMessage());
						}

						@Override
						public void onSuccess(Void nothing) {
							userError(Util.format("Attacked %s",point.toString()));
							updateBoards();
						}});
				} catch (InvalidInstruction e) {
					userError(e.getMessage());
				}
						
		}
	};

	public void onModuleLoad() {
		ecs.initEngine(true, new AsyncCallback<Integer[]>() {

			@Override
			public void onFailure(Throwable caught) {
				userError(caught.getMessage());
			}

			@Override
			public void onSuccess(Integer[] dimensions) {
				MultiPlayer.xwidth = dimensions[0];
				MultiPlayer.ywidth = dimensions[1];
				userError(Arrays.toString(dimensions));
				initGUI();
				
				Timer t = new Timer() {
					public void run() {
						GWT.log("Updating boards");
						updateBoards();
					}
				};

				t.scheduleRepeating(1000);
			}});
	}
	
	private void initGUI() {
		buttocoord = new HashMap<Image, Coord>();
		
		buttons = new Image[2][xwidth][ywidth];
		
		for (int p = 0; p <= 1; p++) {

			for (int i=0; i<xwidth; i++) {
				HorizontalPanel hpanel = new HorizontalPanel();
				for (int j=0; j<ywidth; j++) {

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
		}
		updateBoards();
		RootPanel.get("errorLabelContainer").add(lbl);

	}

	private void updateBoards() {
		List<AsyncCallback<Character[][]>> ac = new ArrayList<AsyncCallback<Character[][]>>(2);
		
		for (final int p : new int[]{0, 1}) {
			
			ac.add(p, new AsyncCallback<Character[][]>() {

				@Override
				public void onFailure(Throwable caught) {
					userError(caught.getMessage());

				}

				@Override
				public void onSuccess(Character[][] result) {
					for (int i=0; i<xwidth; i++) {
						for (int j=0; j<ywidth; j++) {
							updateField(new Coord(p,i,j), result[i][j]);
						}
					}
				}
			});
		}
		
		ecs.getPlayerArray(ac.get(0));
		ecs.getVisibleOpponentArray(ac.get(1));

	}

	private void GameOver() {
		ecs.getWinner(new AsyncCallback<Loser>(){

			@Override
			public void onFailure(Throwable caught) {
				userError(caught.getMessage());
				
			}

			@Override
			public void onSuccess(Loser loser) {
				String message = Util.format("Loser: %d, Reason: %s", loser.playernr, loser.reason);
				userError("Game over! " + message);
			}});
		
	}

	protected void userError(String message) {
		lbl.setText(System.currentTimeMillis() + ": " + message);
		
	}

	protected void updateField(Coord point, char c) {
		buttons[point.p][point.x][point.y].setUrl(TemplateImages.webimagesdir + TemplateImages.fieldToIcon(c) + ".png");
		
	}
}
