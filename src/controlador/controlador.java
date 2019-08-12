/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Modelo.AF;
import Modelo.Dnode;
import Modelo.estado;
import Modelo.tripleta;
import Vista.FormularioPrincipal;
import Vista.PanelTransiciones;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class controlador implements ActionListener, MouseListener {

    private FormularioPrincipal view;
    private PanelTransiciones pt;
    private AF modelo;
    private int contador;
    private int auxCont;
    private int secondContador;
    private int thirContador;
    private String estado;
    private String simboloEntrada;
    private int filas;
    private int columnas;
    private JList liState;
    private DefaultListModel mod;
    private DefaultTableModel model;
    private DefaultTableModel model1;
    private int kt;
    private TableCellRendererColor trc;
    private boolean habilitarTabla;

    public controlador(FormularioPrincipal view, AF modelo) {
        this.view = view;
        this.modelo = modelo;
        modelo.construirAutomata();
        this.contador = 0;
        this.auxCont = 0;
        this.secondContador = 0;
        this.thirContador = 0;
        this.pt = new PanelTransiciones();
        this.filas = 0;
        this.columnas = 0;
        this.kt = 0;
        this.trc = new TableCellRendererColor();
        this.view.getTablaEstado().setDefaultRenderer(Object.class, trc);
        this.habilitarTabla = true;

    }

    /**
     * Metodo que carga los Eventos en los objetos Swing
     */
    public void cargarAtributos() {
        this.view.getbutonAgregarEstado().setActionCommand("AgregarEstado");
        this.view.getbutonAgregarEstado().addActionListener(this);
        this.view.getbutonAgregarSimboloEntrada().setActionCommand("AgregarSimboloEntrada");
        this.view.getbutonAgregarSimboloEntrada().addActionListener(this);
        this.view.gettablaTransicion().addMouseListener(this);
        this.pt.getListaEstados().addMouseListener(this);
        this.pt.getButonEliminarEstado().setActionCommand("EliminarEstado");
        this.pt.getButonEliminarEstado().addActionListener(this);
        this.view.getBotonConfirmarAutomata().setActionCommand("ConfirmandoAutomata");
        this.view.getBotonConfirmarAutomata().addActionListener(this);
        this.view.getbotoConvertirDeterministico().setActionCommand("ConvertirDeterministico");
        this.view.getbotoConvertirDeterministico().addActionListener(this);
        this.view.getExaminaArchivo().setActionCommand("ExaminarArchivo");
        this.view.getExaminaArchivo().addActionListener(this);
        this.view.getbotonEditarAutomata().setActionCommand("EditarAutomata");
        this.view.getbotonEditarAutomata().addActionListener(this);
        this.view.getMenuExportarFile().setActionCommand("Guardando Archivo");
        this.view.getMenuExportarFile().addActionListener(this);
        this.view.getButonEvaluarHilera().setActionCommand("EvaluarHilera");
        this.view.getButonEvaluarHilera().addActionListener(this);
        this.view.getBotonSimplificar().setActionCommand("SimplificarAutomataDeterministico");
        this.view.getBotonSimplificar().addActionListener(this);
        this.view.getBotonAbrirNuevoAutomata().setActionCommand("AbrirNuevoAutomata");
        this.view.getBotonAbrirNuevoAutomata().addActionListener(this);

    }

    /**
     * Metodo que muestrar el formulario
     */
    public void mostrar() {
        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    /**
     * Metodo donde se ejecutan los eventos actionPerformed
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        String comando = e.getActionCommand();

        if (comando.equals("AgregarEstado")) {

            String contenido = this.view.getcajaEstado().getText();
            if (contenido.equals("") || this.view.getTipoEstado().getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(null, "Campos Incompletos...debe ingresar Estado y tipo Estado");

            } else {

                if (auxCont == 0 && !this.view.getEstadoInicial().isSelected()) {

                    //int dialogButton = JOptionPane.YES_NO_OPTION;
                    int response = JOptionPane.showConfirmDialog(null, "Este Es el primer estado ingresado,Desea establecerlo como estado inical?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    // JOptionPane.showConfirmDialog(null, "Este Es el primer estado ingresado,Desea establecerlo como estado inical?", "Warning", dialogButton);
                    if (response == JOptionPane.YES_OPTION) {
                        this.view.getEstadoInicial().setSelected(true);

                    } else if (response == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(null, "Continue sin  problema");

                        String tipoEstado = "";

                        if (this.view.getTipoEstado().getSelectedIndex() == 1) {
                            tipoEstado = "Aceptacion";
                        } else if (this.view.getTipoEstado().getSelectedIndex() == 2) {
                            tipoEstado = "Rechazo";

                        }
                        estado st = new estado(contenido, tipoEstado, false);
                        modelo.insertarEstado(st);
                        this.auxCont++;
                        this.agregarFilasJtable(contenido);
                        this.view.getcajaEstado().setText("");
                        this.view.getEstadoInicial().setSelected(false);
                        this.view.getTipoEstado().setSelectedIndex(0);

                    }

                } else {
                    boolean tipo = false;
                    String tipoEstado = "";
                    if (this.view.getEstadoInicial().isSelected()) {
                        tipo = true;
                    }

                    if (this.view.getTipoEstado().getSelectedIndex() == 1) {
                        tipoEstado = "Aceptacion";
                    } else if (this.view.getTipoEstado().getSelectedIndex() == 2) {
                        tipoEstado = "Rechazo";

                    }
                    estado st = new estado(contenido, tipoEstado, tipo);
                    modelo.insertarEstado(st);
                    this.auxCont++;
                    this.agregarFilasJtable(contenido);
                    this.view.getcajaEstado().setText("");
                    this.view.getEstadoInicial().setSelected(false);
                    this.view.getTipoEstado().setSelectedIndex(0);
                    //this.pintarEstadosInciales(contenido);
                }

            }

        } else if (comando.equals("AgregarSimboloEntrada")) {
            String contenido = this.view.getcajaSimoboloEntrada().getText();
            if (contenido.equals("")) {
                JOptionPane.showMessageDialog(null, "No ingreso Simobolo de Entrada...Debe Ingresar uno");

            } else {
                JOptionPane.showMessageDialog(null, "Agregando Simbolo de Entrada " + contenido);
                modelo.insertarEntrada(contenido);
                this.agregarColumasJtable(contenido);
                this.view.getcajaSimoboloEntrada().setText("");

            }

        } else if (comando.equals("AgregarTransicion")) {

            JComboBox jc = this.pt.getEstadoDestino();
            if (jc.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione Estado!!!");
            } else {

                String cadena = "";
                DefaultTableModel model = (DefaultTableModel) this.view.gettablaTransicion().getModel();
                modelo.cargarTransicion(this.simboloEntrada, this.estado, jc.getSelectedItem().toString());
                ArrayList rta = modelo.getMat().recuperarTransicion(this.filas, this.columnas);
                liState = this.pt.getListaEstados();
                mod = new DefaultListModel();

                for (int k = 0; k < rta.size(); k++) {

                    estado st = modelo.recuperEstdo((Integer) rta.get(k));
                    mod.addElement(st.getNombreEstado());

                    if (k == 0) {
                        cadena = st.getNombreEstado();
                    } else {
                        cadena = cadena + "," + st.getNombreEstado();
                    }

                }

                liState.setModel(mod);
                JOptionPane.showMessageDialog(null, "Inserte en fila " + this.filas + " y Columna " + this.columnas);
                model.setValueAt(cadena, this.filas, this.columnas);

            }
        } else if (comando.equals("EliminarEstado")) {

            if (liState.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione Un estado");
            } else {
                if (mod.getSize() > 0) {
                    int n = liState.getSelectedIndex();
                    this.modelo.getMat().eliminarTransicion(filas, columnas, n);
                    this.mod.removeElementAt(n);
                    liState.setModel(mod);
                    this.agregarTransicionJtable();

                }
            }

        } else if (comando.equals("ConfirmandoAutomata")) {
            int response = JOptionPane.showConfirmDialog(null, "Esta Seguro de confirmar Automata?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Automata Confirmado");
                this.habilitarTabla = false;
                this.view.getTablaEstado().setEnabled(false);
                this.view.getBotonConfirmarAutomata().setEnabled(false);
                this.view.getbotonEditarAutomata().setEnabled(true);
                this.view.getcajaEstado().setEditable(false);
                this.view.getbutonAgregarEstado().setEnabled(false);
                this.view.getcajaSimoboloEntrada().setEditable(false);
                this.view.getbutonAgregarSimboloEntrada().setEnabled(false);
                this.view.getEstadoInicial().setEnabled(false);
                this.view.getTipoEstado().setEnabled(false);
                this.view.getCajaHileraProbar().setEnabled(true);
                this.view.getCajaRespuestaHilera().setEnabled(true);
                this.view.getButonEvaluarHilera().setEnabled(true);

                if (this.modelo.evaluarTipoAutomataEstadosInciales() || this.modelo.evaluarMasdeUnaTransicion()) {
                    this.view.getCajaEvaluacion().setEnabled(true);
                    this.view.getCajaEvaluacion().setText("No Deterministico");
                    this.view.getbotoConvertirDeterministico().setEnabled(true);
                    this.view.getButonEvaluarHilera().setEnabled(false);
                    this.view.getCajaSimplificar().setText("No Deterministico");
                    this.view.getBotonSimplificar().setEnabled(false);
                } else {
                    this.view.getCajaEvaluacion().setEnabled(true);
                    this.view.getCajaEvaluacion().setText("Deterministico");
                    this.view.getCajaSimplificar().setText("Deterministico");
                    this.view.getCajaSimplificar().setEditable(false);
                    this.view.getBotonSimplificar().setEnabled(true);
                    this.view.getButonEvaluarHilera().setEnabled(true);
                    

                }

            }

        } else if (comando.equals("ConvertirDeterministico")) {
            JOptionPane.showMessageDialog(null, "Conviertiendo a Deterministico");
            this.modelo.convertirAutomata();
            this.modelo.cargarAutomataConvertido(this);
            this.view.getbotoConvertirDeterministico().setEnabled(false);

        } else if (comando.equals("ExaminarArchivo")) {
            JOptionPane.showMessageDialog(null, "Examinando Archivo..");
            modelo.cargaInterfaz(this.view);

            if (modelo.getFichero() == null) {
                this.view.getCajaArchivoCargado().setText("Archivo No Cargado");
            } else {
                this.view.getCajaArchivoCargado().setText("");
                this.view.getCajaArchivoCargado().setText(modelo.getFichero().getAbsolutePath());
                String info = this.view.getAreaAutomata().getText();
                this.modelo.convertirCadenaAutomata(info, this);

            }
        } else if (comando.equals("EditarAutomata")) {

            int response = JOptionPane.showConfirmDialog(null, "Esta Seguro de confirmar Automata?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Habilitando Automata Nuevamente..");
                this.view.getbotonEditarAutomata().setEnabled(false);
                this.view.getBotonConfirmarAutomata().setEnabled(true);
                this.view.getcajaSimoboloEntrada().setEditable(true);
                this.view.getbutonAgregarSimboloEntrada().setEnabled(true);

                this.view.getcajaEstado().setEditable(true);
                this.view.getEstadoInicial().setEnabled(true);
                this.view.getTipoEstado().setEnabled(true);
                this.view.getbutonAgregarEstado().setEnabled(true);
                this.habilitarTabla = true;
            }

        } else if (comando.equals("Guardando Archivo")) {
            int response = JOptionPane.showConfirmDialog(null, "Desea Exportar Automata Creado?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Guardando Automata");
                this.modelo.guardarArchivo();
                String ruta = modelo.getFichero().getAbsolutePath();
                File archivo = new File(ruta);
                BufferedWriter bw = null;

                if (archivo.exists()) {
                    try {
                        bw = new BufferedWriter(new FileWriter(archivo));
                    } catch (IOException ex) {
                        Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        bw = new BufferedWriter(new FileWriter(archivo));
                    } catch (IOException ex) {
                        Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.modelo.guardarAutomataEnArchivo(ruta);
                }
                try {
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(controlador.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else if (comando.equals("EvaluarHilera")) {
            JOptionPane.showMessageDialog(null, "Evaluando Hilera");
            if (this.view.getCajaHileraProbar().getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe Ingresar una Hilera!!");
            } else {
                if (this.modelo.probarHilera(this.view.getCajaHileraProbar().getText())) {
                    //Acepte Secuencia
                    this.view.getCajaRespuestaHilera().setText("Acepte");
                } else {
                    //Rechace secuencia
                    this.view.getCajaRespuestaHilera().setText("Rechace");
                }
            }
        } else if (comando.equals("SimplificarAutomataDeterministico")) {
            JOptionPane.showMessageDialog(null, "Simplificando Automata..");
            //this.modelo.simplificarAutomataDeterministico(this);
            this.modelo.calcularEstadoEquivalentes();
            this.view.getBotonSimplificar().setEnabled(false);

        } else if (comando.equals("AbrirNuevoAutomata")) {
            int response = JOptionPane.showConfirmDialog(null, "Esta Seguro de Abrir un Nuevo Automata?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Abriendo Nueva Ventana");

                FormularioPrincipal view2 = new FormularioPrincipal();
                AF model2 = new AF();

                controlador c = new controlador(view2, model2);

                c.cargarAtributos();
                c.mostrar();
                view2.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            }
        }

    }

    public void agregarColumasJtable(String contenido) {
        DefaultTableModel model = (DefaultTableModel) this.view.gettablaTransicion().getModel();
        model.addColumn(contenido);
    }

    public void agregarFilasJtable(String contenido) {
        model = (DefaultTableModel) this.view.gettablaTransicion().getModel();
        model1 = (DefaultTableModel) this.view.getTablaEstado().getModel();

        if (this.contador == 0) {
            model1.addColumn("Estado");
            contador++;
        }

        String[] estado = {contenido};
        model1.addRow(estado);
        //this.trc=new TableCellRendererColor();
        model.addRow(new Object[]{});
        //trc.setCadena(contenido);

        //this.view.getTablaEstado().setDefaultRenderer(Object.class, trc);
    }

    public void agregarTransicionJtable() {
        String cadena = "";
        DefaultTableModel model = (DefaultTableModel) this.view.gettablaTransicion().getModel();
        Dnode answer = modelo.getMat().recuperNodo(this.filas, this.columnas);
        if (answer == null) {
            model.setValueAt("", this.filas, this.columnas);
        } else {

            ArrayList rta = modelo.getMat().recuperarTransicion(this.filas, this.columnas);
            liState = this.pt.getListaEstados();
            mod = new DefaultListModel();

            for (int k = 0; k < rta.size(); k++) {

                estado st = modelo.recuperEstdo((Integer) rta.get(k));
                mod.addElement(st.getNombreEstado());

                if (k == 0) {
                    cadena = st.getNombreEstado();
                } else {
                    cadena = cadena + "," + st.getNombreEstado();
                }

            }

            model.setValueAt(cadena, this.filas, this.columnas);
            liState.setModel(mod);

        }

    }

    public void agregarColumnasJtableSimplificado(String contenido) {
        DefaultTableModel model = (DefaultTableModel) this.view.getTablaTransicion2().getModel();
        model.addColumn(contenido);
    }

    public void agregarFilasJtableSimplificado(String contenido) {
        model = (DefaultTableModel) this.view.getTableEstados2().getModel();
        model1 = (DefaultTableModel) this.view.getTablaTransicion2().getModel();

        if (this.secondContador == 0) {
            model.addColumn("Estado");
            secondContador++;
        }

        String[] estado = {contenido};
        model.addRow(estado);
        model1.addRow(new Object[]{});

    }

    public void agregarTransicionesJtableSimplificado(String contenido, int fila, int columna) {
        String cadena = "";
        DefaultTableModel model = (DefaultTableModel) this.view.getTablaTransicion2().getModel();
        Dnode answer = modelo.getMat().recuperNodo(fila, columna);

        if (answer == null) {
            model.setValueAt("", fila, columna);
        } else {
            ArrayList rta = modelo.getMat().recuperarTransicion(fila, columna);
            liState = this.pt.getListaEstados();
            mod = new DefaultListModel();

            for (int k = 0; k < rta.size(); k++) {

                estado st = modelo.recuperEstdo((Integer) rta.get(k));
                mod.addElement(st.getNombreEstado());

                if (k == 0) {
                    cadena = st.getNombreEstado();
                } else {
                    cadena = cadena + "," + st.getNombreEstado();
                }

            }

            model.setValueAt(cadena, fila, columna);
            liState.setModel(mod);
        }

    }

    public void agregarColumnasJtable3(String contenido) {
        DefaultTableModel model = (DefaultTableModel) this.view.getTablaTransiciones3().getModel();
        model.addColumn(contenido);
    }

    public void agregarFilasJtable3(String contenido) {
        model = (DefaultTableModel) this.view.getTablaEstados3().getModel();
        model1 = (DefaultTableModel) this.view.getTablaTransiciones3().getModel();

        if (this.thirContador == 0) {
            model.addColumn("Estado");
            thirContador++;
        }

        String[] estado = {contenido};
        model.addRow(estado);
        model1.addRow(new Object[]{});
    }

    /**
     * Metodo que agrega Transicones al automata ya convertido
     *
     * @param contenido, nombre del estado al hacer transicion
     * @param fila,fila
     * @param columna,columna
     */
    public void agregarTransicionesJtable3(String contenido, int fila, int columna) {
        String cadena = "";
        DefaultTableModel model = (DefaultTableModel) this.view.getTablaTransiciones3().getModel();
        Dnode answer = modelo.getMat().recuperNodo(fila, columna);
        
            if (answer == null) {
            model.setValueAt("", fila, columna);
        } else {
            ArrayList rta = modelo.getMat().recuperarTransicion(fila, columna);
            liState = this.pt.getListaEstados();
            mod = new DefaultListModel();

            for (int k = 0; k < rta.size(); k++) {

                estado st = modelo.recuperEstdo((Integer) rta.get(k));
                mod.addElement(st.getNombreEstado());

                if (k == 0) {
                    cadena = st.getNombreEstado();
                } else {
                    cadena = cadena + "," + st.getNombreEstado();
                }

            }

            model.setValueAt(cadena, fila, columna);
            liState.setModel(mod);
        }

        
    }

    /**
     * Metodo que agrega Transicion Desde un archivo
     */
    public void agregarTransicionJtableDesdeArchivo(int fila, int columna) {
        String cadena = "";
        DefaultTableModel model = (DefaultTableModel) this.view.gettablaTransicion().getModel();
        Dnode answer = modelo.getMat().recuperNodo(fila, columna);
        if (answer == null) {
            model.setValueAt("", fila, columna);
        } else {

            ArrayList rta = modelo.getMat().recuperarTransicion(fila, columna);
            liState = this.pt.getListaEstados();
            mod = new DefaultListModel();

            for (int k = 0; k < rta.size(); k++) {

                estado st = modelo.recuperEstdo((Integer) rta.get(k));
                mod.addElement(st.getNombreEstado());

                if (k == 0) {
                    cadena = st.getNombreEstado();
                } else {
                    cadena = cadena + "," + st.getNombreEstado();
                }

            }

            model.setValueAt(cadena, fila, columna);
            liState.setModel(mod);

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == this.view.gettablaTransicion()) {
            //int filas = this.view.gettablaTransicion().rowAtPoint(e.getPoint());
            //int columnas = this.view.gettablaTransicion().columnAtPoint(e.getPoint());

            if (this.habilitarTabla) {
                this.filas = this.view.gettablaTransicion().rowAtPoint(e.getPoint());
                this.columnas = this.view.gettablaTransicion().columnAtPoint(e.getPoint());
                //JOptionPane.showMessageDialog(null, "Seleccino Fila " + filas + " Columna" + columnas);

                this.pt = new PanelTransiciones();
                this.pt.botonAgregarTransicion().setActionCommand("AgregarTransicion");
                this.pt.botonAgregarTransicion().addActionListener(this);
                this.pt.botonCerrarTransicion().setActionCommand("CerrarPanelTransicion");
                this.pt.botonCerrarTransicion().addActionListener(this);
                this.pt.getButonEliminarEstado().setActionCommand("EliminarEstado");
                this.pt.getButonEliminarEstado().addActionListener(this);

                ArrayList estados = modelo.getEstados();

                for (int k = 0; k < estados.size(); k++) {

                    estado x = (estado) estados.get(k);
                    this.pt.getEstadoDestino().addItem(x.getNombreEstado());
                }

                //ArrayList listaEstados = modelo.getMat().recuperarTransicion(this.filas, this.columnas);
                this.agregarTransicionJtable();
                pt.setLocationRelativeTo(null);
                pt.show();
                String dato = modelo.recuperSimboloEntrada(columnas);
                estado estado = modelo.recuperEstdo(filas);
                pt.getEstado().setText("Estado Atual " + estado.getNombreEstado());
                this.estado = estado.getNombreEstado();
                this.simboloEntrada = dato;
                pt.getSimboloEntrada().setText("Simbolo de entrada " + dato);
            } else {
                JOptionPane.showMessageDialog(null, "Tabla bloqueda!!");
            }

        } else if (e.getSource() == this.pt.getListaEstados()) {
            JOptionPane.showMessageDialog(null, "Selecciono list");
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
