/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;

/**
 *
 * @author Carlos Benavidez
 */
public class Particion {
    
    
    private int conjunto;
    private ArrayList<estado> estados;

    public Particion() {
        this.conjunto=0;
        this.estados=new ArrayList<>();
    }

   
    
    public Particion(int conjunto, ArrayList<estado> estados) {
        this.conjunto = conjunto;
        this.estados = estados;
    }

    public int getConjunto() {
        return conjunto;
    }

    public void setConjunto(int conjunto) {
        this.conjunto = conjunto;
    }

    public ArrayList<estado> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<estado> estados) {
        this.estados = estados;
    }
    
   public boolean  verificaPertenenciaParticion(estado estado)
   {
      //int answer=this.estados.lastIndexOf(estado);
      
      for(int i=0;i<this.estados.size();i++)
      {
          estado sta=this.estados.get(i);
          if(sta.getNombreEstado().equals(estado.getNombreEstado()))
          {
              return true;
          }
      }
      
      return false;
      
      /*
      
      if(answer==-1)
      {    System.out.println("No belong"+estado.getNombreEstado());
          return false;
      }
      else{
          System.out.println("belong"+estado.getNombreEstado());
          return true;
      }
     
      
      */
     
   }
   
   public void imprimirConjuntoParticion()
   {   
       for(int i=0;i<this.estados.size();i++)
       {   estado state=this.estados.get(i);
           System.out.println("Estado"+state.getNombreEstado());
       }
   }

    
    
    
    
    
}
