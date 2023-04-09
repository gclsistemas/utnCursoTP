public class Configuracion {
    private int ptoGanado = 3;
    private int ptoEmpatado = 1;
    private int ptoPerdido = 0;
    private int ptoExtra = 5;

    public int getPtoGanado() {
        return this.ptoGanado;
    }

    public int getPtoEmpatado() {
        return this.ptoEmpatado;
    }

    public int getPtoPerdido() {
        return this.ptoPerdido;
    }

    public int getPtoExtra() {
        return this.ptoExtra;
    }

    public Configuracion() {
    }

    /**
     *
     * @param ptoGanado
     * @param ptoEmpatado
     * @param ptoPerdido
     * @param ptoExtra
     */
    public Configuracion(int ptoGanado, int ptoEmpatado, int ptoPerdido, int ptoExtra) {
        this.ptoGanado = ptoGanado;
        this.ptoEmpatado = ptoEmpatado;
        this.ptoPerdido = ptoPerdido;
        this.ptoExtra = ptoExtra;
    }
}
