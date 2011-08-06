package testpackage.interfaces;

abstract public class SoundHandler {
	public enum Sound {
		shipAllShotUp
	}
	abstract public void playSound(Sound sound);
}
