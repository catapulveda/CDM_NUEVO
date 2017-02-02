package modelo;

//import static Dialogos.Backup.barra;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.Principal;

public class Backup extends Dialogos.Backup{    
                
    public Backup(java.awt.Frame owner, boolean modal){
        super(owner, modal);
        restaurar();
    }
    
    public void restaurar(){        
        (new Thread(){
            public void run(){
                Process p = null;
                try{
                    ConexionBD conex = new ConexionBD();
                    conex.conectar();
                    ProcessBuilder pb;                                  
                    pb = new ProcessBuilder("C:\\Program Files\\PostgreSQL\\9.5\\bin\\pg_dump.exe", "--host", InetAddress.getByName(conex.getIP()).getHostAddress() , "--port", conex.getPUERTO(), "--username", "postgres", "--role", "postgres", "--no-password", "--format", "custom", "--blobs", "--verbose", "--file", System.getProperty("user.dir")+"//CDM.backup", conex.getBD());
                    pb.environment().put("PGPASSWORD", conex.getPASS());  
                    pb.redirectErrorStream(true);  
                    final Process process = pb.start();        

                    BufferedInputStream bufferIs = new BufferedInputStream(process.getInputStream());
                    InputStreamReader isReader = new InputStreamReader( bufferIs );
                    BufferedReader reader = new BufferedReader(isReader);
                    String line = "";
                    System.out.println(line);
                    barra.setIndeterminate(true);
                    barra.setStringPainted(true);
                    while( (line = reader.readLine())!=null ){
                        System.out.println(line);
                        barra.setString(line);
                    }    
        //            JOptionPane.showMessageDialog(null,"Restore realizado com sucesso.");                         
                } catch (Exception e) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, e);
                    modelo.Metodos.M("NO SE PUDO CREAR LA COPIA DE SEGURIDAD.\n"+e, "error.png");
                }finally{
                    dispose();
                }
            }            
        }).start();
        
    }

    
    
}
