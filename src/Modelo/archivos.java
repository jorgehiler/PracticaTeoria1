package modelo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class archivos {

    private File archivo;
    private String nombreArchivo;

    public archivos(String archivo) {
        this.nombreArchivo = archivo;
    }

    public void conectar() {
        archivo = new File(nombreArchivo);
        
        
        
    }
    public File getArchivo()
    {
        return this.archivo;
    }
    
    

    public String[] cadena() throws FileNotFoundException, IOException {
        String delimitador = ",";
        String[] cadena = null;

        try {
            FileInputStream fstream = new FileInputStream(nombreArchivo);
            DataInputStream entrada = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            int i = 0;

            while ((strLinea = buffer.readLine()) != null) {

                i++;

            }

            cadena = new String[i];
            fstream.close();
            entrada.close();
            buffer.close();

            fstream = new FileInputStream(nombreArchivo);
            entrada = new DataInputStream(fstream);
            buffer = new BufferedReader(new InputStreamReader(entrada));

            for (int j = 0; j < i; j++) {
                cadena[j] = buffer.readLine();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return cadena;
    }

    public String[] dividirLineas(String cadena,int tama) {
         String[] dato=null;
         String delimitador = ",";
        if(tama==3)
        {
               dato = new String[tama];
        }
        else if(tama==4)
        {
            dato = new String[tama];
        }
        else if(tama==5)
        {
            dato = new String[tama];
        }
             
     
        //String[] dato ;
        int contador = 0;
        try {

            StringTokenizer st = new StringTokenizer(cadena, delimitador);
            while (st.hasMoreElements()) {

                dato[contador] = st.nextToken();
                contador++;
            }

        } catch (Exception e) {
           //JOptionPane.showMessageDialog(null, "Ocurrio un error:" + e.getMessage());
        }


        
        
        
        
        return dato;
       
    }

}