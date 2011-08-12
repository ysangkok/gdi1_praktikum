package testpackage.highscore;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import testpackage.highscore.HighscoreManager;
import testpackage.highscore.Score;

class ColumnSorter implements Comparator {
    int colIndex;
    boolean ascending;
    ColumnSorter(int colIndex, boolean ascending) {
        this.colIndex = colIndex;
        this.ascending = ascending;
    }
    public int compare(Object a, Object b) {
        Vector v1 = (Vector)a;
        Vector v2 = (Vector)b;
        Object o1 = v1.get(colIndex);
        Object o2 = v2.get(colIndex);

        // Treat empty strains like nulls
        if (o1 instanceof String && ((String)o1).length() == 0) {
            o1 = null;
        }
        if (o2 instanceof String && ((String)o2).length() == 0) {
            o2 = null;
        }

        // Sort nulls so they appear last, regardless
        // of sort order
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else if (o1 instanceof Comparable) {
            if (ascending) {
                return ((Comparable)o1).compareTo(o2);
            } else {
                return ((Comparable)o2).compareTo(o1);
            }
        } else {
            if (ascending) {
                return o1.toString().compareTo(o2.toString());
            } else {
                return o2.toString().compareTo(o1.toString());
            }
        }
    }
}

//Ein Renderer für java.awt.Point
class DateRender extends DefaultTableCellRenderer{
 @Override
 public Component getTableCellRendererComponent( JTable table, Object value, 
         boolean isSelected, boolean hasFocus, int row, int column ) {
     Date point = (Date)value;
     String text = point.toString();
     return super.getTableCellRendererComponent( table, text, isSelected,
         hasFocus,  row, column );
 }
}

public class TestHighScore extends JFrame {
	private static final long serialVersionUID = -2542984839574589527L;
	private List<Score> highscoreList;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
	    Vector data = model.getDataVector();
	    Collections.sort(data, new ColumnSorter(colIndex, ascending));
	    model.fireTableStructureChanged();
	}
	
	public TestHighScore(List<Score> highscores){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.highscoreList = highscores;
		
		this.setLayout(null);
		//this.setSize(300, 320);

		this.getContentPane().setBackground(Color.white);
		
		String[] columnNames = {"#", "Name", "Score", "Needed time",  "Date"};
		tableModel = new DefaultTableModel(null, columnNames){
            @Override
            public Class<?> getColumnClass( int column ) {
                switch( column ){
                    case 0: return Integer.class;
                    case 1: return String.class;
                    case 2: return Integer.class;
                    case 3: return Integer.class;
                    default: return Date.class;
                }
            }
		};;
		table = new JTable(tableModel);
		//table.setAutoCreateRowSorter(true);
		table.setAutoCreateRowSorter(false);

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();

		// Der Sorter muss dem JTable bekannt sein
		table.setRowSorter( sorter );
		sorter.setModel( tableModel );
		
		table.setDefaultRenderer( Date.class, new DateRender());

		sorter.setComparator(4, new Comparator(){

			@Override
			public int compare(Object arg0, Object arg1) {
				System.err.println(arg0);System.err.println(arg1);
				return ((Date)arg0).compareTo((Date)arg1);
			}});
		
		sortAllRowsBy(tableModel, 3, true);

		getHighScoreManager();

		table.setBounds(0, 20, 350, 270);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowHeight(25);
		table.setEnabled(false);
		table.setShowGrid(false);
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getTableHeader().setBounds(0, 0, 350, 20);
		this.add(table.getTableHeader());
		this.add(table);

		setTableData();
		this.setSize(500,500);
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
	
	
	/**
	 * 
	 * this method opens a dialog window for the player to add his name, after ending the game with a score of the top 10.
	 * 
	 * @return name of player
	 */
    String SetPlayerName(){
    	return (String)JOptionPane.showInputDialog(
    	                    new JFrame(),
    	                    "Complete the sentence:\n"
    	                    + "\"Green eggs and...\"",
    	                    "Customized Dialog",
    	                    JOptionPane.PLAIN_MESSAGE,
    	                    null,
    	                    null,
    	                  null);
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



