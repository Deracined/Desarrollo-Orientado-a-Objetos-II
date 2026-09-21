package modelo;

/**
 * Representa la clase repartidor del sistema Fast Speed.
 */
public class Repartidor {
    private String id;
    private String nombre;
    private boolean disponible;

    public Repartidor(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.disponible = true;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return nombre + " (" + id + ")";
    }
}
