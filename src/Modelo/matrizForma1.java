package Modelo;

//import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPDecoderStream;
import java.util.ArrayList;

public class matrizForma1 {

    private Dnode mat;
    private int acumuladoCabezas;

    /**
     * Contructor de la clase matrizForma1
     *
     * @param m representa el numero de filas de la matriz
     * @param n representa el numero de columnas de la matriz
     */
    public matrizForma1(int m, int n) {
        tripleta t = new tripleta(m, n, null);
        mat = new Dnode(t);
        t.setValor(mat);
        mat.setDato(t);
        acumuladoCabezas = 0;
    }
    
    /**
     * Metodo que retorna nodo cabeza de la matriz Dispersa
     * @return nodo mat
     */

    public Dnode getMat() {
        return mat;
    }

    public void setMat(Dnode mat) {
        this.mat = mat;
    }

    /**
     * Metodo que retorna el nodo que contiene las dimensiones de la matriz
     *
     * @return mat,nodo cabeza que contiene las dimesiones de la matriz
     */
    public Dnode nodoCabeza() {
        return mat;
    }

    /**
     * Metodo que retorna el primer nodo cabeza de la matriz
     *
     * @return direccion del primer nodo cabeza
     */
    public Dnode primerNodo() {
        tripleta tp = (tripleta) mat.getDato();
        Dnode p = (Dnode) tp.getValor();
        return p;

    }

    /**
     * Metodo que valida si la matriz esta vacia
     *
     * @return retorna verdadero si es vacia,falso si es lo contrario;
     */
    public boolean esVacia() {
        Dnode p = (Dnode) primerNodo();
        return p == mat;

    }

    /**
     * Metodo que valida si es el fin de recorrido en la matriz
     *
     * @param p apuntador que se va moviendo a traves de la matriz
     * @return verdadero si es el fin del recorrido,falso de lo contrario
     */
    public boolean findeRecorrido(Dnode p) {
        return p == mat;
    }

    /**
     * Metodo que muestra la matriz
     */
    public void muestraMatriz() {

        int qf, qc, qv;
        Dnode p, q;
        tripleta tq, tp;
        p = primerNodo();
        System.out.println("//////////----/////");

        while (!(findeRecorrido(p))) {
            q = p.getLd();

            while (q != p) {
                tq = (tripleta) q.getDato();
                qf = tq.getFila();
                qc = tq.getColumna();
                //qv = (int) tq.getValor();
                ArrayList k = (ArrayList) tq.getValor();

                for (int i = 0; i < k.size(); i++) {
                    System.out.println(qf + "-" + qc + "-" + k.get(i));
                }

                q = q.getLd();

            }

            tp = (tripleta) p.getDato();
            p = (Dnode) tp.getValor();
        }

    }

    /**
     * Metodo que recupera el ultimo nodocabeza de la matriz
     *
     * @return ultimoNodo de que representa ultima fila ultima columma de la
     * matriz
     */
    public Dnode ultimoNodo() {

        Dnode p = primerNodo();
        Dnode pp = mat;

        while (!findeRecorrido(p)) {
            pp = p;
            //p = (Dnode) p.getDato();
            tripleta tx = (tripleta) p.getDato();
            p = (Dnode) tx.getValor();
        }

        return pp;

    }

    /**
     * Metodo que construye nodos cabeza de la matriz Forma 1 (Nueva)
     */
    public void construyeNodosCabezas() {

        int mayor, i, n, m;
        Dnode x, ultimo;
        tripleta t;

        ultimo = nodoCabeza();
        t = (tripleta) ultimo.getDato();

        m = t.getFila();
        n = t.getColumna();
        mayor = m;

        if (n > m) {
            mayor = n;
        }

        for (int k = 1; k <= mayor; k++) {

            t = new tripleta(k, k, nodoCabeza());
            x = new Dnode(t);
            x.setLd(x);
            x.setLd(x);
            t = (tripleta) ultimo.getDato();
            t.setValor(x);
            ultimo.setDato(t);
            ultimo = x;

        }

    }

    /**
     * Metodo que conecta los nodos por filas
     *
     * @param x nodo a conectar
     */
    public void conectaPorFilas(Dnode x) {

        Dnode p, q, anterior;
        tripleta tp, tq, tx;
        int i;
        tx = (tripleta) x.getDato();
        p = primerNodo();
        for (int k = 0; k < tx.getFila(); k++) {
            tp = (tripleta) p.getDato();
            p = (Dnode) tp.getValor();
        }
        anterior = p;
        q = p.getLd();
        tq = (tripleta) q.getDato();

        while (q != p && tq.getColumna() < tx.getColumna()) {
            anterior = q;
            q = q.getLd();
            tq = (tripleta) q.getDato();
        }
        anterior.setLd(x);
        x.setLd(q);

    }

    /**
     * Metodo que conecta un nodo por columna
     */
    public void conectaPorColumnas(Dnode x) {
        Dnode p, q, anterior;
        tripleta tp, tq, tx;
        tx = (tripleta) x.getDato();
        p = primerNodo();
        for (int i = 0; i < tx.getColumna(); i++) {
            tp = (tripleta) p.getDato();
            p = (Dnode) tp.getValor();

        }

        anterior = p;
        q = p.getLi();
        tq = (tripleta) q.getDato();
        while (q != p && tq.getFila() < tx.getFila()) {
            anterior = q;
            q = q.getLi();
            tq = (tripleta) q.getDato();
        }

        anterior.setLi(x);
        x.setLi(q);

    }

