package vista;

import controlador.Controlador;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Pantalla CRUD de pedidos: dirección + tipo (JComboBox) + estado (JComboBox), con tabla de todos los
 * pedidos registrados y filtro en los pedidos.
 */
public class VentanaPedidos extends JFrame {

    private final Controlador controlador;
    private JTextField txtDireccion;
    private JComboBox<Pedido.Tipo> cmbTipo;
    private JComboBox<Pedido.Estado> cmbEstado;

    // Combos de filtro (independientes del formulario de registro/edición) //
    private JComboBox<String> cmbFiltroTipo;
    private JComboBox<String> cmbFiltroEstado;
    private static final String TODOS = "(Todos)";

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private Integer idSeleccionado = null;


    // Guarda la última lista leída de la BD, para poder filtrarla sin tener que volver a consultar MySQL cada vez
    // que cambia un filtro.
    private List<Pedido> pedidosCache;

    private final Runnable listenerRefresco = this::refrescarTabla;

    public VentanaPedidos(Controlador controlador) {
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
        setTitle("Gestión de pedidos");
        setSize(640, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Formulario de registro/edición --- //
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtDireccion = new JTextField();
        cmbTipo = new JComboBox<>(Pedido.Tipo.values());
        cmbEstado = new JComboBox<>(Pedido.Estado.values());

        formulario.add(new JLabel("Dirección:"));
        formulario.add(txtDireccion);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(cmbTipo);
        formulario.add(new JLabel("Estado:"));
        formulario.add(cmbEstado);

        // --- Panel de filtros (opcional, no afecta a la Base de datos, solo la vista) --- //
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));

        cmbFiltroTipo = new JComboBox<>();
        cmbFiltroTipo.addItem(TODOS);
        for (Pedido.Tipo t : Pedido.Tipo.values()) cmbFiltroTipo.addItem(t.name());

        cmbFiltroEstado = new JComboBox<>();
        cmbFiltroEstado.addItem(TODOS);
        for (Pedido.Estado es : Pedido.Estado.values()) cmbFiltroEstado.addItem(es.name());

        panelFiltros.add(new JLabel("Filtrar por tipo:"));
        panelFiltros.add(cmbFiltroTipo);
        panelFiltros.add(new JLabel("Filtrar por estado:"));
        panelFiltros.add(cmbFiltroEstado);

        cmbFiltroTipo.addActionListener(e -> aplicarFiltro());
        cmbFiltroEstado.addActionListener(e -> aplicarFiltro());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(formulario, BorderLayout.NORTH);
        panelSuperior.add(panelFiltros, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla --- //
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Dirección", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int fila = tabla.getSelectedRow();
                idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
                txtDireccion.setText((String) modeloTabla.getValueAt(fila, 1));
                cmbTipo.setSelectedItem(Pedido.Tipo.valueOf((String) modeloTabla.getValueAt(fila, 2)));
                cmbEstado.setSelectedItem(Pedido.Estado.valueOf((String) modeloTabla.getValueAt(fila, 3)));
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- Botones --- //
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

    private boolean validarFormulario() {
        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.",
                    "Campo incompleto", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (direccion.length() > 100) {
            JOptionPane.showMessageDialog(this,
                    "La dirección no puede superar los 100 caracteres.",
                    "Dirección demasiado larga", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cmbTipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un tipo de pedido.",
                    "Campo incompleto", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cmbEstado.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un estado.",
                    "Campo incompleto", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void guardar() {
        if (!validarFormulario()) return;
        try {
            Pedido pedido = new Pedido(
                    txtDireccion.getText().trim(),
                    (Pedido.Tipo) cmbTipo.getSelectedItem(),
                    (Pedido.Estado) cmbEstado.getSelectedItem()
            );
            controlador.crearPedido(pedido);
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el pedido:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarFormulario()) return;
        try {
            Pedido pedido = new Pedido(
                    idSeleccionado,
                    txtDireccion.getText().trim(),
                    (Pedido.Tipo) cmbTipo.getSelectedItem(),
                    (Pedido.Estado) cmbEstado.getSelectedItem()
            );
            controlador.actualizarPedido(pedido);
            JOptionPane.showMessageDialog(this, "Pedido actualizado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el pedido:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este pedido?\n(Fallará si tiene entregas asociadas, por la llave foránea.)",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirma != JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarPedido(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Pedido eliminado.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el pedido (probablemente tiene entregas asociadas):\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    /** Vuelve a consultar la base de datos y reaplica el filtro activo. */
    public void refrescarTabla() {
        try {
            pedidosCache = controlador.listarPedidos();
            aplicarFiltro();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la lista de pedidos:\n" + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra la lista ya cargada (pedidosCache) según lo elegido en los combos de filtro. No necesita volver a
     * consultar la base de datos: filtra en memoria la última lectura.
     */
    private void aplicarFiltro() {
        if (pedidosCache == null) return;

        String filtroTipo = (String) cmbFiltroTipo.getSelectedItem();
        String filtroEstado = (String) cmbFiltroEstado.getSelectedItem();

        modeloTabla.setRowCount(0);
        for (Pedido p : pedidosCache) {
            boolean pasaTipo = TODOS.equals(filtroTipo) || p.getTipo().name().equals(filtroTipo);
            boolean pasaEstado = TODOS.equals(filtroEstado) || p.getEstado().name().equals(filtroEstado);
            if (pasaTipo && pasaEstado) {
                modeloTabla.addRow(new Object[]{
                        p.getId(), p.getDireccion(), p.getTipo().name(), p.getEstado().name()
                });
            }
        }
    }
}