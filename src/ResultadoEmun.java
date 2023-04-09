public class ResultadoEmun {
    //ganador, perdedor, empate
    public enum estados {GANADOR, PERDEDOR, EMPATE}

    private estados estado;

    public estados getEstado() {
        return this.estado;
    }

    public void setEstado(estados estado) {
        this.estado = estado;
    }

    public ResultadoEmun() {
    }

    public ResultadoEmun(estados estado) {
        this.estado = estado;
    }
}
