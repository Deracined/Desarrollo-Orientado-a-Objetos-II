package vista;

import controlador.ControladorPedidos;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Permite elegir un pedido pendiente y un repartidor disponible
 * para iniciar (simular) la entrega.
 */
public class VentanaAsignarEntrega extends JFrame {

    private final ControladorPedidos controlador;
    private JComboBox<Pedido> cmbPedidos;
    private JComboBox<Repartidor> cmbRepartidores;
    private final Runnable listenerRefresco = this::cargarCombos;

    public VentanaAsignarEntrega(ControladorPedidos controlador) {
        this.controlador = controlador;
        initComponentes();
        cargarCombos();

        controlador.agregarListener(listenerRefresco);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                controlador.quitarListener(listenerRefresco);
            }
        });
    }

    private void initComponentes() {
        setTitle("Asignar repartidor / Iniciar entrega");
        setSize(480, 230);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        cmbPedidos = new JComboBox<>();
        cmbRepartidores = new JComboBox<>();

        formulario.add(new JLabel("Pedido pendiente:"));
        formulario.add(cmbPedidos);
        formulario.add(new JLabel("Repartidor disponible:"));
        formulario.add(cmbRepartidores);
        add(formulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnIniciar = new JButton("Iniciar entrega");
        JButton btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnIniciar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnIniciar.addActionListener(e -> iniciarEntrega());
        btnCerrar.addActionListener(e -> dispose());
    }

    /** Recarga los combos con los pedidos pendientes y repartidores libres. */
    private void cargarCombos() {
        Pedido pedidoSel = (Pedido) cmbPedidos.getSelectedItem();
        Repartidor repSel = (Repartidor) cmbRepartidores.getSelectedItem();

        cmbPedidos.removeAllItems();
        for (Pedido p : controlador.getPedidosPendientes()) cmbPedidos.addItem(p);

        cmbRepartidores.removeAllItems();
        for (Repartidor r : controlador.getRepartidoresDisponibles()) cmbRepartidores.addItem(r);

        // Intenta mantener la selección anterior //
        if (pedidoSel != null) cmbPedidos.setSelectedItem(pedidoSel);
        if (repSel != null) cmbRepartidores.setSelectedItem(repSel);
    }

    private void iniciarEntrega() {
        Pedido pedido = (Pedido) cmbPedidos.getSelectedItem();
        Repartidor repartidor = (Repartidor) cmbRepartidores.getSelectedItem();

        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes.",
                    "Sin pedidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidor == null) {
            JOptionPane.showMessageDialog(this, "No hay repartidores disponibles por ahora.",
                    "Sin repartidores", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controlador.iniciarEntrega(pedido, repartidor);
            JOptionPane.showMessageDialog(this,
                    "Entrega del pedido " + pedido.getId() + " iniciada con " + repartidor.getNombre() + ".",
                    "Entrega iniciada", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
