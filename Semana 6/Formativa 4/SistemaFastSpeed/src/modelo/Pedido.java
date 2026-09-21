package modelo;

/**
 * Representa la clase pedido del sistema FastSpeed.
 *
 */
public class Pedido {

    public enum Estado { PENDIENTE, EN_CAMINO, ENTREGADO }

    private String id;
    private String direccion;
    private String tipo;          // comida, encomienda, express
    private Estado estado;
    private Repartidor repartidor; // null mientras no se asigne

    public Pedido(String id, String direccion, String tipo) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = Estado.PENDIENTE;
    }

    public String getId() { return id; }
    public String getDireccion() { return direccion; }
    public String getTipo() { return tipo; }
    public Estado getEstado() { return estado; }
    public Repartidor getRepartidor() { return repartidor; }

    public void setEstado(Estado estado) { this.estado = estado; }
    public void setRepartidor(Repartidor repartidor) { this.repartidor = repartidor; }

    @Override
    public String toString() {
        return id + " - " + direccion + " [" + tipo + "]";
    }
}
