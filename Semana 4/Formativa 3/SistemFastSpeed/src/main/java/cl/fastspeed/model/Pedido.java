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

    // Creación del constructor
    public Pedido(int idPedido, String direccionEntrega, int distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        registrarEvento("Pedido creado");
    }

    // Definimos el metodo asignar repartidor
    public void asignarRepartidor() {
        System.out.println("Asignando un repartidor.....");
        registrarEvento("Repartidor asignado");
    }

    // Añadimos el metodo abstracto para calcular el tiempo de entrega de los pedidos
    public abstract int calcularTiempoEntrega();

    // metodo getter que retorna la distancia en kilometros del pedido
    public int getDistanciaKm() {
        return distanciaKm;
    }

    // Añadimos el metodo mostrar resumen para direccion, indicar el ID, y distancia del pedido.
    public void mostrarResumen() {
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("ID Pedido: " + idPedido);
        System.out.println("Distancia: " + distanciaKm + " km");
    }

    // Ejemplo de sobrecarga (Overloading) ya que tenemos 2 metodos que se llaman igual, reutilizamos la //
    // funcionabilidad haciendo que reciba distintos tipos da dato desde la misma clase. //
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }

    protected void registrarEvento(String evento) {
        historial.add(evento);
    }

    @Override
    public void despachar() {
        registrarEvento("Pedido despachado");
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
    }
}