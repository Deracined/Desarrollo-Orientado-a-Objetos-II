package cl.fastspeed.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Representa el recurso compartido de SpeedFast: la zona de carga donde
 * llegan los pedidos y desde donde los repartidores los retiran para
 * iniciar su entrega. Utiliza BlockingQueue, que ya garantiza acceso
 * seguro entre múltiples hilos sin necesidad de sincronización manual.
 */
public class ZonaDeCarga {

    private final BlockingQueue<Pedido> pedidosPendientes = new LinkedBlockingQueue<>();

    // Metodos agregarPedido y retirarPedido con Syncronized ya que la actividad lo pide explícitamente, aunque
    // no debería ser necesario por la previa implementación de BlockingQueue, pero por si acaso por como lo indica el
    // docx.
    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.add(pedido);
        System.out.println(pedido + " ingresado a la zona de carga.");
    }

    /**
     * Retira un pedido de forma segura. Si no hay pedidos disponibles,
     * devuelve null de inmediato (no bloquea esperando), para que cada
     * repartidor pueda terminar su recorrido quenda la zona se vacía.
     */
    public synchronized Pedido retirarPedido() {
        return pedidosPendientes.poll();
    }
}