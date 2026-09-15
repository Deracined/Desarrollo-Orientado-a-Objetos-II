package cl.fastspeed.model;

/**
 * Clase que representa un pedido tipo express en el sistema, es una subclase de Pedido (Herencia)
 */
public class PedidoExpress extends Pedido {

    // llamamos al constructor de Pedido
    public PedidoExpress(int idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    /**
     * Sobreescritura para así poder indicar
     * información personalizada acorde a este tipo de pedido
     */

    @Override
    public void despachar() {
        registrarEvento("Pedido express despachado con prioridad.");
        System.out.println("Despacho prioritario Express.");
    }
}
