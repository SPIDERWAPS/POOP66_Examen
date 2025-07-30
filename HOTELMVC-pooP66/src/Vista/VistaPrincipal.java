package Vista;

import controlador.ControladorReserva;
import controlador.ControladorCliente;
import modelo.Habitacion;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaPrincipal extends JFrame {

    private JTable tablaHabitaciones;
    private DefaultTableModel modeloTablaHabitaciones;
    private ControladorReserva controladorReserva;
    private ControladorCliente controladorCliente;
    private Usuario usuarioLogueado;

    private PanelClientes panelClientes;
    private JTabbedPane tabbedPane;

    public VistaPrincipal(Usuario usuario) {
        super("Sistema de Reservas de Hotel");
        this.usuarioLogueado = usuario;

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        controladorReserva = ControladorReserva.getInstancia();
        controladorReserva.setVista(this);

        controladorCliente = ControladorCliente.getInstancia();

        JLabel bienvenida = new JLabel("Bienvenido: " + usuario.getEmail() + " (" + usuario.getRol() + ")");
        bienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);

        tabbedPane = new JTabbedPane();

        // Panel habitaciones
        JPanel panelHabitaciones = new JPanel(new BorderLayout());
        modeloTablaHabitaciones = new DefaultTableModel(new Object[]{"Tipo", "Disponibles", "Precio", "Acción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 3) {
                    Object disponiblesObj = getValueAt(row, 1);
                    if (disponiblesObj instanceof Integer) {
                        return ((Integer) disponiblesObj) > 0;
                    }
                }
                return false;
            }
        };
        tablaHabitaciones = new JTable(modeloTablaHabitaciones);
        tablaHabitaciones.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaHabitaciones.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox(), this));
        panelHabitaciones.add(new JScrollPane(tablaHabitaciones), BorderLayout.CENTER);

        // Panel clientes
        panelClientes = new PanelClientes();

        tabbedPane.addTab("Habitaciones", panelHabitaciones);
        tabbedPane.addTab("Clientes", panelClientes);

        // Listener para cambiar pestaña y recargar clientes automáticamente
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() == panelClientes) {
                    controladorCliente.recargarClientes();
                    cargarClientesEnPanel();
                }
            }
        });

        setLayout(new BorderLayout());
        add(bienvenida, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        actualizarTablaHabitaciones();
    }

    public void actualizarTablaHabitaciones() {
        modeloTablaHabitaciones.setRowCount(0);
        List<Habitacion> habitaciones = controladorReserva.getListaHabitaciones();
        for (Habitacion h : habitaciones) {
            modeloTablaHabitaciones.addRow(new Object[]{
                    h.getTipo(),
                    h.getCantidadDisponible(),
                    String.format("$%.2f", h.getPrecio()),
                    h.getCantidadDisponible() > 0 ? "Reservar" : "No disponible"
            });
        }
    }

    public void cargarClientesEnPanel() {
        var clientes = controladorCliente.getListaClientes();
        panelClientes.limpiarTabla();
        for (var c : clientes) {
            panelClientes.agregarCliente(
                c.getNombre(),
                c.getApellido(),
                c.getCedula(),
                c.getTelefono(),
                c.getEmail()
            );
        }
    }

    public void actualizarTablaClientes() {
        cargarClientesEnPanel();
    }

    public void actualizarTablaReservas() {
        // Implementa si tienes tabla de reservas para recargar
        // Ejemplo: panelReservas.recargarReservas(controladorReserva.getListaReservas());
    }

    public void reservar(String tipo) {
        controladorReserva.reservarHabitacion(tipo, this);
    }
}
