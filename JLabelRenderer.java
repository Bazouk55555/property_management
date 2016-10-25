import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.awt.Component;
import java.util.EventObject;


//CTRL + SHIFT + O pour générer les imports
public class JLabelRenderer implements TableCellEditor, TableCellRenderer{
	JLabel label;
	
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		label=new JLabel(value.toString());
		JScrollPane scroll=new JScrollPane(label);
		return scroll;
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		label=new JLabel(value.toString());
		JScrollPane scroll=new JScrollPane(label);
		return scroll;
	}
}

