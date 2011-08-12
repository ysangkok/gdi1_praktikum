package testpackage.client;

import testpackage.shared.ship.Level;
import testpackage.shared.ship.Loser;
import testpackage.shared.ship.exceptions.InvalidInstruction;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ECS")
public interface EngineCommunicationService extends RemoteService {
	Loser getWinner();
	Character[][] getVisibleOpponentArray();
	Character[][] getPlayerArray();
	Level getCurrentLevel();
	void tryAttackPoint(int x, int y) throws InvalidInstruction;
	Integer[] initEngine(boolean multiplayer);
}
