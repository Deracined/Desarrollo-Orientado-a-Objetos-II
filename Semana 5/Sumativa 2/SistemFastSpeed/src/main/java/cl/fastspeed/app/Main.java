package cl.fastspeed.app;

import cl.fastspeed.model.*;
import cl.fastspeed.concurrencia.Repartidor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        zonaDeCarga.agregarPedido(new PedidoComida(1, "Av. Perú 168", 5));
        zonaDeCarga.agregarPedido(new PedidoEncomienda(2, "El Molino 6484", 8));
        zonaDeCarga.agregarPedido(new PedidoExpress(3, "Camino de la Plegaria 6818", 6));
        zonaDeCarga.agregarPedido(new PedidoEncomienda(4, "Los Álamos 123", 3));
        zonaDeCarga.agregarPedido(new PedidoExpress(5, "Vicuña Mackenna 456", 10));
        zonaDeCarga.agregarPedido(new PedidoComida(6, "Irarrázaval 789", 4));

        Repartidor repartidor1 = new Repartidor("Jesús Avalos", zonaDeCarga);
        Repartidor repartidor2 = new Repartidor("Johan Liebert", zonaDeCarga);
        Repartidor repartidor3 = new Repartidor("Candy White", zonaDeCarga);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(repartidor1);
        executor.execute(repartidor2);
        executor.execute(repartidor3);

        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("La operación fue interrumpida.");
            Thread.currentThread().interrupt();
        }

        System.out.println("Todos los pedidos se han completado");
    }
}