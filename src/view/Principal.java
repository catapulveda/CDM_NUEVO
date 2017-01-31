package view;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import modelo.ConexionBD;
import modelo.Metodos;

/**
 *
 * @author AUXPLANTA
 */
public class Principal extends javax.swing.JFrame {

    private PopupMenu popup = new PopupMenu();
    private Image image =new ImageIcon(getClass().getResource("/recursos/images/logo.png")).getImage() ;
    private final TrayIcon trayIcon = new TrayIcon(image, "CDM SOFTWARE", popup);
    
    static final ConexionBD conexion = new ConexionBD();
    
    public Principal(){
        initComponents();
                
            //REGISTRA LAS ENTRADAS O LOTES
//        ResultSet rs1 = modelo.Conexion2.CONSULTAR("SELECT * FROM entrada e, conductor c WHERE e.conductor=c.nombre_conductor ORDER BY e.identrada");
//        try {
//            String sql = " INSERT INTO entrada (identrada,idcliente,idciudad,idconductor, \n";
//            sql += " idusuario,identradaalmacen,nombrepc,lote,contrato,op,centrodecostos, \n";
//            sql += " fecharecepcion,fecharegistrado,fechaactualizado,estado,observacion,placavehiculo) VALUES \n ";
//            while(rs1.next()){
//                sql += " ('"+rs1.getString("identrada")+"' , '"+rs1.getString("idcliente")+"', ";
//                sql += " '"+rs1.getString("idciudad_entrada")+"' , '"+rs1.getString("id_conductor")+"' , ";
//                sql += " '1' , '"+rs1.getString("identradaalmacen")+"' , 'ALMACEN' , '"+rs1.getString("lote")+"' , ";
//                sql += " '"+rs1.getString("contratono")+"' , '"+rs1.getString("op")+"' , ";
//                sql += " '"+rs1.getString("centrodecosto")+"' , '"+rs1.getDate("fechaderecepcion")+"' , ";
//                sql += " '"+rs1.getDate("fechaderecepcion")+"' , '"+rs1.getDate("fechaderecepcion")+"' , ";
//                sql += " '"+rs1.getString("estadolote")+"' , '"+rs1.getString("observacion_entradaalmacen")+"' , ";
//                sql += " '"+rs1.getString("placa")+"' ),\n";
//            }
//            sql = sql.substring(0, sql.length()-2);
//            Clipboard system;
//            StringSelection stsel;
//            stsel  = new StringSelection(sql.toString());
//            system = Toolkit.getDefaultToolkit().getSystemClipboard();
//            system.setContents(stsel,stsel);
//            System.out.println(sql);
//            if(new modelo.ConexionBD().GUARDAR(sql)){
//                
//            }
//        }catch(SQLException ex){
//            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//        }

//REGISTRA LOS TRANSFORMADORES
//    (new Thread(){
//            public void run(){
//                ResultSet rs = modelo.Conexion2.CONSULTAR("SELECT * FROM datosentrada");
//                try {
//                    String GUARDAR = " INSERT INTO transformador ( ";
//                    GUARDAR += " idtransformador, item, numeroempresa, numeroserie, marca, kvaentrada, kvasalida, fase, tpe, tse, ";
//                    GUARDAR += " tte, tps, tss, tts, aat, abt, hat, hbt, ci, ce, herraje, ano, ";
//                    GUARDAR += " peso, aceite, observacionentrada, observacionsalida, servicioentrada, serviciosalida, ";
//                    GUARDAR += " tipotrafoentrada, tipotrafosalida, estado, identrada, iddespacho, idremision ";
//                    GUARDAR += " ) VALUES ";
//                    while(rs.next()){
//                        //System.out.println(rs.getRow()+" "+rs.getString("tension_salida")+" "+rs.getString("noplaca"));
//                        area.setText(rs.getRow()+" "+rs.getString("tension_salida")+" "+rs.getString("noplaca"));
//                        GUARDAR += " ( "+rs.getInt("idtrafo")+" , ";
//                        GUARDAR += " '"+rs.getString("no")+"' , '"+rs.getString("noempresa")+"' , '"+rs.getString("noplaca")+"' , ";
//                        GUARDAR += " '"+rs.getString("marca")+"' , '"+rs.getString("kva")+"' , '"+rs.getString("kva_salida")+"' , ";
//                        GUARDAR += " '"+rs.getString("fase")+"' , '"+rs.getString("tension").split("/")[0]+"' , '"+rs.getString("tension").split("/")[1]+"' , ";
//                        GUARDAR += " '"+rs.getString("tension").split("/")[2]+"' , '"+rs.getString("tension_salida").split("/")[0]+"' ,  ";
//                        GUARDAR += " '"+rs.getString("tension_salida").split("/")[1]+"' , '"+rs.getString("tension_salida").split("/")[2]+"' , ";
//                        GUARDAR += " '"+rs.getString("aat")+"' , '"+rs.getString("abt")+"' , '"+rs.getString("hat")+"' , '"+rs.getString("hbt")+"' ,";
//                        GUARDAR += " '"+rs.getBoolean("ci")+"' , '"+rs.getBoolean("ce")+"' , '"+rs.getString("herraje")+"' , ";
//                        GUARDAR += " '"+rs.getString("ano")+"' , '"+rs.getString("peso")+"' , '"+rs.getString("aceite")+"' , ";
//                        GUARDAR += " '"+rs.getString("observacion")+"' , '"+rs.getString("observacion_salida")+"' , '"+rs.getString("servicio")+"' , ";
//                        GUARDAR += " '"+rs.getString("servicio_salida")+"' , '"+rs.getString("tipoentrada")+"' , '"+rs.getString("tipotrafo_salida")+"' , ";
//                        GUARDAR += " '"+rs.getString("estado")+"' , '"+rs.getString("identrada_datos")+"' , '"+rs.getString("iddespacho_salida")+"' , ";
//                        GUARDAR += ((rs.getString("idremision_datosentrada")==null)?0:rs.getString("idremision_datosentrada"));
//                        GUARDAR += "),\n";
//                    }
//        //            System.out.println(GUARDAR);
//                    GUARDAR = GUARDAR.substring(0, GUARDAR.length()-2);
//        //            System.out.println(GUARDAR);
//                    Clipboard system;
//                    StringSelection stsel;
//                    stsel  = new StringSelection(GUARDAR);
//                    system = Toolkit.getDefaultToolkit().getSystemClipboard();
//                    system.setContents(stsel,stsel);
//                    if(new modelo.ConexionBD().GUARDAR(GUARDAR)){
//
//                    }
//                } catch (SQLException ex) {
//                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//                    area.setText(""+ex);
//               }
//            }
//        }).start();
       
//REGISTRA LAS DIFERENCIAS DE LAS ENTRADAS REGISTRADAS
//        ResultSet rs = model.Conexion2.CONSULTAR("SELECT * FROM diferencia_entrada");
//        String GUARDAR = " INSERT INTO diferenciasentrada (iddiferencia, identrada, descripcion) VALUES ";        
//        try {
//            while(rs.next()){
//                GUARDAR += " ( '"+rs.getString("id_dif")+"' , '"+rs.getString("identrada_dif")+"' , '"+rs.getString("diferencia_dif")+"' ),\n";
//            }
//            GUARDAR = GUARDAR.substring(0, GUARDAR.length()-2);
//            if(new model.ConexionBD().GUARDAR(GUARDAR)){
//
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//        }


//REGISTRA LOS DESPACHOS
//        ResultSet rs = modelo.Conexion2.CONSULTAR("SELECT * FROM despacho");
//        String GUARDAR = " INSERT INTO despacho VALUES ";
//        try {
//            while(rs.next()){
//                GUARDAR += " ( '"+rs.getString("iddespacho")+"' , '"+rs.getString("nodespacho")+"' , '"+rs.getString("fecha_despacho")+"' , '"+rs.getString("idcliente")+"' , '"+((rs.getString("peso_despacho")==null)?0:rs.getInt("peso_despacho"))+"' , '"+rs.getString("estado_despacho")+"' , '"+rs.getString("descripcion_despacho")+"' , 1) ,\n";
//            }
//            System.out.println(GUARDAR);
//            GUARDAR = GUARDAR.substring(0, GUARDAR.length()-2);
//            if(new modelo.ConexionBD().GUARDAR(GUARDAR)){
//                
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//REGISTRA LAS REMISIONES
//        ResultSet rs = modelo.Conexion2.CONSULTAR("SELECT * FROM remision");
//        String GUARDAR = " INSERT INTO remision VALUES ";
//        try {
//            while(rs.next()){
//                GUARDAR += " ( '"+rs.getString("idremision")+"' , '"+rs.getString("iddespacho_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("numero_remision")+"' , '"+rs.getString("cliente_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("ciudad_remision")+"' , '"+rs.getString("destino_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("telefono_remision")+"' , '"+rs.getString("contrato_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("centrodecostos_remision")+"' , '"+rs.getString("conductor_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("cedula_remision")+"' , '"+rs.getString("placa_remision")+"' , ";
//                GUARDAR += " '"+rs.getDate("fecha_remision")+"' , '"+rs.getString("tipo_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("cedula_remision")+"' , '"+rs.getString("placa_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("servicio_remision")+"' , '"+rs.getString("descripcion_remision")+"' , ";
//                GUARDAR += " '"+rs.getString("factura_numero")+"' , '"+rs.getString("empresa_remision")+"' , ";
//                GUARDAR += " '"+rs.getBoolean("estado")+"', 1 ) ,\n";
//                
//            }
//            System.out.println(GUARDAR);
//            Clipboard system;
//            StringSelection stsel;
//            stsel  = new StringSelection(GUARDAR);
//            system = Toolkit.getDefaultToolkit().getSystemClipboard();
//            system.setContents(stsel,stsel);
//            GUARDAR = GUARDAR.substring(0, GUARDAR.length()-2);
//            if(new modelo.ConexionBD().GUARDAR(GUARDAR)){
//                
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            ServerSocket serverSocket = new ServerSocket(55557);
//        } catch (IOException ex) {
//            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//            Metodos.ERROR(ex, "LA APLICACION YA SE HA INICIADO, VERIFIQUE LOS PROCESOS EJECUTADOS E INTENTE NUEVAMENTE");
//            System.exit(0);
//        }
        
        jPanel1.setLayout (new JScollPanelAjustable.WrapLayout());
        
        btnLotes.addActionListener((ActionEvent e)->{
            setExtendedState(ICONIFIED);
            Dialogos.Login login = new Dialogos.Login(this, rootPaneCheckingEnabled);
            login.setVisible(true);
        });
        
//        if (SystemTray.isSupported()){
//            try {
//                SystemTray systemtray = SystemTray.getSystemTray();
//                                
//                trayIcon.setToolTip("CDM SOFTWARE");
//                trayIcon.setImageAutoSize(true);
//                trayIcon.addMouseListener(new MouseAdapter(){
//                    @Override
//                    public void mouseReleased(MouseEvent e){
//                        if(SwingUtilities.isLeftMouseButton(e)){
//                            setVisible(true);
//                            setExtendedState(JFrame.NORMAL);
//                        }
//                    }
//                });
//                systemtray.add(trayIcon);
//            } catch (Exception ex) {
//                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowIconified(WindowEvent e) {
//                (new Thread(){
//                    @Override
//                    public void run(){
//                        setVisible(false);
//                        while(true){
//                            if(getExtendedState()==JFrame.ICONIFIED){
//                                trayIcon.displayMessage("CDM SOFTWARE:", "Aplicacion iniciada en segundo plano.", TrayIcon.MessageType.INFO);
//                                try {
//                                    Thread.sleep(60000);
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//                        }
//                    }
//                }).start();                
//            }
//        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btnLotes = new javax.swing.JButton();
        btnProduccion = new javax.swing.JButton();
        btnProtocolo = new javax.swing.JButton();
        jPanelImage1 = new CompuChiqui.JPanelImage();
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new CompuChiqui.JCTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu principal");
        setIconImage(new ImageIcon(getClass().getResource("/recursos/images/logo.png")).getImage());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnLotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/lotes.png"))); // NOI18N
        btnLotes.setPreferredSize(new java.awt.Dimension(130, 130));

        btnProduccion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/calendario.png"))); // NOI18N
        btnProduccion.setPreferredSize(new java.awt.Dimension(130, 130));

        btnProtocolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/images/protocolo.png"))); // NOI18N
        btnProtocolo.setPreferredSize(new java.awt.Dimension(130, 130));
        btnProtocolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProtocoloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLotes, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnProtocolo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnProduccion, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLotes, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProtocolo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnProduccion, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        jPanelImage1.setImagen_ImageIcon(new ImageIcon(getClass().getResource("/recursos/images/logo.png")));

        javax.swing.GroupLayout jPanelImage1Layout = new javax.swing.GroupLayout(jPanelImage1);
        jPanelImage1.setLayout(jPanelImage1Layout);
        jPanelImage1Layout.setHorizontalGroup(
            jPanelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );
        jPanelImage1Layout.setVerticalGroup(
            jPanelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 128, Short.MAX_VALUE)
        );

        area.setColumns(20);
        area.setRows(5);
        jScrollPane1.setViewportView(area);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 516, Short.MAX_VALUE)
                        .addComponent(jPanelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnProtocoloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProtocoloActionPerformed
        view.PROTOCOLO proto = new view.PROTOCOLO();
        proto.setVisible(true);
        hide();
    }//GEN-LAST:event_btnProtocoloActionPerformed
    
    public static void main(String args[]){     
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private CompuChiqui.JCTextArea area;
    private javax.swing.JButton btnLotes;
    private javax.swing.JButton btnProduccion;
    private javax.swing.JButton btnProtocolo;
    private javax.swing.JPanel jPanel1;
    private CompuChiqui.JPanelImage jPanelImage1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
