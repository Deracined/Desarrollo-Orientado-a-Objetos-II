package cl.fastspeed.app;

import cl.fastspeed.model.*;

public class Main {

    public static void main(String[] args) {
    /// Declaramos los pedidos demostrando polimorfismo ya que cada uno cambia su comportamieto dependiendo de su subclase
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

        /// Mostramos en consola informacion de cada tipo de pedido junto a datos importantes por cada caso
        System.out.println("===== PEDIDO COMIDA =====");
        pedido1.asignarRepartidor();
        pedido1.asignarRepartidor("Jesús Avalos");
        pedido1.mostrarResumen();
        System.out.println("Tiempo estimado: "
                + pedido1.calcularTiempoEntrega() + " minutos");

        System.out.println();

        System.out.println("===== PEDIDO ENCOMIENDA =====");
        pedido2.asignarRepartidor();
        pedido2.asignarRepartidor("Candy White");
        pedido2.mostrarResumen();
        System.out.println("Tiempo estimado: "
                + pedido2.calcularTiempoEntrega() + " minutos");

        System.out.println();

        System.out.println("===== PEDIDO EXPRESS =====");
        pedido3.asignarRepartidor();
        pedido3.asignarRepartidor("Johan Liebert");
        pedido3.mostrarResumen();
        System.out.println("Tiempo estimado: "
                + pedido3.calcularTiempoEntrega() + " minutos");
    }
}