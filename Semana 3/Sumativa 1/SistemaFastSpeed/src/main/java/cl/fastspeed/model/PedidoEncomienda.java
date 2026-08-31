package cl.fastspeed.model;

/**
 * Subclase proveniente de pedido, demuestra herencia y expresa una encomienda (tipo de pedido diferente)
 */
public class PedidoEncomienda extends Pedido {

    // llamamos el constructor de Pedido
    public PedidoEncomienda(int idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // Sobreescritura de los metodos
    @Override
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
        System.out.println("Validando peso y embalaje... OK");
        registrarEvento("Repartidor asignado (Encomienda)");
    }

    @Override
    public int calcularTiempoEntrega() {
        double tiempoExacto = 20 + (1.5 * getDistanciaKm());
        return (int) (tiempoExacto + 0.5);
    }
}
