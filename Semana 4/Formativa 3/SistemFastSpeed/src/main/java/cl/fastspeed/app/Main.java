package cl.fastspeed.app;

import cl.fastspeed.model.*;
import cl.fastspeed.gestores.ControladorDeEnvios;
import cl.fastspeed.concurrencia.Repartidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) {
        // Declaramos los pedidos demostrando polimorfismo ya que cada uno cambia su comportamieto dependiendo de su subclase
        Pedido pedido1 = new PedidoComida(
                1,
                "Av. Perú 168",
                5
        );

        Pedido pedido2 = new PedidoEncomienda(
                2,
                "El Molino 6484",
                8
        );

        Pedido pedido3 = new PedidoExpress(
                3,
                "Camino de la Plegaria 6818",
                6
        );

        Pedido pedido4 = new PedidoEncomienda(
                4,
                "Camino de la Plegaria 6818",
                3
        );

        Pedido pedido5 = new PedidoExpress(
                5,
                "Camino de la Plegaria 6818",
                10
        );

        Pedido pedido6 = new PedidoComida(
                6,
                "Camino de la Plegaria 6818",
                4
        );

        List<Pedido> pedidosJesus = new ArrayList<>();
        pedidosJesus.add(pedido1);
        pedidosJesus.add(pedido4);

        List<Pedido> pedidosJohan = new ArrayList<>();
        pedidosJohan.add(pedido2);
        pedidosJohan.add(pedido5);

        List<Pedido> pedidosCandy = new ArrayList<>();
        pedidosCandy.add(pedido3);
        pedidosCandy.add(pedido6);

        // Declaramos el Controlador de envios
        ControladorDeEnvios controlador = new ControladorDeEnvios();

        Repartidor repartidor1 = new Repartidor("Jesús Avalos", pedidosJesus, controlador);
        Repartidor repartidor2 = new Repartidor("Johan Liebert", pedidosJohan, controlador);
        Repartidor repartidor3 = new Repartidor("Candy White", pedidosCandy, controlador);

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

        System.out.println("Todas las entregas han finalizado.");
    }
}