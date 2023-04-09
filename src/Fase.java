import java.util.ArrayList;
import java.util.List;

public class Fase {
    private String nro;
    private List<Ronda> rondas;

    public String getNro() {
        return this.nro;
    }

    public List<Ronda> getRondas() {
        return this.rondas;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public void setRondas(List<Ronda> rondas) {
        this.rondas = rondas;
    }

    public Fase() {
        this.nro = "";
        this.rondas = new ArrayList<>();
    }

    public Fase(String nro, List<Ronda> rondas) {
        this.nro = nro;
        this.rondas = rondas;
    }

    public int puntos(int puntoExtra, List<Pronostico> pronosticosRondasFace) {
        int aciertos = 0;
        for (Ronda ronda : this.rondas) {
            List<Pronostico> pronosticosRonda = new ArrayList<>();
            for (Pronostico pronostico : pronosticosRondasFace) {
                if (pronostico.getNroRonda().equals(ronda.getNro())) {
                    pronosticosRonda.add(pronostico);
                }
            }
            if (ronda.aciertoTotalRonda(pronosticosRonda)) {
                aciertos++;
            }
        }
        if (aciertos == this.rondas.size()) {
            return puntoExtra;
        }
        return 0;
    }
}
