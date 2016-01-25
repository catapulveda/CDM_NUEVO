/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author AUXPLANTA
 */
public class Metodos {
    
    public static void packColumn(JTable table) {
        try{
            for(int coli=0; coli<table.getColumnCount(); coli++){
            TableModel model = table.getModel();
            DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
            TableColumn col = colModel.getColumn(coli);
            int width = 0;
            TableCellRenderer renderer = col.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            width = comp.getPreferredSize().width;
            
            for (int r=0; r<table.getRowCount(); r++) {
                try{
                    renderer = table.getCellRenderer(r, coli);
                    comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, coli), false, false, r, coli);
                    width = Math.max(width, comp.getPreferredSize().width);
                }catch(Exception ext){System.err.println("ERROR EN EL METODO PACK COLUMN - LAS COLUMNAS NO PUEDEN REDIMENSIONARSE.\t\t"+ext);}
            }
            width += (2*1)+10;
            col.setPreferredWidth(width);
            }
        }catch(Exception e){
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("ERROR EN EL METODO PACK COLUMN\n"+e);}
    }
    
}
