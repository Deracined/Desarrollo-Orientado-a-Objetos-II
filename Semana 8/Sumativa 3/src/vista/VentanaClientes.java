package vista;

import controlador.Controlador;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Ventana para gestionar clientes mediante operaciones CRUD.
 */
public class VentanaClientes extends JFrame {

    private final Controlador controlador;

    private final JTextField txtNombre = new JTextField(20);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modeloTabla);

    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnNuevo = new JButton("Nuevo");

    public VentanaClientes(Controlador controlador) {
        this.controlador = controlador;

        setTitle("SpeedFast - Clientes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construirInterfaz();
        registrarEventos();

        controlador.agregarListener(this::refrescarTabla);

        refrescarTabla();
    }

    /**
     * Construye la interfaz gráfica.
     */
    private void construirInterfaz() {

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        formulario.add(txtNombre, gbc);

        JPanel botones = new JPanel(new FlowLayout());

        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);

        JScrollPane scroll = new JScrollPane(tabla);

        setLayout(new BorderLayout(10, 10));

        add(formulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    /**
     * Registra los eventos de los botones y de la tabla.
     */
    private void registrarEventos() {

        btnNuevo.addActionListener(e -> limpiarFormulario());

        btnGuardar.addActionListener(e -> guardar());

        btnActualizar.addActionListener(e -> actualizar());

        btnEliminar.addActionListener(e -> eliminar());

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    /**
     * Valida los datos antes de ejecutar una operación CRUD.
     */
    private boolean validarFormulario() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre es obligatorio.",
                    "Campo incompleto",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        if (nombre.length() > 100) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede superar los 100 caracteres.",
                    "Nombre demasiado largo",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        return true;
    }

    /**
     * Inserta un nuevo cliente.
     */
    private void guardar() {

        if (!validarFormulario()) {
            return;
        }

        try {

            Cliente cliente = new Cliente(
                    txtNombre.getText().trim()
            );

            controlador.crearCliente(cliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente.",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el cliente.\n" + ex.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza el cliente seleccionado.
     */
    private void actualizar() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un cliente para actualizar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {

            int id = (int) modeloTabla.getValueAt(fila, 0);

            Cliente cliente = new Cliente(
                    id,
                    txtNombre.getText().trim()
            );

            controlador.actualizarCliente(cliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente actualizado correctamente.",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el cliente.\n" + ex.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina el cliente seleccionado.
     */
    private void eliminar() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un cliente para eliminar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar el cliente seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int id = (int) modeloTabla.getValueAt(fila, 0);

            controlador.eliminarCliente(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente eliminado correctamente.",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el cliente.\n" + ex.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carga los datos del cliente seleccionado en el formulario.
     */
    private void cargarSeleccion() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtNombre.setText(
                String.valueOf(modeloTabla.getValueAt(fila, 1))
        );
    }

    /**
     * Actualiza la tabla consultando nuevamente la base de datos.
     */
    private void refrescarTabla() {

        try {

            List<Cliente> clientes = controlador.listarClientes();

            modeloTabla.setRowCount(0);

            for (Cliente cliente : clientes) {
                modeloTabla.addRow(new Object[]{
                        cliente.getId(),
                        cliente.getNombre()
                });
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron cargar los clientes.\n" + ex.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Limpia el formulario y la selección.
     */
    private void limpiarFormulario() {
        txtNombre.setText("");
        tabla.clearSelection();
        txtNombre.requestFocus();
    }
}