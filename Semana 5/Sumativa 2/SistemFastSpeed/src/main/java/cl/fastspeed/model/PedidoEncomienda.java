package cl.fastspeed.model;

/**
 * Subclase proveniente de pedido, demuestra herencia y expresa una encomienda (tipo de pedido diferente)
 */
public class PedidoEncomienda extends Pedido {

    // llamamos el constructor de Pedido
    public PedidoEncomienda(int idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

}
