package Vista;

import controlador.ControladorReserva;
import modelo.Cliente;
import modelo.Reserva;
import utils.UtilCSV;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class PanelClientes extends JPanel {
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnImportar;  // será "Refrescar"

    private final File archivoClientes = new File("data/clientes.csv");

    private List<Cliente> listaClientesCompleta;

    private ControladorReserva controladorReserva = ControladorReserva.getInstancia();

    public PanelClientes() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Apellido", "Cédula", "Teléfono", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaClientes);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnImportar = new JButton("Refrescar Clientes");

        panelBusqueda.add(new JLabel("Buscar por nombre, apellido o cédula/email:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnImportar);

        add(panelBusqueda, BorderLayout.NORTH);

        btnImportar.addActionListener(_e -> {
            if (archivoClientes.exists()) {
                listaClientesCompleta = UtilCSV.cargarClientesDesdeCSV(archivoClientes);
                mostrarClientes(listaClientesCompleta);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El archivo clientes.csv no existe en la carpeta data.",
                        "Archivo no encontrado",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBuscar.addActionListener(_e -> {
            String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
            if (textoBusqueda.isEmpty()) {
                mostrarClientes(listaClientesCompleta);
            } else {
                List<Cliente> filtrados = listaClientesCompleta.stream()
                        .filter(c -> c.getNombre().toLowerCase().contains(textoBusqueda)
                                || c.getApellido().toLowerCase().contains(textoBusqueda)
                                || c.getCedula().toLowerCase().contains(textoBusqueda)
                                || c.getEmail().toLowerCase().contains(textoBusqueda))
                        .collect(Collectors.toList());
                mostrarClientes(filtrados);
            }
        });

        cargarDesdeArchivoCSV();

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarMenuContextual(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarMenuContextual(e);
            }
        });
    }

    private void mostrarMenuContextual(MouseEvent e) {
        if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
            int fila = tablaClientes.rowAtPoint(e.getPoint());
            if (fila >= 0 && fila < tablaClientes.getRowCount()) {
                tablaClientes.setRowSelectionInterval(fila, fila);
                String cedula = ((String) modeloTabla.getValueAt(fila, 2)).trim();
                Cliente clienteSeleccionado = listaClientesCompleta.stream()
                        .filter(c -> c.getCedula().trim().equalsIgnoreCase(cedula))
                        .findFirst()
                        .orElse(null);

                if (clienteSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró cliente.");
                    return;
                }

                JPopupMenu menu = new JPopupMenu();

                JMenuItem itemVerReservas = new JMenuItem("Ver Reservas");
                itemVerReservas.addActionListener(_ev -> mostrarReservasCliente(clienteSeleccionado));
                menu.add(itemVerReservas);

                JMenuItem itemEliminarReserva = new JMenuItem("Eliminar Reserva");
                itemEliminarReserva.addActionListener(_ev -> eliminarReservaCliente(clienteSeleccionado));
                menu.add(itemEliminarReserva);

                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private void mostrarReservasCliente(Cliente cliente) {
        System.out.println("Buscando reservas para cédula: '" + cliente.getCedula() + "'");
        List<Reserva> reservas = controladorReserva.buscarReservasPorCliente(cliente);
        System.out.println("Reservas encontradas: " + reservas.size());
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este cliente no tiene reservas.");
            return;
        }

        StringBuilder sb = new StringBuilder("Reservas de " + cliente.getNombre() + " " + cliente.getApellido() + ":\n\n");
        double totalPagar = 0.0;
        for (Reserva r : reservas) {
            sb.append("Habitación: ").append(r.getHabitacion().getNombre())
              .append("\nFecha inicio: ").append(r.getFechaInicio())
              .append("\nFecha fin: ").append(r.getFechaFin())
              .append("\nPrecio total: $").append(String.format("%.2f", r.getPrecioTotal()))
              .append("\n\n");
            totalPagar += r.getPrecioTotal();
        }
        sb.append("Precio total a pagar por todas las reservas: $").append(String.format("%.2f", totalPagar));

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void eliminarReservaCliente(Cliente cliente) {
        List<Reserva> reservas = controladorReserva.buscarReservasPorCliente(cliente);
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este cliente no tiene reservas para eliminar.");
            return;
        }

        Reserva reserva = (Reserva) JOptionPane.showInputDialog(this,
                "Seleccione reserva a eliminar:",
                "Eliminar Reserva",
                JOptionPane.PLAIN_MESSAGE,
                null,
                reservas.toArray(),
                reservas.get(0));

        if (reserva != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea eliminar la reserva para " + reserva.getHabitacion().getNombre() + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = controladorReserva.eliminarReserva(reserva);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Reserva eliminada correctamente.");

                    // Actualizar la lista de clientes y refrescar tabla
                    listaClientesCompleta = controladorReserva.getListaClientes();
                    mostrarClientes(listaClientesCompleta);

                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la reserva.");
                }
            }
        }
    }

    public void cargarDesdeArchivoCSV() {
        if (archivoClientes.exists()) {
            listaClientesCompleta = UtilCSV.cargarClientesDesdeCSV(archivoClientes);
            mostrarClientes(listaClientesCompleta);
        }
    }

    private void mostrarClientes(List<Cliente> clientes) {
        limpiarTabla();
        for (Cliente c : clientes) {
            agregarCliente(c.getNombre(), c.getApellido(), c.getCedula(), c.getTelefono(), c.getEmail());
        }
    }

    public void agregarCliente(String nombre, String apellido, String cedula, String telefono, String correo) {
        modeloTabla.addRow(new Object[]{nombre, apellido, cedula, telefono, correo});
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }
}
