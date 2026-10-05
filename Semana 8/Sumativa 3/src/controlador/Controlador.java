package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;
import dao.ClienteDAO;
import modelo.Cliente;
import java.sql.SQLException;
import java.util.List;
import javax.swing.SwingUtilities;
import java.util.ArrayList;

/**
 * Controlador único que centraliza el acceso a los DAO y avisa a las ventanas
 * abiertas (mediante listeners) cuando los datos cambian, para que las JTable y los JComboBox se mantengan sincronizados
 * con la base de datos sin que cada ventana tenga que preocuparse de refrescar a las demás.
 */
public class Controlador {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    private final List<Runnable> listeners = new ArrayList<>();

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
        SwingUtilities.invokeLater(() -> copia.forEach(Runnable::run));
    }

    // ---------- Repartidores ---------- //

    public void crearRepartidor(Repartidor r) throws SQLException {
        repartidorDAO.create(r);
        notificarCambios();
    }

    public List<Repartidor> listarRepartidores() throws SQLException {
        return repartidorDAO.readAll();
    }

    public void actualizarRepartidor(Repartidor r) throws SQLException {
        repartidorDAO.update(r);
        notificarCambios();
    }

    public void eliminarRepartidor(int id) throws SQLException {
        repartidorDAO.delete(id);
        notificarCambios();
    }

    // ---------- Pedidos ---------- //

    public void crearPedido(Pedido p) throws SQLException {
        pedidoDAO.create(p);
        notificarCambios();
    }

    public List<Pedido> listarPedidos() throws SQLException {
        return pedidoDAO.readAll();
    }

    public void actualizarPedido(Pedido p) throws SQLException {
        pedidoDAO.update(p);
        notificarCambios();
    }

    public void eliminarPedido(int id) throws SQLException {
        pedidoDAO.delete(id);
        notificarCambios();
    }

    // ---------- Entregas ---------- //

    public void crearEntrega(Entrega e) throws SQLException {
        entregaDAO.create(e);
        notificarCambios();
    }

    public List<Entrega> listarEntregas() throws SQLException {
        return entregaDAO.readAll();
    }

    public void actualizarEntrega(Entrega e) throws SQLException {
        entregaDAO.update(e);
        notificarCambios();
    }

    public void eliminarEntrega(int id) throws SQLException {
        entregaDAO.delete(id);
        notificarCambios();
    }

    // ---------- Clientes ---------- //

    public void crearCliente(Cliente cliente) throws SQLException {
        clienteDAO.create(cliente);
        notificarCambios();
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.readAll();
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        clienteDAO.update(cliente);
        notificarCambios();
    }

    public void eliminarCliente(int id) throws SQLException {
        clienteDAO.delete(id);
        notificarCambios();
    }



}
