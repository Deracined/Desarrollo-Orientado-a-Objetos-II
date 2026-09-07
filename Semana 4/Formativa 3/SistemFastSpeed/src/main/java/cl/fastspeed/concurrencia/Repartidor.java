package cl.fastspeed.concurrencia;

import cl.fastspeed.model.Pedido;
import cl.fastspeed.gestores.ControladorDeEnvios;
import java.util.List;
import java.util.Random;

public class Repartidor implements Runnable {

    private final String nombre;
    private final List<Pedido> pedidosAsignados;
    private final ControladorDeEnvios controlador;
    private final Random random = new Random();

    public Repartidor(String nombre, List<Pedido> pedidosAsignados, ControladorDeEnvios controlador) {
        this.nombre = nombre;
        this.pedidosAsignados = pedidosAsignados;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        System.out.println("===== " + nombre + " inicia su recorrido =====");

        for (Pedido pedido : pedidosAsignados) {
            pedido.asignarRepartidor();
            pedido.asignarRepartidor(nombre);
            pedido.mostrarResumen();
            System.out.println("Tiempo estimado: " + pedido.calcularTiempoEntrega() + " minutos");

            try {
                long tiempoSimulado = 500 + random.nextInt(2500);
                Thread.sleep(tiempoSimulado);
            } catch (InterruptedException e) {
                System.out.println(nombre + ": la entrega fue interrumpida inesperadamente.");
                Thread.currentThread().interrupt();
            }

            // Simula que no todos los pedidos llegan a destino (20% de probabilidad de cancelación)
            boolean seCancela = random.nextInt(100) < 20;

            if (seCancela) {
                controlador.cancelar(pedido);
            } else {
                controlador.despachar(pedido);
            }

            controlador.mostrarHistorial(pedido);
            System.out.println(nombre + " completó el procesamiento del pedido.\n");
        }

        System.out.println("===== " + nombre + " finalizó todas sus entregas =====\n");
    }
}