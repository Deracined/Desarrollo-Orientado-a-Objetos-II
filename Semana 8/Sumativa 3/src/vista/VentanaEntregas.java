package vista;

import controlador.Controlador;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pantalla CRUD de entregas: combos de Pedido y Repartidor (cargados desde la BD, mostrando "id - descripción" pero
 * guarda el id), más fecha, hora como texto validado y filtro opcional.
 */
public class VentanaEntregas extends JFrame {

    private final Controlador controlador;

    private JComboBox<Pedido> cmbPedido;
    private JComboBox<Repartidor> cmbRepartidor;
    private JTextField txtFecha; // formato yyyy-MM-dd
    private JTextField txtHora;  // formato HH:mm

    // Combos de filtro (independientes del formulario de registro/edición) //
    private JComboBox<Object> cmbFiltroPedido;
    private JComboBox<Object> cmbFiltroRepartidor;
    private static final String TODOS = "(Todos)";

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private Integer idSeleccionado = null;

    // Mapas id -> objeto, para mostrar texto legible en la tabla de entregas //
    private final Map<Integer, Pedido> pedidosPorId = new HashMap<>();
    private final Map<Integer, Repartidor> repartidoresPorId = new HashMap<>();

    // Última lista de entregas vista de la BD, para filtrar sin re-consultar //
    private List<Entrega> entregasCache;

    private final Runnable listenerRefresco = this::refrescarTodo;

