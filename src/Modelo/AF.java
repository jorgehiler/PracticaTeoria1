package Modelo;

import Vista.FormularioPrincipal;
import controlador.controlador;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import static java.util.Collections.list;

import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sun.font.TrueTypeFont;
import java.util.HashSet;
import java.util.Set;
import jdk.nashorn.internal.parser.TokenType;

public class AF {

    private ArrayList estados;
    private matrizForma1 mat;
    private JFileChooser fc;
    private File fichero;
    private String cadena;
    private final ArrayList transicionEstadoAFD = new ArrayList();
    private ArrayList simbolosEntrada;
    private ArrayList particiones;
    private ArrayList nuevaParticion;
    private int flag;
    private int numeroParticiones;

    public AF() {
        this.simbolosEntrada = new ArrayList();
        this.estados = new ArrayList();
        this.fc = new JFileChooser();
        this.fichero = null;
        this.particiones = new ArrayList();
        this.nuevaParticion = new ArrayList();
        this.flag = 0;
        this.numeroParticiones = 0;
    }

    public void construirAutomata() {
        mat = new matrizForma1(0, 0);
    }

    /**
     *
     * @param automata Autómata al que pertenecen los estados a fusionar
     * @param estadosFusionar nodo que contienen los estados a fusionar
     * @return objeto que contienene el objeto estado resultante y un ArrayList
     * con las posiciones de los estados fusionados "indexTransiciones"
     */
    public estadoFusionado fusionarEstados(AF automata, Dnode estadosFusionar) {
        ArrayList listIndicesEstado = ut.getArrayDataDnode(estadosFusionar);
        ArrayList indexTransiciones = new ArrayList();
        int n = listIndicesEstado.size();
        estado e = new estado();
        if (n == 1) {
            int i = (int) listIndicesEstado.get(0);
            estado auxE = (estado) this.estados.get(i); //obtener nombre de estado AF actual Dnode
            e.setNombreEstado(auxE.getNombreEstado());
            e.setTipoEstado(auxE.getTipoEstado());
            indexTransiciones.add(i);
        } else { // Hace transición a más de un estado
            String nuevoNombre = "";
            for (int i = 0; i <= n - 1; i++) {                 //verificar que no exista la fusión 
                int j = (int) listIndicesEstado.get(i); //indice de la transicion al estado n-simo i
                estado et = (estado) this.estados.get(j);
                nuevoNombre = nuevoNombre + (String) et.getNombreEstado();
                indexTransiciones.add(j); //Transiciones al nuevo estado
            }
            e.setNombreEstado(nuevoNombre);
            e.setTipoEstado(this.obtenerTipoEstado(listIndicesEstado));
        }
        estadoFusionado ef = new estadoFusionado(e, indexTransiciones);
        return ef;
    }

    /**
     * Metodo que construye ArrayLisy con las transasiones por cada simbolo de
     * entrada para un estado fusionado o no. Cada posición i del ArrayList
     * representa la transicion que hace el estado fusionado k+1 debido a la
     * entrada del simbolo i. El metodo almacena cada ArrayList de transiciones
     * en el ArrayList "transicionEstadoAFD" en el que la posicion cero
     * representa al estado 1 en el automata.
     *
     * @param ef Contiene un ArrayList en el que cada elemento tiene un indice
     * de la posición de los estados fusionados en el AFND
     */
    void construirTransicion(estadoFusionado ef) {
        ArrayList transPorSimbolo = new ArrayList();
        ArrayList estadosDestino;
        ArrayList estadosOrigen = ef.transiciones;
        int cantidadSimbolos = this.simbolosEntrada.size();
        int j = 0;
        while (j <= cantidadSimbolos - 1) {
            estadosDestino = new ArrayList();
            for (int i = 0; i <= estadosOrigen.size() - 1; i++) {
                int origen = (int) estadosOrigen.get(i);
                Dnode estadoDestino = this.mat.recuperNodo(origen, j);
                ArrayList aux = ut.getArrayDataDnode(estadoDestino);
                for (int k = 0; k <= aux.size() - 1; k++) {
                    estadosDestino.add(aux.get(k));
                }
            }
            this.eliminarRepeticionesArrayList(estadosDestino);
            transPorSimbolo.add(estadosDestino);
            j++;
        }
        this.transicionEstadoAFD.add(transPorSimbolo);
    }

