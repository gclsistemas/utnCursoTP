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
    public int puntos () {
        return 1;
    }
}
