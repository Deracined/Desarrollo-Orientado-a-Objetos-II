package vista;

import controlador.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal: menú con los tres botones del sistema.
 */
public class VentanaPrincipal extends JFrame {

    private final ControladorPedidos controlador;

    // Se guardan para no abrir la misma ventana varias veces
    private VentanaRegistroPedido ventanaRegistro;
    private VentanaListaPedidos ventanaLista;
    private VentanaAsignarEntrega ventanaAsignar;

    public VentanaPrincipal() {
        this.controlador = new ControladorPedidos();
        initComponentes();
        setVisible(true);
    }

    private void initComponentes() {
        setTitle("FastSpeed - Gestión de Entregas");
        setSize(420, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Título (arriba)
        JLabel titulo = new JLabel("FastSpeed", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(titulo, BorderLayout.NORTH);

        // Botones (centro) con GridLayout
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        JButton btnRegistrar = new JButton("Registrar pedido");
        JButton btnListar = new JButton("Listar pedidos");
        JButton btnAsignar = new JButton("Asignar repartidor / Iniciar entrega");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnAsignar);
        add(panelBotones, BorderLayout.CENTER);

        // Pie de página
        JLabel pie = new JLabel("Sistema de gestión de pedidos", SwingConstants.CENTER);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(pie, BorderLayout.SOUTH);

        // Acciones
        btnRegistrar.addActionListener(e -> {
            if (ventanaRegistro == null || !ventanaRegistro.isDisplayable()) {
                ventanaRegistro = new VentanaRegistroPedido(controlador);
            }
            ventanaRegistro.setVisible(true);
            ventanaRegistro.toFront();
        });

        btnListar.addActionListener(e -> {
            if (ventanaLista == null || !ventanaLista.isDisplayable()) {
                ventanaLista = new VentanaListaPedidos(controlador);
            }
            ventanaLista.setVisible(true);
            ventanaLista.toFront();
        });

        btnAsignar.addActionListener(e -> {
            if (ventanaAsignar == null || !ventanaAsignar.isDisplayable()) {
                ventanaAsignar = new VentanaAsignarEntrega(controlador);
            }
            ventanaAsignar.setVisible(true);
            ventanaAsignar.toFront();
        });
    }
}