    /**
     * Metodo que crea e inserta de ser necesario, los estados a los que hace
     * transicion el estadoFusionado indicaod
     *
     * @param ef
     * @param AFD Automata deterministico al que pertenece el estado fusionado.
     * @return
     */
    public Boolean crearInsertarEstados(estadoFusionado ef, AF AFD) {
        String nombreEst = ef.e.getNombreEstado();
        if (!ef.existeEstado(AFD.estados, nombreEst)) { //Si el destino del estado fusionado no existe insertarlo                   
            estado ne = new estado(ef.e.getNombreEstado(), ef.e.getTipoEstado(), ef.e.isEstadoIncial()); //Si entra uno de aceptacion o inicial actualizar
            AFD.insertarEstado(ne);
            this.construirTransicion(ef); //Define las transiciones del estado destino
            return true;
        }
        return false;
    }

    /**
     * Convierte un AFND a AFD
     *
     */
    public AF transformarAFNDaAFD() {
        Dnode n = this.mat.primerNodo().getLd();
        AF AFD = new AF();
        AFD.construirAutomata();
        AFD.mat.agregarNodoCabeza(0);
        estado ei = new estado(ut.getNombreEstado(estados, 0), ut.getTipoEstado(estados, 0), true); //Estado inicial nuevo
        AFD.estados.add(ei); //Agregar estado inicial
        AFD.setSimbolosEntrada(this.simbolosEntrada);
        Dnode primerNodo = this.mat.primerNodo();

        //Aumentar en una unidad la fila y agregar cantidad de columnas
        tripleta aux1 = (tripleta) AFD.mat.getMat().getDato();
        tripleta aux2 = (tripleta) this.mat.getMat().getDato();
        aux1.setFila(1);
        aux1.setColumna(aux2.getColumna());

        while (primerNodo != n) {
            estadoFusionado efn = this.fusionarEstados(AFD, n);
            String nombreEst = efn.e.getNombreEstado();
            String nombreTransicion = "";
            tripleta axi = (tripleta) n.getDato();
            int idAct = axi.getFila(); //Estado actual
            int idS = axi.getColumna();//Simbolo de entrada actual
            estado std = (estado) this.estados.get(idAct);
            String estadoAct = (String) std.getNombreEstado();
            String simboloE = (String) this.simbolosEntrada.get(idS);
            if (this.crearInsertarEstados(efn, AFD)) { //Si el destino del fusionado existe insertarlo                   
                nombreTransicion = nombreEst;
            }
            AFD.cargarTransicion(simboloE, estadoAct, nombreTransicion);
            n = n.getLd();
        }

        tripleta auxt = (tripleta) AFD.mat.primerNodo().getDato();
        Dnode x = (Dnode) auxt.getValor();
        int k = 0;
        ArrayList<estado> estadosAFD = AFD.estados;
        while (!AFD.mat.findeRecorrido(x)) { //Recorre los estados para agregar sus transiciones
            int i = 0;
            estado estAux = estadosAFD.get(k + 1);
            ArrayList indicesEstados = (ArrayList) this.transicionEstadoAFD.get(k); //Los indices del estado al que hace transición por entrada de cada simbolo
            String nombreTransicion = "";
            while (i <= this.simbolosEntrada.size() - 1) { //Po cada simbolo se inserta un estado
                ArrayList a = (ArrayList) indicesEstados.get(i);
                String nombreEst = this.decodificarNombreEstado(this.estados, a); //Decodifica el nombre del estado al que se hace tansicion correpondiente al simbolo de la posicion i
                String tipoE = this.obtenerTipoEstado(a);
                estado e = new estado(nombreEst, tipoE, false);
                estadoFusionado efn = new estadoFusionado(e, (ArrayList) indicesEstados.get(i)); //Estado al que hace transicion
                this.crearInsertarEstados(efn, AFD);  //Si el destino del fusionado existe insertarlo, sino crearlo e insertarlo                                  
                nombreTransicion = nombreEst;
                String simboloEntrada = (String) AFD.simbolosEntrada.get(i);
                AFD.cargarTransicion(simboloEntrada, estAux.getNombreEstado(), nombreTransicion);
                i++;
            }
            tripleta axt = (tripleta) x.getDato();
            x = (Dnode) axt.getValor();
            k++;
        }

        return AFD;
    }

    private void eliminarRepeticionesArrayList(ArrayList a) {
        Set<String> set;
        set = new HashSet<>(a);
        a.clear();
        a.addAll(set);
    }

    /**
     * Metodo que retorna un String que indica si el estado es de "Rechazo" o
     * "Aceptacion"
     *
     * @param indicesEstado ArrayList que contiene el indice o indices en el
     * AFND del estado procesado o fusionado a evaluar.
     * @return retorna un String con "Aceptacion" o "Rechazo"
     */
    public String obtenerTipoEstado(ArrayList indicesEstado) {
        String tipoE = "Rechazo";
        int nj = indicesEstado.size();
        for (int ij = 0; ij <= nj - 1; ij++) {
            int j = (int) indicesEstado.get(ij);
            estado et = (estado) this.estados.get(j);
            if (et.getTipoEstado().equals("Aceptacion")) {
                tipoE = "Aceptacion";
            }
        }
        return tipoE;
    }

