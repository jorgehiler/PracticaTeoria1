package controlador;

import Modelo.AF;
import Vista.FormularioPrincipal;
import javax.sound.midi.ControllerEventListener;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Main {

    public static void main(String[] args) {

        FormularioPrincipal view = new FormularioPrincipal();
        AF model = new AF();

        controlador c = new controlador(view, model);

        c.cargarAtributos();
        c.mostrar();
        view.setDefaultCloseOperation(DISPOSE_ON_CLOSE); 

    }

}
