/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;

/**
 *
 * @author Jorge
 */
public final class ut {

    /**
     *
     * @param nodo Metodo para obtener el ArrayList que se
     * encuentra como dato en una tripleta esta tripleta se encuentra en un
     * objeto Dnode.
     * @return
     */
    public static ArrayList getArrayDataDnode(Dnode nodo) {
        tripleta tp = (tripleta) nodo.getDato();
        return (ArrayList) tp.getValor();
    }
    
    public static tripleta getTripletaDode (Dnode nodo){
        tripleta tp = (tripleta) nodo.getDato();
        return tp;    
    }

    public static Dnode getDnodeData(Dnode nodo) {
        tripleta axt = (tripleta) nodo.getDato();
        Dnode x = (Dnode) axt.getValor();
        return x;
    }

    public static String getNombreEstado(ArrayList estados, int posicion) {
        estado aux = (estado) estados.get(posicion); //EstadoInicial
        String nombreEstado = aux.getNombreEstado(); //Estado inicial
        return nombreEstado;

    }

    public static String getTipoEstado(ArrayList estados, int posicion) {
        estado aux = (estado) estados.get(posicion); //EstadoInicial
        String tipoEstado = aux.getTipoEstado(); //EstadoIicial
        return tipoEstado;

    }

}
