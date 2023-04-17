public class Pronostico {
    private String nroFase;
    private String nroRonda;
    private Partido partido;
    private Equipo equipo;
    private ResultadoEmun resultado;

    public String getNroFase() {
        return this.nroFase;
    }

    public void setNroFase(String nroFase) {
        this.nroFase = nroFase;
    }

    public String getNroRonda() {
        return this.nroRonda;
    }

    public void setNroRonda(String nroRonda) {
        this.nroRonda = nroRonda;
    }

    public Equipo getEquipo() {
        return this.equipo;
    }

    public Partido getPartido() {
        return this.partido;
    }

    public ResultadoEmun getResultado() {
        return this.resultado;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public void setResultado(ResultadoEmun resultadoEmun) {
        this.resultado = resultadoEmun;
    }

    public Pronostico() {
    }

    public Pronostico(String nroFase, String nroRonda, Partido partido, Equipo equipo, ResultadoEmun resultadoEmun) {
        this.nroFase = nroFase;
        this.nroRonda = nroRonda;
        this.partido = partido;
        this.equipo = equipo;
        this.resultado = resultadoEmun;
    }

    public int puntos(Configuracion configuracion) {
        ResultadoEmun resultadoEmun = this.partido.resultado(this.equipo);
        //Acierta el resultado del partido y devuelve los ptos que le corresponde.
        if (resultadoEmun.getEstado() == this.resultado.getEstado()) {
//            return 1;
            if (this.resultado.getEstado() == ResultadoEmun.estados.GANADOR) {
                return configuracion.getPtoGanado();
            } else if (this.resultado.getEstado() == ResultadoEmun.estados.EMPATE) {
                return configuracion.getPtoEmpatado();
            } else {
                return configuracion.getPtoPerdido();
            }
        }
        return 0;
    }
}
