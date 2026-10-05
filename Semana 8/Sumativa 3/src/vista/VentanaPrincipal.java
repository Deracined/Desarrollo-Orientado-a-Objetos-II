package vista;

import controlador.Controlador;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal: menú de acceso a las pantallas de gestión.
 */
public class VentanaPrincipal extends JFrame {

    private final Controlador controlador;

    private VentanaRepartidores ventanaRepartidores;
    private VentanaPedidos ventanaPedidos;
    private VentanaEntregas ventanaEntregas;
    private VentanaClientes ventanaClientes;

    public VentanaPrincipal() {
        this.controlador = new Controlador();
        initComponentes();
        setVisible(true);
    }

    private void initComponentes() {
        setTitle("SpeedFast - Gestión de Pedidos y Entregas");
        setSize(420, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("SpeedFast", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(10, 60, 10, 60)
        );

        JButton btnRepartidores = new JButton("Gestionar repartidores");
        JButton btnPedidos = new JButton("Gestionar pedidos");
        JButton btnEntregas = new JButton("Gestionar entregas");
        JButton btnClientes = new JButton("Gestionar clientes");

        panelBotones.add(btnRepartidores);
        panelBotones.add(btnPedidos);
        panelBotones.add(btnEntregas);
        panelBotones.add(btnClientes);

        add(panelBotones, BorderLayout.CENTER);

        JLabel pie = new JLabel(
                "CRUD completo sobre MySQL (speedfast_db)",
                SwingConstants.CENTER
        );
        pie.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(pie, BorderLayout.SOUTH);

        btnRepartidores.addActionListener(e -> {
            if (ventanaRepartidores == null
                    || !ventanaRepartidores.isDisplayable()) {

                ventanaRepartidores =
                        new VentanaRepartidores(controlador);
            }

            ventanaRepartidores.setVisible(true);
            ventanaRepartidores.toFront();
        });

        btnPedidos.addActionListener(e -> {
            if (ventanaPedidos == null
                    || !ventanaPedidos.isDisplayable()) {

                ventanaPedidos =
                        new VentanaPedidos(controlador);
            }

            ventanaPedidos.setVisible(true);
            ventanaPedidos.toFront();
        });

        btnEntregas.addActionListener(e -> {
            if (ventanaEntregas == null
                    || !ventanaEntregas.isDisplayable()) {

                ventanaEntregas =
                        new VentanaEntregas(controlador);
            }

            ventanaEntregas.setVisible(true);
            ventanaEntregas.toFront();
        });

        btnClientes.addActionListener(e -> {
            if (ventanaClientes == null
                    || !ventanaClientes.isDisplayable()) {

                ventanaClientes =
                        new VentanaClientes(controlador);
            }

            ventanaClientes.setVisible(true);
            ventanaClientes.toFront();
        });
    }
}