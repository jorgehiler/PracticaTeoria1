/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Modelo.estado;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Cesar Benavidez
 */
public class TableCellRendererColor extends DefaultTableCellRenderer {

    private Component componente;
    private String cadena;
    private estado st;
    private int fila;

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public void setEstado(estado st) {
        this.st = st;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
     

            if (String.valueOf(table.getValueAt(row,column)).equals(this.cadena)) {
                setBackground(Color.red);

            }
        

        return componente;

    }

}
