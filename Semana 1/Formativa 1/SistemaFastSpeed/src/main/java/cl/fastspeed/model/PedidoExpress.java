package cl.fastspeed.model;
/// Sub-clase proveniente de la clase Pedido (Herencia)
public class PedidoExpress extends Pedido {

    /// llamamos al constructor de Pedido
    public PedidoExpress(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Express");
    }
    @Override
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
        System.out.println("Repartidor con disponibilidad inmediata encontrado.");
    }
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}