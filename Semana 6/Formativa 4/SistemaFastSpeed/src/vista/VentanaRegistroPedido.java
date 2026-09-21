package vista;

import controlador.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para registrar un nuevo pedido.
 */
public class VentanaRegistroPedido extends JFrame {

    private final ControladorPedidos controlador;

    private JTextField txtId;
    private JTextField txtDireccion;
    private JComboBox<String> cmbTipo;

    public VentanaRegistroPedido(ControladorPedidos controlador) {
        this.controlador = controlador;
        initComponentes();
    }

    private void initComponentes() {
        setTitle("Registrar pedido");
        setSize(400, 240);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Formulario con GridLayout (3 filas, 2 columnas)
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        txtId = new JTextField();
        txtDireccion = new JTextField();
        cmbTipo = new JComboBox<>(new String[]{"Comida", "Encomienda", "Express"});

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Dirección:"));
        formulario.add(txtDireccion);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(cmbTipo);
        add(formulario, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarPedido());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarPedido() {
        String id = txtId.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        // Validación de campos
        if (id.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes completar el ID y la dirección.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controlador.registrarPedido(id, direccion, tipo);
            JOptionPane.showMessageDialog(this,
                    "Pedido " + id + " registrado correctamente.",
                    "Pedido guardado", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        txtId.requestFocus();
    }
}
