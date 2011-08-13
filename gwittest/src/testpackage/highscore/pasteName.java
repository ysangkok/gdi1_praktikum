package testpackage.highscore;

import java.awt.Component;
import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import testpackage.shared.ship.Level;

public class pasteName {
	
	
	
//	private static final int ResultScore = 0;
//	private LinkedList<Score> scores;
//    private int Score;
//    
//    
//    
//    
//    /**
//     * Constructor
//     * 
//     * 
//     */
//    public pasteName (){
//    	
//
//    	
//    }
//    
//
//	public void getScoreFromList(LinkedList<Score> scores){
//	
//		
//		scores = new LinkedList<Score>();
//		int score = new ; 
//		int resultScore = score;
//	
//		
//	
//		if(scores.size() < 10 ){
//	
//			if(resultScore > scores.getLast().getScore())
//			{
//				
//				scores.add(scores.getLast());
//				
//				
//			}
//			
//			
//		}
//	}
//	public void setScores(List<Score> scores) {
//		this.scores = scores;
//	}
//	

	//Object[] options = {"ok", "cancel"} ;
	
	
	static String askForName() {
		Frame frame = new Frame("dialog");
		String result = JOptionPane.showInputDialog(frame, null,"Enter your name");
		return result;
	}
	
	
	
	public static void main(String[] args) {
		String date="30.02.1999   06:36";
		int neededTime=3289;
		Level level = new Level("**-T**T***|*T-T**T***\n--*V**V***|*V*V**V-**\nT**V**V-**|*V*V**B-**\nV**V**B**-|*V*B**-T*T\nB-*B***LR*|*B**LR*V*B\n**T*T*****|**-****B**\n*-B*V-LHR*|T***-*****\n***-V****-|V***LHHR*T\nLHR*B**LR-|B----*---B\n-*-**lR*-*|**-**LR***");
		
		maybeAddHighScore(date, neededTime, level);

	}



	public static void maybeAddHighScore(String date, int neededTime, Level level) {
		Character[][] playerboard = level.getPlayerBoard(0);
		//System.err.println(level.toString());
		
		int score = calculateScore(level);
		
		HighscoreManager hm = new HighscoreManager();
		hm.setDateFormat("dd.MM.yyyy   HH:mm");
	    //hm.addScore("Bart",240, 30,   "01.08.2011   08:01"  );
		hm.loadScoreFile();
		
		if (hm.getWorstScore() < score || hm.getScores().size() < hm.getMaxHighScoreListElements()) {
			String name = askForName();
			hm.addScore(name, score, neededTime, date);
		} else {
			System.err.println("Score war zu schlecht.");
		}
		hm.updateScoreFile();
		TestHighScore ths = new TestHighScore(hm.getScores());
	    
	}


	
	
	

	private static int gethitsofharmedship(Level level) {
	
		Character[][] board = level.getPlayerBoard(1);
		Character[][] board2 = level.getPlayerBoard(0);

		
		
		int hits = 0 ;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (Level.matchChar(Level.harmedShip, board[i][j])) {
					hits++;
				}
			}
		}
		
		System.err.println(hits);
		return hits;	
		}
	
	private static int gethitsofwater(Level level) {

		Character[][] board = level.getPlayerBoard(1);
		Character[][] board2 = level.getPlayerBoard(0);

		
	int hitwater = 0 ;
	for(int i = 0; i <  board.length; i++){
		for (int j=0 ; j < board.length ; j++){
			if ('*' == board[i][j]) {
				hitwater++;
			}
		}
	} 
	
	System.err.println(hitwater);
	return hitwater;
	}
}


