package testpackage.shared.ship;

public class Loser {
	public String reason;
	public int playernr;
	
	Loser(String reason, int playernr) {
		this.reason = reason;
		this.playernr = playernr;
	}
	
	Loser(int playernr) {
		this.playernr = playernr;
	}
}