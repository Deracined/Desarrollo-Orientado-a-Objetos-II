package cl.fastspeed.model;

import cl.fastspeed.interfaces.Despachable;
import cl.fastspeed.interfaces.Cancelable;
import cl.fastspeed.interfaces.Rastreable;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa un pedido en el sistema
 * Define atributos y metodos comunes de todos los tipos de pedido,
 * dejando el cálculo del tiempo de entrega a cada subclase.
 * Además implementa las 3 interfaces creadas (Despachable,Cancelable y Rastreable)
 */
public abstract class Pedido implements Despachable, Cancelable, Rastreable {
    // Encapsulamiento de atributos
    private final int idPedido;
    private final String direccionEntrega;
    private final int distanciaKm;
    private final List<String> historial = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    // Creación del constructor
    public Pedido(int idPedido, String direccionEntrega, int distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        registrarEvento("Pedido creado");
    }


    // metodo getter que retorna la distancia en kilometros del pedido
    public int getDistanciaKm() {
        return distanciaKm;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
        registrarEvento("Estado cambiado a " + nuevoEstado);
    }


    protected void registrarEvento(String evento) {
        historial.add(evento);
    }

    @Override
    public void despachar() {
        setEstado(EstadoPedido.EN_REPARTO);
        System.out.println("Pedido despachado.");
    }

    @Override
    public void cancelar() {
        registrarEvento("Pedido cancelado");
        System.out.println("Pedido cancelado.");
    }

    @Override
    public void verHistorial() {
        System.out.println("Historial del pedido:");
        for (String evento : historial) {
            System.out.println("- " + evento);
        }
        System.out.println("Estado actual: " + estado);
    }
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", distanciaKm=" + distanciaKm +
                ", estado=" + estado +
                '}';
    }
}