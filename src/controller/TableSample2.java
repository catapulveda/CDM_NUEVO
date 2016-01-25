package controller;

/**
 *
 * @author AUXPLANTA
 */
import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class TableSample2 {
  public static void main(String args[]) {
    Object rows[][] = { { "one", "ichi - \u4E00", "unhhhhh" },
        { "two", "ni - \u4E8C", "deuxhhhh" },
        { "three", "san - \u4E09", "troishhh" },
        { "four", "shi - \u56DB", "quatrehhh" },
        { "five", "go - \u4E94", "cinqhhh" },
        { "six", "roku - \u516D", "treizahhh" },
        { "seven", "shichi - \u4E03", "septhhh" },
        { "eight", "hachi - \u516B", "huithhh" },
        { "nine", "kyu - \u4E5D", "neufhhh" },
        { "ten", "ju - \u5341", "dixhhh" }
    };
    Object options[] = { "un", "deux", "trois", "quatre", "cinq", "treiza",
        "sept", "huit", "neuf", "dix" };
    JComboBox comboBox = new JComboBox(options);
    comboBox.setMaximumRowCount(4);
    TableCellEditor editor = new DefaultCellEditor(comboBox);

    Object headers[] = { "English", "Japanese", "French" };
    JFrame frame = new JFrame("JTable Anatomy");
    
    class CustomTableModel extends DefaultTableModel {
        
      public CustomTableModel(Object rowData[][], Object columnNames[]) {
        super(rowData, columnNames);
      }

      @Override
      public Class getColumnClass(int col) {
        Vector v = (Vector) dataVector.elementAt(0);
        return v.elementAt(col).getClass();
      }

      @Override
      public boolean isCellEditable(int row, int col){
        return true;
      }
    }
    JTable table = new JTable(new DefaultTableModel(rows, headers));

    //    ColumnModelUtilities.removeHeaders(table.getColumnModel());
    table.getColumnModel().getColumn(2).setCellEditor(editor);

    JScrollPane scrollPane = new JScrollPane(table);
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setSize(300, 150);
    frame.setVisible(true);
  }
}
