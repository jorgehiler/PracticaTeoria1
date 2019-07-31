
package Modelo;


public class Dnode {
    private Dnode li;
 private Dnode ld;
 //private Object dato;
 private tripleta dato;

    public Dnode(Object dato) {
        this.li =null;
        this.ld = null;
        this.dato = (tripleta)dato;
    }

    public Dnode getLi() {
        return li;
    }

    public Dnode getLd() {
        return ld;
    }

    public Object getDato() {
        return dato;
    }

    public void setLi(Dnode li) {
        this.li = li;
    }

    public void setLd(Dnode ld) {
        this.ld = ld;
    }

    public void setDato(Object dato) {
        this.dato =(tripleta)dato;
    }
 
    
    
}
