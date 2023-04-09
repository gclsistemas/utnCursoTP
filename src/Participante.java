import java.util.ArrayList;
import java.util.List;

public class Participante {
    private String nombre;
    private List<Pronostico> pronosticos;
//    private List<Integer> aciertosRonda;
//    private List<Integer> aciertosFace;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pronostico> getPronosticos() {
        return this.pronosticos;
    }

    public void setPronosticos(List<Pronostico> pronosticos) {
        this.pronosticos = pronosticos;
    }

//    public List<Integer> getAciertosRonda() {
//        return this.aciertosRonda;
//    }
//
//    public void setAciertosRonda(List<Integer> aciertosRonda) {
//        this.aciertosRonda = aciertosRonda;
//    }

//    public List<Integer> getAciertosFace() {
//        return this.aciertosFace;
//    }
//
//    public void setAciertosFace(List<Integer> aciertosFace) {
//        this.aciertosFace = aciertosFace;
//    }

    public Participante() {
        this.nombre = "";
        this.pronosticos = new ArrayList<>();
    }

    public Participante(String nombre) {
        this.nombre = nombre;
        this.pronosticos = new ArrayList<>();
    }

}
