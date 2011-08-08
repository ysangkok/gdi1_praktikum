package testpackage.highscore;

import javax.swing.JFrame;



import java.applet.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
public class ScoreTable extends JFrame {
	
	

	public static final int WIDTH = 400;
	
	public static final int HEIGHT = 300;
	
	public String[] columnNames =  {
			
			"Player:", "Score:", "needed time:" , "Date:"
	};
	
	
	
	private ArrayList<Score> playerList = null;
	
	
	
	
	public void HighScoreData(ArrayList<Score> playerList){
		
		this.playerList= playerList;
		
		
	}
	
public ScoreTable(String HighScore ){
		
		super (HighScore);
		
	}
	


	@override
	
	public String getColumnName(int column){
		return this.columnNames[column];
		
	}
	@override
	public int getColumnCount(){
		
		return this.columnNames.length;
	}
	
	
	@override
	public int getRowCount(){
		
		return playerList.size();
	}
	
	
	@override
	
	public Object getValueAt(int row, int col) {
		
		
		switch(col){
		
		case 0: return playerList.get(row).getName();
		case 1: return playerList.get(row).getScore();
		case 2: return playerList.get(row).getNeededTime();
		case 3: return playerList.get(row).getDate();
		}
		return null;
	}
	
	
	public void PanelResult(){
	Container pane = getContentPane();
	
	JLabel sortingorderlabel= new JLabel("choose the sorting order");
	
	
	pane.add(sortingorderlabel);
		
	}
		
	
	
	
	
	public static void main(String args[]){
	ScoreTable theWindow= new ScoreTable("High Score Table");
	
	theWindow.setSize(WIDTH, HEIGHT);
	theWindow.setVisible(true);
	
	System.out.println("exiting main...");
		
		
		
	}
	
	
	

	
	
	
	
}