    /**
     * Metodo que recibe un ArrayList con indices de estados como parametro y
     * regresa el nombre de los parametros concatenados
     *
     * @param estados ArrayList con los estados de AFND
     * @param arrIndiceE
     * @return
     */
    public String decodificarNombreEstado(ArrayList<estado> estados, ArrayList arrIndiceE) {
        int n = arrIndiceE.size();
        String nombreEstado = "";
        for (int k = 0; k <= n - 1; k++) {
            int indiceEstado = (int) arrIndiceE.get(k);
            estado aux = (estado) estados.get(indiceEstado);
            nombreEstado = nombreEstado + aux.getNombreEstado();
        }
        return nombreEstado;
    }

    public matrizForma1 getMat() {
        return mat;
    }

    public ArrayList getSimbolosEntrada() {
        return simbolosEntrada;
    }

    public void setSimbolosEntrada(ArrayList simbolosEntrada) {
        this.simbolosEntrada = simbolosEntrada;
    }

    public ArrayList getEstados() {
        return estados;
    }

    public String recuperSimboloEntrada(int i) {
        String simbolo = simbolosEntrada.get(i).toString();
        return simbolo;
    }

    public estado recuperEstdo(int i) {
        estado state = (estado) estados.get(i);
        return state;
    }

    public File getFichero() {
        return fichero;
    }

    public void insertarEntrada(String entrada) {
        this.simbolosEntrada.add(entrada);
        tripleta t = (tripleta) mat.getMat().getDato();
        t.setColumna(t.getColumna() + 1);
        mat.getMat().setDato(t);
    }

    public void insertarEstado(estado estado) {
        this.estados.add(estado);
        tripleta t = (tripleta) mat.getMat().getDato();
        t.setFila(t.getFila() + 1);
        mat.getMat().setDato(t);
        mat.agregarNodoCabeza(this.estados.lastIndexOf(estado));
    }

    public boolean validarExistenciaEstado(estado state, String contenido) {
        boolean respuesta = false;

        int i = this.estados.lastIndexOf(estados);
        int j = this.simbolosEntrada.lastIndexOf(contenido);

        return respuesta;
    }

    /**
     * Metodo que inserta las transiciones en la matriz dispersa
     *
     * @param simboloEntrada variable que representa simbolo de entrada
     * @param estado variable que representa el estado actual
     * @param estadoTransicion variable q indica hacia donde se hara transicion
     */
    public void cargarTransicion(String simboloEntrada, String estado, String estadoTransicion) {

        int s = this.simbolosEntrada.lastIndexOf(simboloEntrada);
        int eo = this.buscarEstadoEnColeccion(estado);
        int ed = this.buscarEstadoEnColeccion(estadoTransicion);
        mat.agregarTransicion(s, eo, ed);
        mat.muestraMatriz();

    }

    public int buscarEstadoEnColeccion(String estado) {
        int respuesta = -1;
        estado st;

        for (int i = 0; i < this.estados.size(); i++) {
            st = (estado) this.estados.get(i);
            if (st.getNombreEstado().equals(estado)) {
                respuesta = i;
            }
        }

        return respuesta;
    }

    public boolean evaluarTipoAutomataEstadosInciales() {
        //Validemos si existen mas de dos estados iniciales
        ArrayList listaEstado = this.estados;
        estado st;
        boolean respuesta = false;
        int cont = 0;

        for (int i = 0; i < listaEstado.size(); i++) {
            st = (estado) listaEstado.get(i);
            if (st.isEstadoIncial()) {
                cont++;
            }

        }

        if (cont > 1) {
            respuesta = true;
        }

        return respuesta;

    }

    public boolean evaluarMasdeUnaTransicion() {
        Dnode p, q;
        ArrayList rta;
        tripleta tx = null;
        tripleta txs;
        boolean respueta = false;
        p = this.mat.primerNodo();

        while (!mat.findeRecorrido(p)) {
            q = p.getLd();

            while (p != q) {
                tx = (tripleta) q.getDato();
                rta = (ArrayList) tx.getValor();

                if (rta.size() == 2) {
                    respueta = true;
                }

                q = q.getLd();
            }

            txs = (tripleta) p.getDato();
            p = (Dnode) txs.getValor();

        }

        return respueta;

    }

