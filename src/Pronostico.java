public class Pronostico {
    private Partido partido;
    private Equipo equipo;
    private ResultadoEmun resultado;

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
    public Pronostico(Partido partido, Equipo equipo, ResultadoEmun resultadoEmun) {
        this.partido = partido;
        this.equipo = equipo;
        this.resultado = resultadoEmun;
    }
    public int puntos() {
        ResultadoEmun resultadoEmun = this.partido.resultado(this.equipo);
        if (resultadoEmun.getEstado() == this.resultado.getEstado()) {
            return 1;
        }
        return 0;
    }
}
