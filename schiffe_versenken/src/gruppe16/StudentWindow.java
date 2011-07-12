package gruppe16;

import de.tu_darmstadt.gdi1.battleship.ui.GamePanel;
import de.tu_darmstadt.gdi1.battleship.ui.GameWindow;

public class StudentWindow extends GameWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentWindow(String windowTitle) {
		super(windowTitle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GamePanel createGamePanel() {
		StudentPanel panel = new StudentPanel(this);
		add(panel);
		return panel;
	}

}