    public void convertirDeterministicoC1() {
        //Buscar Estado Inicial
        Dnode p = this.mat.primerNodo();
        AF automaDete = new AF();
        automaDete.construirAutomata();
        Dnode q;
        int estado;
        estado st;
        tripleta tx, txs;
        ArrayList rta;
        String cadena = "";
        String auxCadena = "";
        char c;

        ArrayList simboloEntrada = this.simbolosEntrada;
        for (int i = 0; i < simboloEntrada.size(); i++) {
            automaDete.insertarEntrada(simboloEntrada.get(i).toString());
        }

        while (!mat.findeRecorrido(p)) {
            tx = (tripleta) p.getDato();
            estado = tx.getFila();

            st = (estado) this.estados.get(estado);
            if (st.isEstadoIncial()) {
                automaDete.insertarEstado(st);
                q = p.getLd();

                while (p != q) {
                    txs = (tripleta) q.getDato();
                    rta = (ArrayList) txs.getValor();
                    if (rta.size() == 1) {

                        int est = (Integer) rta.get(0);
                        if (automaDete.recuperEstdo(est) == null) {
                            //Crear el estado
                            estado estate = this.recuperEstdo(est);
                            automaDete.insertarEstado(estate);
                        }

                        automaDete.getMat().agregarTransicion(txs.getColumna(), txs.getFila(), (Integer) rta.get(0));

                    } else {
                        for (int k = 0; k < rta.size(); k++) {

                            cadena = cadena + rta.get(k).toString();

                        }
                        ArrayList<Boolean> listaRespuesta = new ArrayList();

                        for (int kl = 0; kl < cadena.length(); kl++) {
                            char d = cadena.charAt(kl);
                            int number = (int) d;
                            estado std = this.recuperEstdo(number);
                            if (std.getTipoEstado() == "Rechazo") {
                                listaRespuesta.add(false);
                            } else {
                                //Aceptacion
                                listaRespuesta.add(true);
                            }
                            auxCadena = auxCadena + std.getNombreEstado();

                        }
                        int acepta = 0;
                        int rechaza = 0;
                        //Validando
                        for (int cb = 0; cb < listaRespuesta.size(); cb++) {
                            boolean res = listaRespuesta.get(cb);
                            if (res == true) {
                                acepta++;
                            } else {
                                rechaza++;
                            }

                        }

                        String tipoEstado = "";

                        if (rechaza == cadena.length()) {
                            //Estado de Rechazo
                            tipoEstado = "Rechazo";
                        } else if (acepta == cadena.length()) {
                            //Estado Acepte
                            tipoEstado = "Aceptacion";
                        } else if (acepta >= 1) {
                            //Estado acepta
                            tipoEstado = "Aceptacion";
                        }

                        estado estadoDeterministico = new estado(auxCadena, tipoEstado, true);
                        automaDete.insertarEstado(estadoDeterministico);
                        automaDete.getMat().agregarTransicion(txs.getColumna(), txs.getFila(), automaDete.estados.lastIndexOf(estadoDeterministico));

                    }

                    q = q.getLd();

                }

                //Movamonos en la matriz que ya estoy construyendo
                Dnode cp, dp;
                cp = automaDete.getMat().primerNodo();

                while (!automaDete.getMat().findeRecorrido(cp)) {
                    dp = cp.getLd();

                    while (dp != cp) {
                        tripleta txsw = (tripleta) dp.getDato();
                        ArrayList val = (ArrayList) txsw.getValor();
                        int d = (Integer) val.get(0);
                        //automaDete.getMat().recuperaNodoCabeza(d);
                        String ca = String.valueOf(automaDete.getMat().recuperaNodoCabeza(d));
                        if (ca.length() == 1) {
                            //inserte normal
                            //Cargue la transicion
                            //automaDete.getMat().agregarTransicion(txsw.getColumna(),txsw.getFila(), estado);
                        }
                        if (ca.length() > 1) {
                            //Cocatene estados
                            ArrayList<Dnode> global = new ArrayList();
                            for (int hj = 0; hj < ca.length(); hj++) {
                                int id = (int) ca.charAt(hj);
                                Dnode pr = this.getMat().recuperaNodoCabeza(id);
                                Dnode pq = pr.getLd();

                                while (pq != pr) {
                                    global.add(pq);

                                    pq = pq.getLd();
                                }
                            }

                        }
                    }
                }

            }

            tripleta tfl = (tripleta) p.getDato();
            p = (Dnode) tfl.getValor();

        }

    }

    public void agruparNodos(ArrayList<Dnode> listaNodos) {
        ArrayList<Integer> entradas = new ArrayList();
        for (int i = 0; i < listaNodos.size(); i++) {
            Dnode p = (Dnode) listaNodos.get(i);
            tripleta tx = (tripleta) p.getDato();

            int number = (Integer) entradas.lastIndexOf(tx.getColumna());
            if (number == -1) {
                entradas.add(tx.getColumna());
            }

        }

        for (int j = 0; j < entradas.size(); j++) {

        }

    }

