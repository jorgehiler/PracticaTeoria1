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
public class estadoFusionado {

    estado e;
    ArrayList transiciones;
    int indiceEstadoExistente = 0;
    String nombreEstadoExistente = "";
    String nombrenuevoEstado = "";
    private ArrayList casos = new ArrayList();

    public estadoFusionado(estado e, ArrayList transiciones) {
        this.e = e;
        this.transiciones = transiciones;
    }

    public estado getE() {
        return e;
    }

    public void setE(estado e) {
        this.e = e;
    }

    public ArrayList getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(ArrayList transiciones) {
        this.transiciones = transiciones;
    }

    /**
     * Metodo que retorna true si un estado "nombreEstado" existe y false si no. Si el estado no existe y retorna false actualiza la variable nombreNuevoEstado, que es el nombre del estado que se creará luego de que se evaluara que no existe.
     * @param estadosExistentes Lista de estados del autómata en el que se evaluará la existencia del estado
     * @param nombreEstado nombre del estdo a evaluar existencia
     * @return 
     */
    public boolean existeEstado(ArrayList estadosExistentes, String nombreEstado) {
        String estado = "";
        //Construir string de estado a evaluar Existencia
        for (int i = 1; i <= this.transiciones.size(); i++) {
            int k = (int) this.transiciones.get(i - 1);
            estado = estado + Integer.toString(k);
        }
        //Permutar estado a evaluar Existencia
        permute(estado, 0, estado.length());
        //Buscarlo entre estados existentes
        for (int i = 0; i <= estadosExistentes.size() - 1; i++) {
            estado aux = (estado) estadosExistentes.get(i);
            String estadoAnalizado = aux.getNombreEstado();
            if (estadoAnalizado.equals(nombreEstado)) {
                this.indiceEstadoExistente = i;
                this.nombreEstadoExistente = nombreEstado;
                return true;
            }
        }
        this.nombrenuevoEstado = estado;
        return false;
    }

    public int getIndiceEstadoExistente() {
        return indiceEstadoExistente;
    }

    public String getNombreEstadoExistente() {
        return nombreEstadoExistente;
    }

    public String getNombrenuevoEstado() {
        return nombrenuevoEstado;
    }

    private void permute(String str, int l, int r) {
        try {
            if (l == r) {
                System.out.println(str);
                casos.add(str);
            } else {
                for (int i = l; i <= r; i++) {
                    str = swap(str, l, i);
                    permute(str, l + 1, r);
                    str = swap(str, l, i);
                }
            }

        } catch (Exception e) {
            System.out.println("Ocurrio un error en la permutación: " + e);
        }
    }

    /**
     * Swap Characters at position
     *
     * @param a string value
     * @param i position 1
     * @param j position 2
     * @return swapped string
     */
    public String swap(String a, int i, int j) {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

}
