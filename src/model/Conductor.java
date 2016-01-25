package model;

/**
 *
 * @author AUXPLANTA
 */
public class Conductor {

    private int idConductor;
    private String cedulaConductor;
    private String nombreConductor;
    
    public Conductor(int idConductor, String cedulaConductor, String nombreConductor) {
        this.idConductor = idConductor;
        this.cedulaConductor = cedulaConductor;
        this.nombreConductor = nombreConductor;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public String getCedulaConductor() {
        return cedulaConductor;
    }

    public void setCedulaConductor(String cedulaConductor) {
        this.cedulaConductor = cedulaConductor;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }
    
    @Override
    public String toString(){
        return nombreConductor;
    }
    
}
