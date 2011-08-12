package testpackage.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import testpackage.shared.ship.Level;
import testpackage.shared.ship.Loser;
import testpackage.shared.ship.exceptions.InvalidInstruction;

public interface EngineCommunicationServiceAsync {

	void getWinner(AsyncCallback<Loser> callback);

	void getVisibleOpponentArray(AsyncCallback<Character[][]> callback);

	void getPlayerArray(AsyncCallback<Character[][]> callback);

	void getCurrentLevel(AsyncCallback<Level> callback);

	void tryAttackPoint(int x, int y, AsyncCallback<Void> callback) throws InvalidInstruction;

	void initEngine(boolean multiplayer, AsyncCallback<Integer[]> asyncCallback);

}
