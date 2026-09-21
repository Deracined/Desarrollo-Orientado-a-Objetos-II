package controlador;

import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador compartido por todas las ventanas.
 * Guarda la lista de pedidos y repartidores, y avisa a las ventanas
 * mediante listeners cuando los datos cambian para que sean refrescados.
 */
public class ControladorPedidos {

    private static final int DURACION_ENTREGA_MS = 6000; // simulación de 6 segundos

    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<Repartidor> repartidores = new ArrayList<>();
    private final List<Runnable> listeners = new ArrayList<>();

    public ControladorPedidos() {
        // Repartidores
        repartidores.add(new Repartidor("R1", "Jesús Avalos"));
        repartidores.add(new Repartidor("R2", "Cristina Guinolin"));
        repartidores.add(new Repartidor("R3", "Candice White Adley"));
    }

    // ---------- Listeners (para refrescar ventanas) ----------
    public synchronized void agregarListener(Runnable listener) {
        listeners.add(listener);
    }

    public synchronized void quitarListener(Runnable listener) {
        listeners.remove(listener);
    }

    private void notificarCambios() {
        List<Runnable> copia;
        synchronized (this) {
            copia = new ArrayList<>(listeners);
        }

        // Los cambios se hacen a traves del hilo swing (EDT)
        SwingUtilities.invokeLater(() -> copia.forEach(Runnable::run));
    }

    // ---------- Pedidos ---------- //
    public synchronized boolean existePedido(String id) {
        for (Pedido p : pedidos) {
            if (p.getId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    /** Crea y agrega un pedido. Lanza excepción si el ID ya existe. */
    public void registrarPedido(String id, String direccion, String tipo) {
        synchronized (this) {
            if (existePedido(id)) {
                throw new IllegalArgumentException("Ya existe un pedido con el ID " + id);
            }
            pedidos.add(new Pedido(id, direccion, tipo));
        }
        notificarCambios();
    }

    public synchronized List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    public synchronized List<Pedido> getPedidosPendientes() {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getEstado() == Pedido.Estado.PENDIENTE) resultado.add(p);
        }
        return resultado;
    }

    // ---------- Repartidores ---------- //
    public synchronized List<Repartidor> getRepartidoresDisponibles() {
        List<Repartidor> resultado = new ArrayList<>();
        for (Repartidor r : repartidores) {
            if (r.isDisponible()) resultado.add(r);
        }
        return resultado;
    }

    // ---------- Entregas (concurrentes) ----------
    /**
     * Asigna un repartidor al pedido e inicia la entrega en un hilo aparte,
     * de modo que la ventana no se congela y se pueden tener varias
     * entregas simultáneas.
     */
    public void iniciarEntrega(Pedido pedido, Repartidor repartidor) {
        synchronized (this) {
            if (pedido.getEstado() != Pedido.Estado.PENDIENTE) {
                throw new IllegalStateException("El pedido ya fue asignado.");
            }
            if (!repartidor.isDisponible()) {
                throw new IllegalStateException("El repartidor no está disponible.");
            }
            pedido.setRepartidor(repartidor);
            pedido.setEstado(Pedido.Estado.EN_CAMINO);
            repartidor.setDisponible(false);
        }
        notificarCambios();

        Thread hilo = new Thread(() -> {
            try {
                Thread.sleep(DURACION_ENTREGA_MS); // simula el viaje
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (ControladorPedidos.this) {
                pedido.setEstado(Pedido.Estado.ENTREGADO);
                repartidor.setDisponible(true);
            }
            notificarCambios();
        }, "Entrega-" + pedido.getId());
        hilo.start();
    }
}
