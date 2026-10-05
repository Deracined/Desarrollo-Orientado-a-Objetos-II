package vista;

import controlador.Controlador;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Pantalla CRUD de repartidores: formulario arriba, tabla abajo. Seleccionar una fila carga esos datos en el formulario
 * para editar o eliminar; "Nuevo" limpia el formulario para registrar otro.
 */
public class VentanaRepartidores extends JFrame {

    private final Controlador controlador;
    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private Integer idSeleccionado = null; // null = modo "nuevo registro"

    private final Runnable listenerRefresco = this::refrescarTabla;

    public VentanaRepartidores(Controlador controlador) {
        this.controlador = controlador;
        initComponentes();
        refrescarTabla();

        controlador.agregarListener(listenerRefresco);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                controlador.quitarListener(listenerRefresco);
            }
        });
    }

    private void initComponentes() {
        setTitle("Gestión de repartidores");
        setSize(480, 420);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(1, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        txtNombre = new JTextField();
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        add(formulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int fila = tabla.getSelectedRow();
                idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnNuevo = new JButton("Nuevo");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnNuevo);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnNuevo.addActionListener(e -> limpiarFormulario());
    }

    /**
     *  Valida que el nombre no esté vacío, devuelve true si sale ok
     *  */
    private boolean validarFormulario() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.",
                    "Campo incompleto", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (nombre.length() > 100) {
            JOptionPane.showMessageDialog(this,
                    "El nombre no puede superar los 100 caracteres.",
                    "Nombre demasiado largo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void guardar() {
        if (!validarFormulario()) return;
        try {
            controlador.crearRepartidor(new Repartidor(txtNombre.getText().trim()));
            JOptionPane.showMessageDialog(this, "Repartidor registrado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el repartidor:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un repartidor de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarFormulario()) return;
        try {
            controlador.actualizarRepartidor(new Repartidor(idSeleccionado, txtNombre.getText().trim()));
            JOptionPane.showMessageDialog(this, "Repartidor actualizado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el repartidor:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un repartidor de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este repartidor?\n(Fallará si tiene entregas asociadas, por la llave foránea.)",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirma != JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarRepartidor(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Repartidor eliminado.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el repartidor (probablemente tiene entregas asociadas):\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtNombre.setText("");
        tabla.clearSelection();
    }

    /** Vuelve a consultar la base de datos y recarga la tabla. */
    public void refrescarTabla() {
        try {
            modeloTabla.setRowCount(0);
            for (Repartidor r : controlador.listarRepartidores()) {
                modeloTabla.addRow(new Object[]{r.getId(), r.getNombre()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de repartidores:\n" + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
