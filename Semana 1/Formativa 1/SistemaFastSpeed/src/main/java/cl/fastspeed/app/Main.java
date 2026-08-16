package cl.fastspeed.app;

import cl.fastspeed.model.*;

public class Main {

    public static void main(String[] args) {

        Pedido pedido1 = new PedidoComida(
                1,
                "Av. Perú 167"
        );

        Pedido pedido2 = new PedidoEncomienda(
                2,
                "El Molino 6484"
        );

        Pedido pedido3 = new PedidoExpress(
                3,
                "Camino de la Plegaria 6818"
        );
        /// Ejemplo de polimorfismo ya que el metodo asignarRepartidor produce comportamientos diferentes dependiendo
        /// objeto.
        System.out.println("===== PEDIDO COMIDA =====");
        pedido1.asignarRepartidor();
        pedido1.asignarRepartidor("Jesús Avalos");

        System.out.println();

        System.out.println("===== PEDIDO ENCOMIENDA =====");
        pedido2.asignarRepartidor();
        pedido2.asignarRepartidor("Candy White");

        System.out.println();

        System.out.println("===== PEDIDO EXPRESS =====");
        pedido3.asignarRepartidor();
        pedido3.asignarRepartidor("Johan Liebert");
    }
}