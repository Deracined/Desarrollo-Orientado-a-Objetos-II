package cl.fastspeed.model;


/**
 * Clase que representa un pedido tipo comida en el sistema, es una subclase de Pedido (Herencia)
 */
public class PedidoComida extends Pedido {

    // llamamos el constructor de su clase padre Pedido
    public PedidoComida(int idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

}