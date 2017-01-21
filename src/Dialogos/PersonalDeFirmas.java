/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dialogos;

import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.ConexionBD;

/**
 *
 * @author AUXPLANTA
 */
public class PersonalDeFirmas extends javax.swing.JDialog {

    ConexionBD con = new ConexionBD();
    boolean CARGADO = false;
    
    public PersonalDeFirmas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        con.conectar();
        ResultSet rs = con.CONSULTAR("SELECT * FROM personal");
        try {
            while(rs.next()){
                jComboBox1.addItem(new Personal(
                        rs.getInt("idpersonal"), 
                        rs.getString("nombre"), 
                        rs.getString("cargo"), 
                        rs.getBytes("firma")));
            }
            CARGADO = true;
        } catch (SQLException ex) {
            Logger.getLogger(PersonalDeFirmas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelfirma = new CompuChiqui.JPanelImage();
        btnActualizarFirma = new javax.swing.JButton();
        btnBorrarFirma = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        panelfirma.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnActualizarFirma.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizarFirma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/upload.png"))); // NOI18N
        btnActualizarFirma.setToolTipText("Actualizar");
        btnActualizarFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarFirmaActionPerformed(evt);
            }
        });

        btnBorrarFirma.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBorrarFirma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/Borrar.png"))); // NOI18N
        btnBorrarFirma.setToolTipText("Borrar");
        btnBorrarFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarFirmaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelfirmaLayout = new javax.swing.GroupLayout(panelfirma);
        panelfirma.setLayout(panelfirmaLayout);
        panelfirmaLayout.setHorizontalGroup(
            panelfirmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfirmaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelfirmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnActualizarFirma, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrarFirma, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelfirmaLayout.setVerticalGroup(
            panelfirmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfirmaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnActualizarFirma)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrarFirma)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox1, 0, 380, Short.MAX_VALUE)
                    .addComponent(panelfirma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelfirma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if(CARGADO){
            if(evt.getStateChange() == ItemEvent.SELECTED){
                Personal p = (Personal) evt.getItem();
                if(p.getFirma() != null){
                    panelfirma.setImagen_BufferedImage(modelo.Metodos.byteToBufferedImage(p.getFirma()));
                }else{
                    JOptionPane.showMessageDialog(null, "NO TIENE FIRMA REGISTRADA");
                }
            }            
        }        
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void btnActualizarFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarFirmaActionPerformed
        try {
            Personal p = jComboBox1.getItemAt(jComboBox1.getSelectedIndex());
            int id = p.getId();
            Image imagefirma = (BufferedImage) panelfirma.getImagen();
            BufferedImage bfimagefirma = modelo.Metodos.imageToBufferedImage(imagefirma);
            byte[] bytefirma = modelo.Metodos.BufferedImageToByteArray(bfimagefirma);
            if(imagefirma != null){
                Connection conex = con.conectar();
                PreparedStatement pst = conex.prepareStatement("UPDATE personal SET firma=? WHERE idpersonal="+id+" ");
                InputStream is =  new  ByteArrayInputStream(bytefirma);
                pst.setBinaryStream(1, is, bytefirma.length);
                if(pst.executeUpdate()>0){
                    modelo.Metodos.M("LA FIRMA HA SIDO ACTUALIZADA.", "bien.png");
                }
            }else{
                modelo.Metodos.M("NO SE HA INSERTADO UNA IMAGEN.", "error.png");
            }            

        } catch (IOException | SQLException e) {
        }
    }//GEN-LAST:event_btnActualizarFirmaActionPerformed

    private void btnBorrarFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFirmaActionPerformed
        if(con.GUARDAR("UPDATE personal SET firma=null WHERE idpersonal="+jComboBox1.getItemAt(jComboBox1.getSelectedIndex()).getId()+" ")){
            modelo.Metodos.M("SE HA ELIMINADO LA FIRMA A LA PERSONA SELECCIONADA.", "bien.png");
            panelfirma.setImagen_BufferedImage(null);
        }
    }//GEN-LAST:event_btnBorrarFirmaActionPerformed

    class Personal{

        private int id;
        private String nombre;
        private String cargo;
        private byte[] firma;
        
        public Personal(int id, String nombre, String cargo, byte[] firma) {
            this.id = id;
            this.nombre = nombre;
            this.cargo = cargo;
            this.firma = firma;
        }        

        @Override
        public String toString() {
            return nombre + " - " + cargo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCargo() {
            return cargo;
        }

        public void setCargo(String cargo) {
            this.cargo = cargo;
        }

        public byte[] getFirma() {
            return firma;
        }

        public void setFirma(byte[] firma) {
            this.firma = firma;
        }
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PersonalDeFirmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PersonalDeFirmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PersonalDeFirmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PersonalDeFirmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PersonalDeFirmas dialog = new PersonalDeFirmas(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarFirma;
    private javax.swing.JButton btnBorrarFirma;
    private javax.swing.JComboBox<Personal> jComboBox1;
    private CompuChiqui.JPanelImage panelfirma;
    // End of variables declaration//GEN-END:variables
}
