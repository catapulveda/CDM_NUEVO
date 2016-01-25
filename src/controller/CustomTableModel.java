package controller;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Lote;

/**
 *
 * @author AUXPLANTA
 */
public class CustomTableModel extends DefaultTableModel {
    
    private JTable tabla;
    private Class[] columnClass;
    
    public CustomTableModel(Object rowData[][], Object columnNames[], JTable table, Class[] columnClass) {
        super(rowData, columnNames);
        this.tabla = table;
        this.columnClass = columnClass;
        
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        model.Metodos.packColumn(table);      
        tabla.setModel(this);               
    }

      @Override
      public Class getColumnClass(int col) {        
            return columnClass[col];
      }

      @Override
      public boolean isCellEditable(int row, int col) {
        return true;
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