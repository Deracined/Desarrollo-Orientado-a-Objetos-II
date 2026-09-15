package cl.fastspeed.concurrencia;

import cl.fastspeed.model.Pedido;
import cl.fastspeed.model.EstadoPedido;
import cl.fastspeed.model.ZonaDeCarga;

public class Repartidor implements Runnable {

    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        System.out.println("===== " + nombre + " inicia su recorrido =====");

        Pedido pedido;
        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            boolean continuar = procesarPedido(pedido);
            if (!continuar) {
                break; // Detiene el recorrido ante una interrupción
            }
        }

        System.out.println("===== " + nombre + " finalizó su recorrido =====\n");
    }

    // Procesa un pedido retirado
    private boolean procesarPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.EN_REPARTO);
        System.out.println(nombre + " está entregando el pedido: " + pedido);

        try {
            long tiempoSimulado = 500 + (long) (Math.random() * 2500);
            Thread.sleep(tiempoSimulado);
        } catch (InterruptedException e) {
            System.out.println(nombre + ": la entrega fue interrumpida. Deteniendo el recorrido.");
            Thread.currentThread().interrupt();
            return false;
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        System.out.println(nombre + " entregó el pedido: " + pedido);

        return true;
    }
}