package testpackage.highscore;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class TestHighScore extends JFrame {
	private static final long serialVersionUID = -2542984839574589527L;
	private List<Score> highscoreList;
	private JTable table;
	private DefaultTableModel tableModel;
	
	
	
	public TestHighScore(List<Score> highscores){
		this.highscoreList = highscores;
		
		this.setLayout(null);
		this.setSize(300, 320);
		this.getContentPane().setBackground(Color.white);
		
		String[] columnNames = {"#", "Name", "Score", "Needed time",  "Date"};
		tableModel = new DefaultTableModel(null, columnNames);
		table = new JTable(tableModel);
		
		getHighScoreManager();
		
		table.setBounds(0, 20, 290, 270);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowHeight(25);
		table.setEnabled(false);
		table.setShowGrid(false);
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getTableHeader().setBounds(0, 0, 290, 20);
		this.add(table.getTableHeader());
		this.add(table);
		
		setTableData();
		
		this.setVisible(true);
	}

	HighscoreManager hm;
	
	private void getHighScoreManager(){
		hm = new HighscoreManager();
		hm.loadScoreFile();
		
	}
	public void setTableData(){
		
		tableModel.getDataVector().removeAllElements();
		
		int i = 0;
		for(Score score : this.highscoreList){
			tableModel.insertRow(i, new Object[]{i+1, score.getName(), score.getScore(), score.getNeededTime(), score.getDate()});
	 		i++;
		}
	}
	
	public static void main(String [] args){
	HighscoreManager hm;
	
	hm = new HighscoreManager();
	hm.setScores(new LinkedList<Score>());
	hm.updateScoreFile();
	
    hm = new HighscoreManager();
    hm.loadScoreFile();
    hm.setDateFormat("dd.MM.yyyy   HH:mm");
    hm.addScore("Bart",240, 30,   "01.08.2011   08:01"  );
    hm.addScore("Marge",300, 22,  "11.08.2011   09:01" );
    hm.addScore("Maggie",220, 45, "01.07.2011   10:01" );
    hm.addScore("Homer",100, 33,  "21.08.2011   11:01" );
    hm.addScore("Lisa",270,  22,  "01.11.2011   08:01" );
    hm.updateScoreFile();
    
    System.out.print(hm.getHighscoreString());
    System.out.println("===");
    
	hm = new HighscoreManager();
    hm.loadScoreFile();
    hm.setDateFormat("dd.MM.yyyy   HH:mm");
    
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
    System.out.print(hm.getHighscoreString());

    hm.sort("neededTime", false);
    System.out.println("===");
    System.out.print(hm.getHighscoreString());

    TestHighScore ths = new TestHighScore(hm.getScores());
}

}



