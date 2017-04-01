package modelo;

import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
 
/**
 *
 * @author AUXPLANTA
 */
public class CustomTableModel extends DefaultTableModel {
    
    private final JTable tabla;
    private final Class[] columnClass;
    private final Boolean[] columnEditable;
    
    ColorRowJTable.ColorRowInJTable colorRow = new ColorRowJTable.ColorRowInJTable();
    
    public CustomTableModel(Object rowData[][], Object columnNames[], JTable table, Class[] columnClass, Boolean[] columnEditable) {
        super(rowData, columnNames);
        this.tabla = table;
        this.columnClass = columnClass;
        this.columnEditable = columnEditable;
        
        tabla.setFont(new Font("Ebrima", Font.PLAIN, 12));
        tabla.setGridColor(new java.awt.Color(227, 227, 227));
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabla.setCellSelectionEnabled(true);
        tabla.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectNextColumnCell");
        tabla.setSurrendersFocusOnKeystroke(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);       
        tabla.setDragEnabled(true);
        tabla.setModel(this);
        
        tabla.setDefaultRenderer(Object.class, colorRow);
        tabla.setDefaultRenderer(Integer.class, colorRow);
        tabla.setDefaultRenderer(java.util.Date.class, colorRow);
        tabla.setDefaultRenderer(Double.class, colorRow);
    }

      @Override
      public Class getColumnClass(int col) {        
            return columnClass[col];
      }

      @Override
      public boolean isCellEditable(int row, int col) {
        return columnEditable[col];
      }
      
      public static String[] getColumnsName(){
          return new String[]{"ID","CLIENTE","CIUDAD","LOTE"};
      }
      
      public DefaultCellEditor getComboBox(){
        Object options[] = { "UNO","DOS","TRES","CUATRO" };
        JComboBox comboBox = new JComboBox(options);
        comboBox.setMaximumRowCount(4);
        return new DefaultCellEditor(comboBox);
      }
      
}