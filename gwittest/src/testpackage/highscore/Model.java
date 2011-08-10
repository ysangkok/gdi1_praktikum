package testpackage.highscore;

import javax.swing.table.AbstractTableModel;

public class Model extends AbstractTableModel {

	
	
	public String getColumnName(int col) {
       
		return this.getcolumnNames();
        
	}

    
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return  rowData.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		return  rowData[row][col];
	}

	
	
	
	
	
}
