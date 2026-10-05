package modelo;

/**
 * Representa un pedido de SpeedFast. id = 0 indica que todavía no existe en la base de datos
 * (lo asignamos con MySQL automáticamente con AUTO_INCREMENT al crearlo).
 */
public class Pedido {

    public enum Tipo { COMIDA, ENCOMIENDA, EXPRESS }
    public enum Estado { PENDIENTE, EN_REPARTO, ENTREGADO }

    private int id;
    private String direccion;
    private Tipo tipo;
    private Estado estado;

    /** Constructor para un pedido nuevo (aún sin ID). */
    public Pedido(String direccion, Tipo tipo, Estado estado) {
        this(0, direccion, tipo, estado);
    }

    /** Constructor para un pedido ya existente en la base de datos. */
    public Pedido(int id, String direccion, Tipo tipo, Estado estado) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    @Override
    public String toString() {
        return id + " - " + direccion;
    }
}
