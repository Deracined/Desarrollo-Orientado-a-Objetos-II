package cl.fastspeed.model;

public class Pedido {
    /// Encapsulamiento de datos(atributos)
    private int idPedido;
    private String direccionEntrega;
    private String tipoPedido;

    /// Creación del constructor
    public Pedido(int idPedido, String direccionEntrega, String tipoPedido) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
    }
    /// Definimos el metodo principal(comportamiento del objeto)
    public void asignarRepartidor() {
        System.out.println("Asignando un repartidor.....");
    }
    /// Ejemplo de sobrecarga (Overloading) ya que tenemos 2 metodos que se llaman igual, reutilizamos la
    /// funcionabilidad haciendo que reciba distintos tipos da dato desde la misma clase.
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}