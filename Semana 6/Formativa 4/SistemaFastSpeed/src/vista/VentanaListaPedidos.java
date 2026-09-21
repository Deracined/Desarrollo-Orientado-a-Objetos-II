package vista;

import controlador.ControladorPedidos;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Muestra todos los pedidos en una JTable.
 * Se refresca automáticamente cuando el controlador avisa de cambios
 * y también con el botón "Refrescar".
 */
public class VentanaListaPedidos extends JFrame {

    private final ControladorPedidos controlador;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private final Runnable listenerRefresco = this::refrescarTabla;

    public VentanaListaPedidos(ControladorPedidos controlador) {
        this.controlador = controlador;
        initComponentes();
        refrescarTabla();

        // Se suscribe a cambios y se desuscribe al cerrar
        controlador.agregarListener(listenerRefresco);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                controlador.quitarListener(listenerRefresco);
            }
        });
    }

    private void initComponentes() {
        setTitle("Listado de pedidos");
        setSize(650, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado", "Repartidor"};
        // Modelo no editable: el usuario no puede modificar las celdas a mano
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnRefrescar.addActionListener(e -> refrescarTabla());
        btnCerrar.addActionListener(e -> dispose());
    }

    /** Vuelve a cargar la tabla con los datos actuales del controlador. */
    public void refrescarTabla() {
        modeloTabla.setRowCount(0); // limpia la tabla
        for (Pedido p : controlador.getPedidos()) {
            String repartidor = (p.getRepartidor() == null) ? "-" : p.getRepartidor().getNombre();
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getDireccion(), p.getTipo(), p.getEstado(), repartidor
            });
        }
    }
}
