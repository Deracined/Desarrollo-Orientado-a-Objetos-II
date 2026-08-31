package cl.fastspeed.model;


/**
 * Clase que representa un pedido tipo comida en el sistema, es una subclase de Pedido (Herencia)
 */
public class PedidoComida extends Pedido {

    // llamamos el constructor de su clase padre Pedido
    public PedidoComida(int idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // Ejemplos de sobreescritura
    @Override
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
        System.out.println("Verificando mochila térmica... OK");
        registrarEvento("Repartidor asignado (Comida)");
    }

    @Override
    public int calcularTiempoEntrega() {
        return 15 + (2 * getDistanciaKm());
    }

}