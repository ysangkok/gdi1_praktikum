package testpackage.interfaces;

import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.IOException;


public class SoundStreamPlayer { // http://www.javalobby.org/java/forums/t18465.html
	Object lock = new Object();

	volatile boolean paused = true;
	volatile boolean doRestart = false;

	public SoundStreamPlayer() {
		Thread pt = new Thread() {
			public void run() {
				runLoop();
			}
		};
		pt.start();
	}

	void pause() {
		System.err.println("Pausing");
		paused = true;
	}

	void play() {
		System.err.println("Playing");
		synchronized(lock) {
			paused = false;
			lock.notifyAll();
		}
	}

	void restart() {
		System.err.println("Restarting");
		doRestart = true;
		play();
	}

	void runLoop() {
		AudioInputStream din = null;
		try {
			BufferedInputStream soundStream = new BufferedInputStream(GUISchiffe.class.getResourceAsStream("/sounds/shipAllShotUp.mp3"));
			soundStream.mark(1);
			SourceDataLine line;

			outer: for (;;) {
			soundStream.reset();
			AudioInputStream in = AudioSystem.getAudioInputStream(soundStream);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			line = (SourceDataLine) AudioSystem.getLine(info);
			if(line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();
				
				int nBytesRead;

				synchronized (lock) {
					while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
						while (paused) {
							if(line.isRunning()) {
								line.stop();
							}
							try {
								lock.wait();
							}
							catch(InterruptedException e) {
								System.err.println("SoundSteamPlayer thread interrupted");
								break outer;
							}
						}
						if (doRestart) { doRestart = false; continue outer; }
	
						if(!line.isRunning()) {
							line.start();
						}
						line.write(data, 0, nBytesRead);
					}
				}

			}
			System.err.println("Done");
			pause();
			}//for(;;)
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(din != null) {
				try { din.close(); } catch(IOException e) { }
			}
		}
	}

public static void main( String[] args ) {
	final SoundStreamPlayer ssp = new SoundStreamPlayer();
	ssp.play();
	sleep(1000);
	ssp.restart();
	sleep(1000);
	ssp.pause();
	sleep(1000);
	ssp.play();
	sleep(10000);
	ssp.restart();
	sleep(10000);

}

private static void sleep(int count) {
	try {
	Thread.sleep(count);
	} catch (Exception e) {}
}
}
