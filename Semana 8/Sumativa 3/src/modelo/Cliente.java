package modelo;

/**
 * Representa un cliente de SpeedFast.
 */
public class Cliente {

    private int id;
    private String nombre;

    /**
     * Constructor para crear un cliente nuevo.
     */
    public Cliente(String nombre) {
        this(0, nombre);
    }

    /**
     * Constructor para cargar un cliente existente.
     */
    public Cliente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}