    /**
     * Metodo que carga el archivo en la aplicacion
     */
    public void cargaInterfaz(FormularioPrincipal view) {

        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        fc.setDialogTitle("Examinar Archivo");
        int result = fc.showSaveDialog(null);
        String aux = "";

        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Cargo Archivo");
            this.fichero = fc.getSelectedFile();

            try (FileReader fr = new FileReader(fichero)) {
                String cadena = "";
                int valor = fr.read();
                while (valor != -1) {
                    cadena = cadena + (char) valor;
                    valor = fr.read();

                }

                view.getAreaAutomata().setText(cadena);
                view.getAreaAutomata().setEditable(false);

                //view.getareaCadena().setText(cadena);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "No cargo ningun archivo");

        }

    }

    public void guardarArchivo() {

        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        fc.setDialogTitle("Examinar Archivo");
        int result = fc.showSaveDialog(null);
        String aux = "";

        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Cargo Archivo");
            this.fichero = fc.getSelectedFile();

        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "No cargo ningun archivo");

        }

    }

    public void convertirCadenaAutomata(String cadena, controlador c) {
        String[] x = cadena.split("\n");
        String[] simboloEntrada;
        String[] estados;
        String[] estadosIniciales;
        String[] estadoAceptacion;
        String[] transiciones;

        System.out.println("Simbolos de entrada:" + x[0]);
        simboloEntrada = x[0].split(",");

        System.out.println("Estados:" + x[1]);
        estados = x[1].split(",");

        System.out.println("Estado Inicial:" + x[2]);
        estadosIniciales = x[2].split(",");

        System.out.println("Estados Aceptacion:" + x[3]);
        estadoAceptacion = x[3].split(",");
        boolean estadoInicial = false;
        String tipoEstado = "";

        for (int k = 0; k < simboloEntrada.length - 1; k++) {
            this.insertarEntrada(simboloEntrada[k]);
            c.agregarColumasJtable(simboloEntrada[k]);
        }

        for (int feet = 0; feet < estados.length - 1; feet++) {

            if (validarEstadoInicial(estadosIniciales, estados[feet])) {
                estadoInicial = true;

            } else {
                estadoInicial = false;
            }

            if (this.validarEstadoAceptacion(estadoAceptacion, estados[feet])) {
                //Estado Aceptacion
                tipoEstado = "Aceptacion";
            } else {
                tipoEstado = "Rechazo";
            }

            System.out.println(estados[feet] + tipoEstado + estadoInicial);
            estado s = new estado(estados[feet], tipoEstado, estadoInicial);
            this.insertarEstado(s);
            c.agregarFilasJtable(estados[feet]);

        }

        System.out.println("Transicones");
        String[] tmp;

        for (int i = 4; i < x.length; i++) {
            System.out.println(x[i]);

            try {
                tmp = x[i].split(",");
                int fila = this.buscarEstadoEnColeccion(tmp[0]);
                int columna = this.simbolosEntrada.lastIndexOf(tmp[1]);
                cargarTransicion(tmp[1], tmp[0], tmp[2]);
                c.agregarTransicionJtableDesdeArchivo(fila, columna);
            } catch (Exception e) {

            }

        }

    }

    public boolean validarEstadoInicial(String[] estadoIniciales, String estado) {
        boolean respuesta = false;

        for (int i = 0; i < estadoIniciales.length; i++) {

            if (estadoIniciales[i].equals(estado)) {
                return true;
            }

        }

        return respuesta;

    }

    public boolean validarEstadoAceptacion(String[] estadosAceptacion, String estado) {
        boolean respuesta = false;

        for (int i = 0; i < estadosAceptacion.length; i++) {
            if (estadosAceptacion[i].equals(estado)) {
                respuesta = true;
            }
        }
        return respuesta;
    }

    public void guardarAutomataEnArchivo(String ruta) {
        String entrada = "";
        String estados = "";
        String estadoAceptacion = "";
        String estadosInciales = "";

        for (int j = 0; j < this.simbolosEntrada.size(); j++) {

            entrada = entrada + simbolosEntrada.get(j) + ",";
        }

        for (int i = 0; i < this.estados.size(); i++) {
            estado st = (estado) this.estados.get(i);

            estados = estados + st.getNombreEstado() + ",";

            if (st.isEstadoIncial()) {
                estadosInciales = estadosInciales + st.getNombreEstado() + ",";
            }

            if (st.getTipoEstado() == "Aceptacion") {
                estadoAceptacion = estadoAceptacion + st.getNombreEstado() + ",";
            }

        }

        //Cargar Transiciones
        System.out.println("Simbolos Entrada:" + entrada);
        System.out.println("Estados:" + estados);
        System.out.println("Estados Inciales:" + estadoAceptacion);
        System.out.println("Estados Aceptacion:" + estadoAceptacion);
        System.out.println("Lista de Transiciones:");
        ArrayList transi = this.cargarTransicionesArchivo();
        this.loadFile(ruta, entrada, estados, estadosInciales, estadoAceptacion, transi);

    }

    public ArrayList cargarTransicionesArchivo() {
        ArrayList listaTransiciones = new ArrayList();
        int qf, qc, qv;
        Dnode p, q;
        tripleta tq, tp;
        p = this.mat.primerNodo();
        String transiciones = "";

        while (!(this.mat.findeRecorrido(p))) {
            q = p.getLd();

            while (q != p) {
                tq = (tripleta) q.getDato();
                qf = tq.getFila();
                qc = tq.getColumna();
                //qv = (int) tq.getValor();
                ArrayList k = (ArrayList) tq.getValor();

                for (int i = 0; i < k.size(); i++) {
                    //System.out.println(qf + "-" + qc + "-" + k.get(i));
                    estado st = (estado) this.estados.get(qf);
                    String entrada = this.simbolosEntrada.get(qc).toString();
                    int valor = (Integer) k.get(i);
                    estado destino = (estado) this.estados.get(valor);
                    //transiciones=transiciones+st.getNombreEstado()+","+entrada+","+destino.getNombreEstado()+","+"\n";
                    transiciones = st.getNombreEstado() + "," + entrada + "," + destino.getNombreEstado() + ",";
                    System.out.println(transiciones);
                    listaTransiciones.add(transiciones);

                }

                q = q.getLd();

            }

            tp = (tripleta) p.getDato();
            p = (Dnode) tp.getValor();
        }

        return listaTransiciones;

    }

    public void loadFile(String nombreArchivo, String entrada, String estados, String estadosInciales, String estadoAceptacion, ArrayList transiciones) {
        try (PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter(nombreArchivo, true)))) {
            //out2.println(doc + "," + nombre + "," + apellido + "," + direccion + "," + telefono);

            out2.println(entrada);
            out2.println(estados);
            out2.println(estadosInciales);
            out2.println(estadoAceptacion);
            // out2.println(transiciones);
            for (int i = 0; i < transiciones.size(); i++) {
                if (i == transiciones.size() - 1) {
                    out2.print(transiciones.get(i));
                } else {
                    out2.println(transiciones.get(i));
                }

            }

            JOptionPane.showMessageDialog(null, "Archivo Creado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean probarHilera(String hilera) {

        Dnode p, q;
        int estadoAcutal;
        int st;
        //Dnode p = mat.primerNodo();
        tripleta tx;
        int tamHilera = hilera.length();
        estado estadoActual;
        boolean aceptacion = false;
        int contador = 0;
        int filaEstado = 0;
        char ca;

        for (int i = 0; i < tamHilera; i++) {
            ca = hilera.charAt(i);
            if (contador == 0) {
                estado status = recuperEstadoIncial();
                estadoActual = status;
                System.out.println("Estado Incial:" + estadoActual.getNombreEstado());
                filaEstado = this.estados.lastIndexOf(status);
                System.out.println(filaEstado);
                contador = contador + 1;
                p = this.mat.recuperaNodoCabeza(filaEstado);

                q = p.getLd();
                while (p != q) {
                    tripleta txs = (tripleta) q.getDato();
                    int entradNumero = txs.getColumna();
                    String de = (String) this.simbolosEntrada.get(entradNumero);

                    if (String.valueOf(ca).equals(de)) {
                        System.out.println("Entroooo");
                        ArrayList listEstadoActual = (ArrayList) txs.getValor();
                        int posEstadoActual = (Integer) listEstadoActual.get(0);
                        estadoActual = this.recuperEstdo(posEstadoActual);
                        System.out.println(estadoActual.getNombreEstado());
                        //filaEstado = txs.getFila();
                        filaEstado = posEstadoActual;

                        if (estadoActual.getTipoEstado().equals("Aceptacion")) {
                            aceptacion = true;
                        } else if (estadoActual.getTipoEstado().equals("Rechazo")) {
                            aceptacion = false;
                        }

                    }
                    q = q.getLd();
                }

            } else {
                Dnode pl = this.mat.recuperaNodoCabeza(filaEstado);
                Dnode ql = pl.getLd();

                while (pl != ql) {
                    tripleta txs = (tripleta) ql.getDato();
                    int entradNumero = txs.getColumna();
                    String de = (String) this.simbolosEntrada.get(entradNumero);

                    if (String.valueOf(ca).equals(de)) {

                        ArrayList listEstadoActual = (ArrayList) txs.getValor();
                        int posEstadoActual = (Integer) listEstadoActual.get(0);
                        estadoActual = this.recuperEstdo(posEstadoActual);
                        System.out.println(estadoActual.getNombreEstado());
                        //filaEstado = txs.getFila();
                        filaEstado = posEstadoActual;

                        if (estadoActual.getTipoEstado().equals("Aceptacion")) {
                            aceptacion = true;
                        } else if (estadoActual.getTipoEstado().equals("Rechazo")) {
                            aceptacion = false;
                        }

                    }

                    ql = ql.getLd();
                }

            }
        }

        return aceptacion;
    }

    public estado recuperEstadoIncial() {
        Dnode p = this.mat.primerNodo();
        estado st = null;
        while (!mat.findeRecorrido(p)) {
            tripleta txs = (tripleta) p.getDato();
            int filaEstado = (Integer) txs.getFila();
            st = (estado) this.estados.get(filaEstado);
            if (st.isEstadoIncial()) {
                return st;
            }

        }

        return st;
    }

    public void simplificarAutomataDeterministico(controlador c) {

        for (int i = 0; i < this.simbolosEntrada.size(); i++) {
            c.agregarColumnasJtableSimplificado((String) simbolosEntrada.get(i));
        }

        for (int j = 0; j < this.estados.size(); j++) {
            estado st = (estado) estados.get(j);
            c.agregarFilasJtableSimplificado(st.getNombreEstado());
        }

        Dnode p = this.mat.primerNodo();
        Dnode q;

        while (!mat.findeRecorrido(p)) {
            q = p.getLd();

            while (p != q) {
                tripleta tq = (tripleta) q.getDato();
                int qf = tq.getFila();
                int qc = tq.getColumna();
                ArrayList k = (ArrayList) tq.getValor();

                for (int i = 0; i < k.size(); i++) {
                    //System.out.println(qf + "-" + qc + "-" + k.get(i));
                    estado st = (estado) this.estados.get(qf);
                    String entrada = this.simbolosEntrada.get(qc).toString();
                    int valor = (Integer) k.get(i);
                    estado destino = (estado) this.estados.get(valor);
                    c.agregarTransicionesJtableSimplificado(destino.getNombreEstado(), qf, qc);

                }
                q = q.getLd();

            }

            tripleta txts = (tripleta) p.getDato();
            p = (Dnode) txts.getValor();
        }

    }

    public void convertirAutomata() {
        AF AFD = this.transformarAFNDaAFD();
        this.mat = AFD.mat;
        this.cadena = AFD.cadena;
        this.simbolosEntrada = AFD.simbolosEntrada;
        this.estados = AFD.estados;
    }

    public void cargarAutomataConvertido(controlador c) {
        for (int i = 0; i < this.simbolosEntrada.size(); i++) {

            c.agregarColumnasJtable3((String) simbolosEntrada.get(i));
        }

        for (int j = 0; j < this.estados.size(); j++) {
            estado st = (estado) estados.get(j);
            c.agregarFilasJtable3(st.getNombreEstado());
        }

        Dnode p = this.mat.primerNodo();
        Dnode q;

        while (!mat.findeRecorrido(p)) {
            q = p.getLd();

            while (p != q) {
                tripleta tq = (tripleta) q.getDato();
                int qf = tq.getFila();
                int qc = tq.getColumna();
                ArrayList k = (ArrayList) tq.getValor();

                for (int i = 0; i < k.size(); i++) {
                    //System.out.println(qf + "-" + qc + "-" + k.get(i));
                    estado st = (estado) this.estados.get(qf);
                    String entrada = this.simbolosEntrada.get(qc).toString();
                    int valor = (Integer) k.get(i);
                    estado destino = (estado) this.estados.get(valor);
                    //c.agregarTransicionesJtableSimplificado(destino.getNombreEstado(), qf, qc);
                    c.agregarTransicionesJtable3(destino.getNombreEstado(), qf, qc);

                }
                q = q.getLd();

            }

            tripleta txts = (tripleta) p.getDato();
            p = (Dnode) txts.getValor();
        }

    }

    /**
     * Método que halla estados equivalentes
     *
     */
    public void calcularEstadoEquivalentes() {

        Particion particionzero = new Particion();
        Particion particionone = new Particion();

        for (int i = 0; i < this.estados.size(); i++) {
            estado tmpstate = (estado) this.estados.get(i);

            if (tmpstate.getTipoEstado().equals("Rechazo")) {
                //Cree la Partición 0
                particionzero.setConjunto(0);
                particionzero.getEstados().add(tmpstate);
            }
            if (tmpstate.getTipoEstado().equals("Aceptacion")) {
                //Creer la Partición 1
                particionone.setConjunto(1);
                particionone.getEstados().add(tmpstate);
            }

        }

        this.particiones.add(particionzero);
        this.particiones.add(particionone);
        this.numeroParticiones = 2;

        for (int j = 0; j < this.simbolosEntrada.size(); j++) ///Simbolos
        {
            this.nuevaParticion = new ArrayList();
            Particion nuevaPar = new Particion();
            Particion basePar = new Particion();

            for (int i = 0; i < this.particiones.size(); i++) //Particiones
            {
                int flanco = 0;
                Particion particiontmp = (Particion) this.particiones.get(i);
                for (int k = 0; k < particiontmp.getEstados().size(); k++) {
                    estado state = particiontmp.getEstados().get(k);
                    int indice = this.estados.lastIndexOf(state);
                    ArrayList respuesta = this.mat.recuperarTransicion(indice, j);
                    int index = (int) respuesta.get(0);
                    estado estadoTransicion = (estado) this.estados.get(index);
                    boolean response = particiontmp.verificaPertenenciaParticion(estadoTransicion);

                    //System.out.println("Estado Origen" + state.getNombreEstado());
                    //System.out.println("Hace Transicion a" + estadoTransicion.getNombreEstado());
                    if (!response) {
                        flanco++;
                        nuevaPar.getEstados().add(state);

                    } else {
                        //System.out.println("holal"+state.getNombreEstado()); 
                        basePar.getEstados().add(state);
                        //state.getNombreEstado();
                    }

                }
                if (flanco != 0) {
                    
                     //Debe Agregar un nuevo Estado
                     //this.nuevaParticion.add(nuevaPar);
                    //Particion particionactualizada=this.reorganizarParticion(particiontmp);
                    //System.out.println("Particion---------------");
                    //particionactualizada.imprimirConjuntoParticion();
                    //basePar.imprimirConjuntoParticion();
                    //this.particiones.add(nuevaPar);
                    //this.particiones.set(i,basePar);
                    this.particiones=new ArrayList();
                    this.particiones.add(basePar);
                    this.particiones.add(nuevaPar);
                    
                    //this.particiones.remove(i);
                    //this.particiones.add(basePar);
                    System.out.println("------Hello----");
                     //this.recorrerParticiones();
                    
                   
                    
                    //System.out.println("Particion-------------------");
                    //nuevaPar.imprimirConjuntoParticion();

                   

                }
               
                
                 
            }
            
            
            
            

        }
        
       this.recorrerParticiones();


       

    }

    public Particion reorganizarParticion(Particion group) {
        //Vieja Particion, actualiza y luego agregar la nueva particion en array list de particiones
        ArrayList listadoEstado = group.getEstados();

        for (int i = 0; i < listadoEstado.size(); i++) {
            estado tmp = (estado) listadoEstado.get(i);
            Particion parti = (Particion) this.nuevaParticion.get(0);
            //int response = parti.getEstados().lastIndexOf(tmp);

            boolean respon = parti.verificaPertenenciaParticion(tmp);
            //System.out.println(response);
            /*if (response != -1) {
                listadoEstado.remove(response);
            }
            
            
             */
            if (respon) {
                //Eliminelo

                listadoEstado.remove(i);
            }
        }

        Particion particionclonada = new Particion();

        particionclonada.setEstados(listadoEstado);
        return particionclonada;

    }

    public void recorrerParticiones() {
        for (int i = 0; i < this.particiones.size(); i++) {
            System.out.println("Particion");
            Particion particion = (Particion) this.particiones.get(i);
            particion.imprimirConjuntoParticion();
        }
    }

    public void checkConjuntoTransicion(int simobolo, Particion group) {
        ArrayList conjuntoTmp = group.getEstados();

        for (int i = 0; i < conjuntoTmp.size(); i++) {
            estado estadocheck = (estado) conjuntoTmp.get(i);
            int filaEstado = this.estados.lastIndexOf(estadocheck);
            int columna = simobolo;
            ArrayList respuesta = this.mat.recuperarTransicion(filaEstado, columna);//Se recupera es un index del arraylist
            int index = (int) respuesta.get(0);
            estado estadoTransicion = (estado) this.estados.get(index);
            boolean response = group.verificaPertenenciaParticion(estadoTransicion);

            if (!response && this.nuevaParticion.size() == 0) {
                Particion nuevaPar = new Particion();
                nuevaPar.getEstados().add(estadocheck);
                this.nuevaParticion.add(nuevaPar);

            }

        }

    }

}
