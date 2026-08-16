package cl.fastspeed.model;
/// Herencia ya que PedidoComida es subclase de Pedido
public class PedidoComida extends Pedido {

    /// llamamos el constructor de Pedido
    public PedidoComida(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Comida");
    }

    /// ejemplo sobreescritura
    @Override
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
        System.out.println("Verificando mochila térmica... OK");
    }
}