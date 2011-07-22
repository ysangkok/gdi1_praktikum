package gruppe16;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ScoreTableModel implements TableModel {
	
	private final int SCORE_ENTRY_LIMIT = 10;
	
	Object[] titel = null;
	
	Object[] scoreModel = null;
	
	@Override
	public void addTableModelListener(TableModelListener l) {
			
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		
		return ;
	}

	@Override
	public int getColumnCount() {
		if (this.scoreModel != null) {
			
		}
		return 0;
	}

	@Override
	public String getColumnName(int columnIndex) {
		
		return null;
	}

	@Override
	public int getRowCount() {
		if(scoreModel != null)
			return scoreModel.length;
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}
	
}
