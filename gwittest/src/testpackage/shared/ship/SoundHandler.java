package testpackage.shared.ship;

abstract public class SoundHandler {
	public enum Sound {
		shipAllShotUp_mp3, Water_wav, Boom1_wav, Boom2_wav, Boom3_wav
	}
	abstract public void playSound(Sound sound);
	abstract public void playSound(Sound sound, int player);
}
