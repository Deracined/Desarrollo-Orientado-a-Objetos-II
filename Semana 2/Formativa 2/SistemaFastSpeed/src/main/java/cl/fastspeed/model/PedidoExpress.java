package cl.fastspeed.model;
/**
 * Clase que representa un pedido tipo express en el sistema, es una subclase de Pedido (Herencia)
 */
public class PedidoExpress extends Pedido {

    /// llamamos al constructor de Pedido
    public PedidoExpress(int idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    /**
     * Sobreescritura de los metodos asignarRepartidor y calcularTiempoEntrega para así poder indicar
     * información personalizada acorde a este tipo de pedido
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
        System.out.println("Repartidor con disponibilidad inmediata encontrado.");
    }

    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 10;
        if (getDistanciaKm() > 5) {
            tiempo += 5;
        }
        return tiempo;
    }
}