package view;

import controller.LotesController;
/**
 *
 * @author AUXPLANTA
 */
public class Lotes extends javax.swing.JFrame {        
    
    public Lotes() {
        
        initComponents();
        
        new LotesController(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuTablaLotes = new javax.swing.JPopupMenu();
        subMenuAbrirLote = new javax.swing.JMenuItem();
        subMenuDarPorTerminado = new javax.swing.JMenuItem();
        subMenuPrepararDespacho = new javax.swing.JMenuItem();
        subMenuRegistrarDiferencias = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaLotes = new javax.swing.JTable();
        cjBuscarLote = new CompuChiqui.JTextFieldPopup();
        btnCargarLotes = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        subMenuAbrirLote.setText("jMenuItem1");
        menuTablaLotes.add(subMenuAbrirLote);

        subMenuDarPorTerminado.setText("jMenuItem2");
        menuTablaLotes.add(subMenuDarPorTerminado);

        subMenuPrepararDespacho.setText("jMenuItem3");
        menuTablaLotes.add(subMenuPrepararDespacho);

        subMenuRegistrarDiferencias.setText("jMenuItem4");
        menuTablaLotes.add(subMenuRegistrarDiferencias);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablaLotes.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        tablaLotes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaLotes.setComponentPopupMenu(menuTablaLotes);
        tablaLotes.setRowHeight(25);
        tablaLotes.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaLotes);
        tablaLotes.getAccessibleContext().setAccessibleName("tablaLotes");

        cjBuscarLote.setPlaceholder("Ingrese el nombre del cliente");

        btnCargarLotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/X16/actualizar.png"))); // NOI18N

        jMenu1.setText("Archivo");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cjBuscarLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCargarLotes)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCargarLotes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cjBuscarLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Lotes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCargarLotes;
    public CompuChiqui.JTextFieldPopup cjBuscarLote;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JPopupMenu menuTablaLotes;
    public javax.swing.JMenuItem subMenuAbrirLote;
    public javax.swing.JMenuItem subMenuDarPorTerminado;
    public javax.swing.JMenuItem subMenuPrepararDespacho;
    public javax.swing.JMenuItem subMenuRegistrarDiferencias;
    public javax.swing.JTable tablaLotes;
    // End of variables declaration//GEN-END:variables
}
