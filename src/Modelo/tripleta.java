
package Modelo;


public class tripleta {
    
    private int fila;
    private int columna;
    private Object valor;
    
    public tripleta(int fila,int columna,Object valor)
    {
        this.fila=fila;
        this.columna=columna;
        this.valor=valor;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Object getValor() {
        return valor;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
    
    
    
    
}
