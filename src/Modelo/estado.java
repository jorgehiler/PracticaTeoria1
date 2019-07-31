
package Modelo;


public class estado {
    
    private String nombreEstado;
    //private int tipoEstado;
    private String tipoEstado;
    private boolean  estadoIncial;

    public estado(String nombreEstado, String tipoEstado,boolean  estadoInicial) {
        this.nombreEstado = nombreEstado;
        //this.tipoEstado = tipoEstado;
        this.tipoEstado=tipoEstado;
        this.estadoIncial=estadoInicial;
    }
    
    public estado()
    {
    
    }

    public boolean isEstadoIncial() {
        return estadoIncial;
    }
    
    

    public String getNombreEstado() {
        return nombreEstado;
    }

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }
    
    
    
    
}
