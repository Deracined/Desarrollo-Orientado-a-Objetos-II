package cl.fastspeed.model;
/**
 * Clase abstracta que representa un pedido en el sistema
 * Define atributos y metodos comunes de todos los tipos de pedido,
 * dejando el cálculo del tiempo de entrega a cada subclase.
 */
public abstract class Pedido {
    /// Encapsulamiento de atributos
    private int idPedido;
    private String direccionEntrega;
    private int distanciaKm;

    /// Creación del constructor
    public Pedido(int idPedido, String direccionEntrega, int distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
    }
    /// Definimos el metodo asignar repartidor
    public void asignarRepartidor() {
        System.out.println("Asignando un repartidor.....");
    }

    /// Añadimos el metodo abstracto para calcular el tiempo de entrega de los pedidos
    public abstract int calcularTiempoEntrega();

    /// metodo getter que retorna la distancia en kilometros del pedido
    public int getDistanciaKm() {
        return distanciaKm;
    }

    /// Añadimos el metodo mostrar resumen para direccion, indicar el ID, y distancia del pedido.
    public void mostrarResumen() {
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("ID Pedido: " + idPedido);
        System.out.println("Distancia: " + distanciaKm + " km");
    }
    /// Ejemplo de sobrecarga (Overloading) ya que tenemos 2 metodos que se llaman igual, reutilizamos la
    /// funcionabilidad haciendo que reciba distintos tipos da dato desde la misma clase.
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}