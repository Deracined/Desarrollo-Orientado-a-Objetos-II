package modelo;

/**
 * Representa a un repartidor de SpeedFast. id = 0 significa "todavía no existe en la base de datos".
 */
public class Repartidor {
    private int id;
    private String nombre;

    public Repartidor(String nombre) {
        this(0, nombre);
    }

    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
