package controller;

import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import model.Lote;
import view.Lotes;

/**
 *
 * @author AUXPLANTA
 */
public class LotesController {
    
    Lotes frameLotes;
    CustomTableModel modelo;
    private TableRowSorter rowSorter;
    
    public LotesController(Lotes lotes){
        this.frameLotes = lotes;
        
        //CREA EL MODODELO CON LAS COLUMNAS Y LO ASIGNA A LA TABLA
        modelo = new CustomTableModel(new String[][]{}, model.Lote.getColumnNames(), frameLotes.tablaLotes, Lote.getColumnClass());
        
        //LLENA EL MODELO CON LOS DATOS DESDE LA BASE DE DATOS.
        Lote.cargarLotes(modelo, "");
        
        //ASIGNAR EL TableRowSorter AL MODELO PARA LA BUSQUEDA EN LA TABLA
        rowSorter = new TableRowSorter(modelo);
        frameLotes.tablaLotes.setRowSorter(rowSorter);
        
        //REDIMENSIONA NUEVAMENTE EL ANCHO DE LAS COLUMANS SEGUN EL CONTIDO DE LAS CELDAS
        model.Metodos.packColumn(frameLotes.tablaLotes);
        
        //MAXIMIZA LA VENTANA
        frameLotes.setExtendedState(MAXIMIZED_BOTH);
        
        //EVENTO PARA RECARGAR LA TABLA CON LOS DATOS DESDE LA BASE DE DATOS.
        frameLotes.btnCargarLotes.addActionListener((ActionEvent e) -> {
            for (int i = modelo.getRowCount()-1; i>=0; i--) {
                modelo.removeRow(i);
            }
            Lote.cargarLotes(modelo, "");
        });
        
        frameLotes.cjBuscarLote.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt){
                
//                if(evt.getKeyChar() != KeyEvent.VK_BACK_SPACE){
                    rowSorter.setRowFilter(RowFilter.regexFilter(frameLotes.cjBuscarLote.getText().toUpperCase(), 1));
//                }
            }
        });
        
    }
    
}
