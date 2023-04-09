import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.util.ArrayList;
import java.util.List;

public class Ronda {
    private String nro;
    private List<Partido> partidos;

    public String getNro() {
        return this.nro;
    }

    public List<Partido> getPartidos() {
        return this.partidos;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    public Ronda() {
        this.nro = "";
        this.partidos = new ArrayList<Partido>();
    }

    public Ronda(String nro) {
        this.nro = nro;
        this.partidos = new ArrayList<Partido>();
    }

    public boolean aciertoTotalRonda(List<Pronostico> pronosticosRonda) {
        int aciertos = 0;
        for (Pronostico pronostico : pronosticosRonda) {
            ResultadoEmun resultadoEmun = pronostico.getPartido().resultado(pronostico.getEquipo());
            //Acierta el resultado del partido y devuelve los ptos que le corresponde.
            if (resultadoEmun.getEstado() == pronostico.getResultado().getEstado()) {
                aciertos++;
            }
        }
        if (aciertos == pronosticosRonda.size()) {
            return true;
        }
        return false;
    }

    public int puntos(int puntoExtra, List<Pronostico> pronosticosRonda) {
//        int aciertos = 0;
//        for (Pronostico pronostico : pronosticosRonda) {
//            ResultadoEmun resultadoEmun = pronostico.getPartido().resultado(pronostico.getEquipo());
//            //Acierta el resultado del partido y devuelve los ptos que le corresponde.
//            if (resultadoEmun.getEstado() == pronostico.getResultado().getEstado()) {
//                aciertos++;
//            }
//        }
//        if (aciertos == pronosticosRonda.size()) {
//            return puntoExtra;
//        }
        if (this.aciertoTotalRonda(pronosticosRonda)) {
            return puntoExtra;
        }
        return 0;
    }
}