    public VentanaEntregas(Controlador controlador) {
        this.controlador = controlador;
        initComponentes();
        refrescarTodo();

        controlador.agregarListener(listenerRefresco);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                controlador.quitarListener(listenerRefresco);
            }
        });
    }

    private void initComponentes() {
        setTitle("Gestión de entregas");
        setSize(700, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Formulario de registro/edición --- //
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        cmbPedido = new JComboBox<>();
        cmbRepartidor = new JComboBox<>();
        txtFecha = new JTextField(LocalDate.now().toString()); // ej: 2026-10-03
        txtHora = new JTextField("12:00");                      // ej: 14:30

        formulario.add(new JLabel("Pedido:"));
        formulario.add(cmbPedido);
        formulario.add(new JLabel("Repartidor:"));
        formulario.add(cmbRepartidor);
        formulario.add(new JLabel("Fecha (aaaa-mm-dd):"));
        formulario.add(txtFecha);
        formulario.add(new JLabel("Hora (hh:mm):"));
        formulario.add(txtHora);

        // --- Panel de filtros (opcional, no afecta a la BD, sino la vista) --- //
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));

        cmbFiltroPedido = new JComboBox<>();
        cmbFiltroRepartidor = new JComboBox<>();

        panelFiltros.add(new JLabel("Filtrar por pedido:"));
        panelFiltros.add(cmbFiltroPedido);
        panelFiltros.add(new JLabel("Filtrar por repartidor:"));
        panelFiltros.add(cmbFiltroRepartidor);

        cmbFiltroPedido.addActionListener(e -> aplicarFiltro());
        cmbFiltroRepartidor.addActionListener(e -> aplicarFiltro());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(formulario, BorderLayout.NORTH);
        panelSuperior.add(panelFiltros, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla --- //
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
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

    /**
     * Cuando se selecciona una fila, reconstruye el objeto Entrega y lo carga en el formulario.
     * */
    private void cargarSeleccion() {
        if (tabla.getSelectedRow() == -1) return;
        int fila = tabla.getSelectedRow();
        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);

        if (entregasCache == null) return;
        for (Entrega en : entregasCache) {
            if (en.getId() == idSeleccionado) {
                seleccionarEnCombo(cmbPedido, en.getIdPedido());
                seleccionarEnCombo(cmbRepartidor, en.getIdRepartidor());
                txtFecha.setText(en.getFecha().toString());
                txtHora.setText(en.getHora().toString().substring(0, 5)); // HH:mm
                break;
            }
        }
    }

    private void seleccionarEnCombo(JComboBox<?> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item instanceof Pedido && ((Pedido) item).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
            if (item instanceof Repartidor && ((Repartidor) item).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     *  Valida los combos y el formato de fecha-hora. Devulve null si algo falla (y ya avisó al usuario).
     *  */
    private Entrega construirEntregaDesdeFormulario() {
        Pedido pedido = (Pedido) cmbPedido.getSelectedItem();
        Repartidor repartidor = (Repartidor) cmbRepartidor.getSelectedItem();

        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes tener al menos un pedido y un repartidor registrados.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(txtFecha.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "La fecha debe tener el formato aaaa-mm-dd, por ejemplo: 2026-10-03",
                    "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            String textoHora = txtHora.getText().trim();

            if (!textoHora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
                throw new DateTimeParseException("Formato inválido", textoHora, 0);
            }

            hora = LocalTime.parse(textoHora);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "La hora debe tener el formato HH:mm, por ejemplo: 14:30",
                    "Hora inválida", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Entrega(pedido.getId(), repartidor.getId(), fecha, hora);
    }

    private void guardar() {
        Entrega entrega = construirEntregaDesdeFormulario();
        if (entrega == null) return;

        try {
            controlador.crearEntrega(entrega);
            JOptionPane.showMessageDialog(this, "Entrega registrada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la entrega:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una entrega de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Entrega entrega = construirEntregaDesdeFormulario();
        if (entrega == null) return;
        entrega.setId(idSeleccionado);

        try {
            controlador.actualizarEntrega(entrega);
            JOptionPane.showMessageDialog(this, "Entrega actualizada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar la entrega:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una entrega de la tabla primero.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this, "¿Eliminar esta entrega?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirma != JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarEntrega(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Entrega eliminada.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar la entrega:\n" + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        if (cmbPedido.getItemCount() > 0) cmbPedido.setSelectedIndex(0);
        if (cmbRepartidor.getItemCount() > 0) cmbRepartidor.setSelectedIndex(0);
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText("12:00");
        tabla.clearSelection();
    }

    /**
     *  Refresca los combos (pedidos/repartidores/filtros) y la tabla de entregas.
     *  */
    public void refrescarTodo() {
        try {
            // Recarga los combos del formulario conservando la selección si es posible
            Pedido pedidoSel = (Pedido) cmbPedido.getSelectedItem();
            Repartidor repSel = (Repartidor) cmbRepartidor.getSelectedItem();

            pedidosPorId.clear();
            cmbPedido.removeAllItems();
            for (Pedido p : controlador.listarPedidos()) {
                cmbPedido.addItem(p);
                pedidosPorId.put(p.getId(), p);
            }

            repartidoresPorId.clear();
            cmbRepartidor.removeAllItems();
            for (Repartidor r : controlador.listarRepartidores()) {
                cmbRepartidor.addItem(r);
                repartidoresPorId.put(r.getId(), r);
            }

            if (pedidoSel != null) seleccionarEnCombo(cmbPedido, pedidoSel.getId());
            if (repSel != null) seleccionarEnCombo(cmbRepartidor, repSel.getId());

            // Recarga los combos de FILTRO, conservando su selección si es posible
            Object filtroPedidoSel = cmbFiltroPedido.getSelectedItem();
            Object filtroRepartidorSel = cmbFiltroRepartidor.getSelectedItem();

            cmbFiltroPedido.removeAllItems();
            cmbFiltroPedido.addItem(TODOS);
            for (Pedido p : pedidosPorId.values()) cmbFiltroPedido.addItem(p);

            cmbFiltroRepartidor.removeAllItems();
            cmbFiltroRepartidor.addItem(TODOS);
            for (Repartidor r : repartidoresPorId.values()) cmbFiltroRepartidor.addItem(r);

            if (filtroPedidoSel != null) cmbFiltroPedido.setSelectedItem(filtroPedidoSel);
            if (filtroRepartidorSel != null) cmbFiltroRepartidor.setSelectedItem(filtroRepartidorSel);

            // Vuelve a leer las entregas desde la BD y reaplica el filtro //
            entregasCache = controlador.listarEntregas();
            aplicarFiltro();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los datos desde la base de datos:\n" + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filtra la lista ya cargada (entregasCache) según lo elegido en los
     * combos de filtro, y vuelve recargar la tabla.
     */
    private void aplicarFiltro() {
        if (entregasCache == null) return;

        Object filtroPedido = cmbFiltroPedido.getSelectedItem();
        Object filtroRepartidor = cmbFiltroRepartidor.getSelectedItem();

        Integer idPedidoFiltro = (filtroPedido instanceof Pedido) ? ((Pedido) filtroPedido).getId() : null;
        Integer idRepartidorFiltro = (filtroRepartidor instanceof Repartidor) ? ((Repartidor) filtroRepartidor).getId() : null;

        modeloTabla.setRowCount(0);
        for (Entrega en : entregasCache) {
            boolean pasaPedido = (idPedidoFiltro == null) || en.getIdPedido() == idPedidoFiltro;
            boolean pasaRepartidor = (idRepartidorFiltro == null) || en.getIdRepartidor() == idRepartidorFiltro;
            if (pasaPedido && pasaRepartidor) {
                Pedido p = pedidosPorId.get(en.getIdPedido());
                Repartidor r = repartidoresPorId.get(en.getIdRepartidor());
                modeloTabla.addRow(new Object[]{
                        en.getId(),
                        (p != null ? p.toString() : "ID " + en.getIdPedido()),
                        (r != null ? r.toString() : "ID " + en.getIdRepartidor()),
                        en.getFecha(),
                        en.getHora()
                });
            }
        }
    }
}
