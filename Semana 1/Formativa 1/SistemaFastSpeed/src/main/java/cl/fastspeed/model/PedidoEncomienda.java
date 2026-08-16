package cl.fastspeed.model;
/// Sub-clase proveniente de la clase Pedido (Herencia)
public class PedidoEncomienda extends Pedido {

    /// llamamos el constructor de Pedido
    public PedidoEncomienda(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Encomienda");
    }
    @Override
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
        System.out.println("Validando peso y embalaje... OK");
    }
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}