    /**
     * Metodo que agrega nuevo nodo cabeza en la matriz
     */
    public void agregarNodoCabeza(int indice) {

        Dnode p = ultimoNodo();
        tripleta t = new tripleta(indice, indice, mat);
        Dnode x = new Dnode(t);
        x.setLi(x);
        x.setLd(x);
        tripleta tp = (tripleta) p.getDato();
        tp.setValor(x);
        p.setDato(tp);

    }

    /**
     * Metodo que agregar las transiciones en la matriz
     *
     * @param simoboloEntrada,representa la columna de la matriz
     * @param estaInicial,representa la fila de la matriz
     * @param estadoDestino ,el dato insertar en la matriz
     */
    public void agregarTransicion(int simoboloEntrada, int estaInicial, int estadoDestino) {

        Dnode p = primerNodo();
        Dnode q;
        tripleta tx = (tripleta) p.getDato();

        while (!findeRecorrido(p) && tx.getFila() != estaInicial) {
            p = (Dnode) tx.getValor();
            tx = (tripleta) p.getDato();
        }

        q = p.getLd();
        tripleta txs = (tripleta) q.getDato();

        while (p != q && txs.getColumna() != simoboloEntrada) {
            q = q.getLd();
            txs = (tripleta) q.getDato();
        }

        if (p == q) {
            ArrayList transicion = new ArrayList();
            transicion.add(estadoDestino);
            tripleta tl = new tripleta(estaInicial, simoboloEntrada, transicion);
            Dnode x = new Dnode(tl);
            conectaPorFilas(x);
            conectaPorColumnas(x);
        } else {
            tripleta txl = (tripleta) q.getDato();
            ArrayList t = (ArrayList) txl.getValor();
            t.add(estadoDestino);
            txs.setValor(t);
            q.setDato(txs);
        }

    }

    /**
     * Metodo que recupera la Transicion de la matriz
     *
     * @param fila coordenada que indica la fila en la matriz (estado)
     * @param columna coordena que indica la columna en la matriz (simbolo de
     * entrada)
     * @return
     */
   public ArrayList recuperarTransicion(int fila, int columna) {
        Dnode p, q;
        tripleta dato, ok;
        ArrayList respuesta;

        p = primerNodo();
        dato = (tripleta) p.getDato();

        while (!findeRecorrido(p) && dato.getFila() != fila) {
            p = (Dnode) dato.getValor();
            dato = (tripleta) p.getDato();
        }

        q = p.getLd();

        tripleta datos = (tripleta) q.getDato();

        while ((p != q) && (datos.getColumna() != columna)) {
            q = q.getLd();
            datos = (tripleta) q.getDato();
        }

        ok = (tripleta) q.getDato();
        respuesta = (ArrayList) ok.getValor();

        return respuesta;

    }

    public Dnode recuperNodo(int i, int j) {
        Dnode p = primerNodo();
        Dnode q;
        int l, k;

        while (!findeRecorrido(p)) {
            q = p.getLd();

            while (p != q) {
                tripleta t = (tripleta) q.getDato();
                l = t.getFila();
                k = t.getColumna();
                if (i == l && j == k) {
                    return q;
                }

                q = q.getLd();

            }

            tripleta txs = (tripleta) p.getDato();
            p = (Dnode) txs.getValor();
        }
        return null;
    }

    public Dnode recuperarNodoAnterior(int i, int j) {
        Dnode p = primerNodo();
        Dnode pq, q;
        int l, c;
        pq = null;

        while (!findeRecorrido(p)) {
            pq = p;
            q = p.getLd();

            while (p != q) {
                tripleta t = (tripleta) q.getDato();
                l = t.getFila();
                c = t.getColumna();

                if (i == l && j == c) {
                    return pq;
                }

                pq = q;
                q = q.getLd();

            }

            tripleta txs = (tripleta) p.getDato();
            p = (Dnode) txs.getValor();

        }

        return pq;

    }

    public void eliminarTransicion(int fila, int columna, int valor) {
        //ArrayList transiciones = recuperarTransicion(fila, columna);

        Dnode transicion = this.recuperNodo(fila, columna);
        tripleta tx = (tripleta) transicion.getDato();
        ArrayList tr = (ArrayList) tx.getValor();
        int num = tr.size();
        if (num == 1) {
         
            desconectaFila(transicion);
            desconectarColumna(transicion);
        } else {
            tr.remove(valor);
            tx.setValor(tr);
            transicion.setDato(tx);
        }

    }

    public void desconectaFila(Dnode x) {
        Dnode p, antf;
        p = x.getLd();
        antf = p;

        while (p != x) {
            antf = p;
            p = p.getLd();
        }

        antf.setLd(x.getLd());

    }

    public void desconectarColumna(Dnode x) {
        Dnode p, antc;
        p = x.getLi();
        antc = p;
        while (p != x) {
            antc = p;
            p = p.getLi();
        }

        antc.setLi(x.getLi());

    }
    
    public Dnode recuperaNodoCabeza(int i)
    {
        
        Dnode p=primerNodo();
        
        while(!findeRecorrido(p))
        {
            tripleta txs=(tripleta)p.getDato();
            
            if(txs.getFila()==i)
            {
                return p;
            }
           
            tripleta txl=(tripleta)p.getDato();
            p=(Dnode)txl.getValor();
            
        }
        
        return null;
        
    }
    
    
    

}
