package testpackage.interfaces;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import testpackage.shared.ship.Rules;

/**
 * panel handles display AND logic
 */
class CountdownTimerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
// http://leepoint.net/notes-java/examples/animation/41TextClock/25textclock.html
	
    private JTextField _timeField;  // set by timer listener
    private long endtime;
    private long timeleft;
    private GUISchiffe app;
    /**
     * swing timer used measuring time. used in constructor
     */
    javax.swing.Timer t;

    void go() {
    	t.start();
    }
    
    /**
     * @param app guischiffe app so that we can change turn and attack when timer expires
     */
    public CountdownTimerPanel(GUISchiffe app) {
    	this.app = app;
    	
        _timeField = new JTextField(Rules.standardSpeerfeuerTime/1000);
        _timeField.setEditable(false);
        _timeField.setFont(new Font("sansserif", Font.PLAIN, 48));

        this.setLayout(new FlowLayout());
        this.add(_timeField); 
        
        resetCountdown();
        
        //    Use full package qualification for javax.swing.Timer
        //    to avoid potential conflicts with java.util.Timer.
        t = new javax.swing.Timer(50, new ClockListener());

    }
    
    /**
     * called at the point of time the user should have 5 seconds from
     */
    void resetCountdown() {
    	endtime = System.currentTimeMillis() + app.speerfeuertime;
    }
    
    /**
     * used for pausing the timer when the user opens a dialog window or similar that obstructs gameplay. would be unfair if timer was still running then.
     */
    void pause() {
    	t.stop();
    	timeleft = endtime - System.currentTimeMillis();
    }
    
    /**
     * used after pausing with pause()
     */
    void resume() {
    	endtime = System.currentTimeMillis() + timeleft;
    	t.start();
    }
    
    /**
     * this actionlistener is used for handling the clock timeout every 50 milliseconds
     */
    class ClockListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {

       		long nowtime = System.currentTimeMillis();

            _timeField.setText(String.format("%.1f", Math.abs((double) (endtime-nowtime))/1000)); // abs to prevent short minus interval when timer goes below 0
 
    		if (nowtime >= endtime) {
    			if (!app.engine.getState().isPlayerTurn()) return;
    			app.engine.getState().changeTurn();
    			app.aiAttackAs(1);
    			//app.userError("Time's up!");
    			//t.stop();
    		}
    	}
    }
}


