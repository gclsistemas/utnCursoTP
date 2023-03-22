public class Equipo {
    private String nombre;
    private String descripcion;

    public String getNombre() {
        return this.nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Equipo() {
    }

    public Equipo (String nombre) {
        this.nombre = nombre;
    }
}
