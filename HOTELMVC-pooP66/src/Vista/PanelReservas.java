package Vista;

import controlador.ControladorCliente;
import controlador.ControladorReserva;
import modelo.Cliente;
import modelo.Habitacion;
import modelo.Reserva;
import modelo.ServicioAdicional;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PanelReservas extends JPanel {

    private JTextField txtNombre, txtApellido, txtCedula, txtTelefono, txtEmail;
    private JTextField txtFechaInicio, txtFechaFin;
    private JComboBox<Habitacion> comboHabitaciones;
    private JPanel panelServicios;  // Panel para servicios adicionales
    private JTextField txtTotal;
    private JButton btnConfirmar;

    private ControladorReserva controladorReserva = ControladorReserva.getInstancia();
    private ControladorCliente controladorCliente = ControladorCliente.getInstancia();

    // Lista de JCheckBox para servicios adicionales
    private List<JCheckBox> checkBoxesServicios = new ArrayList<>();
    // Lista de servicios disponibles (puedes modificar esta lista)
    private List<ServicioAdicional> listaServicios = List.of(
        new ServicioAdicional("Desayuno", 10.0),
        new ServicioAdicional("Transporte", 15.0),
        new ServicioAdicional("Spa", 25.0)
    );

    public PanelReservas() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int y = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        add(txtNombre, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        add(txtApellido, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(20);
        add(txtCedula, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        add(txtTelefono, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Fecha inicio (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaInicio = new JTextField(20);
        add(txtFechaInicio, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Fecha fin (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaFin = new JTextField(20);
        add(txtFechaFin, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Habitación:"), gbc);
        gbc.gridx = 1;
        comboHabitaciones = new JComboBox<>();
        add(comboHabitaciones, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panelServicios = new JPanel();
        panelServicios.setBorder(BorderFactory.createTitledBorder("Servicios Adicionales"));
        panelServicios.setLayout(new BoxLayout(panelServicios, BoxLayout.Y_AXIS));
        add(panelServicios, gbc);
        gbc.gridwidth = 1;

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Total a pagar:"), gbc);
        gbc.gridx = 1;
        txtTotal = new JTextField(20);
        txtTotal.setEditable(false);
        txtTotal.setForeground(Color.BLUE);
        add(txtTotal, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        btnConfirmar = new JButton("Confirmar reserva");
        gbc.gridwidth = 2;
        add(btnConfirmar, gbc);
        gbc.gridwidth = 1;

        cargarHabitacionesEnCombo();
        cargarServicios();

        // Listeners para actualizar total cuando cambian fechas, habitación o servicios
        comboHabitaciones.addActionListener(e -> actualizarTotal());
        txtFechaInicio.getDocument().addDocumentListener(new SimpleDocumentListener(this::actualizarTotal));
        txtFechaFin.getDocument().addDocumentListener(new SimpleDocumentListener(this::actualizarTotal));

        btnConfirmar.addActionListener(e -> confirmarReserva());
    }

    private void cargarHabitacionesEnCombo() {
        comboHabitaciones.removeAllItems();
        for (Habitacion h : controladorReserva.getListaHabitaciones()) {
            if (h.getCantidadDisponible() > 0) {
                comboHabitaciones.addItem(h);
            }
        }
        if (comboHabitaciones.getItemCount() > 0) {
            comboHabitaciones.setSelectedIndex(0);
        }
    }

    private void cargarServicios() {
        panelServicios.removeAll();
        checkBoxesServicios.clear();
        for (ServicioAdicional servicio : listaServicios) {
            JCheckBox checkBox = new JCheckBox(servicio.getDescripcion() + " ($" + servicio.getPrecio() + ")");
            checkBox.addActionListener(e -> actualizarTotal());
            panelServicios.add(checkBox);
            checkBoxesServicios.add(checkBox);
        }
        panelServicios.revalidate();
        panelServicios.repaint();
    }

    private void actualizarTotal() {
        Habitacion habitacion = (Habitacion) comboHabitaciones.getSelectedItem();
        if (habitacion == null) {
            txtTotal.setText("");
            return;
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;
        try {
            fechaInicio = LocalDate.parse(txtFechaInicio.getText().trim());
            fechaFin = LocalDate.parse(txtFechaFin.getText().trim());
            if (!fechaFin.isAfter(fechaInicio)) {
                txtTotal.setText("Fecha fin debe ser posterior");
                return;
            }
        } catch (Exception e) {
            txtTotal.setText("");
            return;
        }

        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        if (dias <= 0) dias = 1;

        double totalHabitacion = habitacion.getPrecio() * dias;

        double totalServicios = 0;
        for (int i = 0; i < checkBoxesServicios.size(); i++) {
            if (checkBoxesServicios.get(i).isSelected()) {
                totalServicios += listaServicios.get(i).getPrecio();
            }
        }

        double total = totalHabitacion + totalServicios;
        txtTotal.setText(String.format("%.2f", total));
    }

    private void confirmarReserva() {
        try {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String cedula = txtCedula.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Complete todos los datos del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaInicio;
            LocalDate fechaFin;
            try {
                fechaInicio = LocalDate.parse(txtFechaInicio.getText().trim());
                fechaFin = LocalDate.parse(txtFechaFin.getText().trim());
            } catch (DateTimeParseException dtpe) {
                JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de fecha", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!fechaFin.isAfter(fechaInicio)) {
                JOptionPane.showMessageDialog(null, "La fecha fin debe ser posterior a la fecha inicio.", "Error de fecha", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Habitacion habitacion = (Habitacion) comboHabitaciones.getSelectedItem();

            if (habitacion == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una habitación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente cliente = new Cliente(nombre, apellido, cedula, telefono, email);

            boolean clienteOk = controladorCliente.agregarCliente(cliente);
            System.out.println("¿Cliente guardado? " + clienteOk);

            if (!clienteOk) {
                JOptionPane.showMessageDialog(null, "Error con los datos del cliente (campos incompletos o cédula/email duplicados).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Reserva reserva = new Reserva(generateIdReserva(), cliente, habitacion, fechaInicio, fechaFin);

            // Agregar servicios seleccionados
            for (int i = 0; i < checkBoxesServicios.size(); i++) {
                if (checkBoxesServicios.get(i).isSelected()) {
                    reserva.agregarServicioAdicional(listaServicios.get(i));
                }
            }

            boolean ok = controladorReserva.agregarReserva(reserva);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Reserva realizada con éxito.\nTotal a pagar: $" + txtTotal.getText());
                limpiarCampos();
                cargarHabitacionesEnCombo();
                actualizarTotal();
            } else {
                JOptionPane.showMessageDialog(null, "No hay disponibilidad para esa habitación.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        if (comboHabitaciones.getItemCount() > 0) {
            comboHabitaciones.setSelectedIndex(0);
        }
        for (JCheckBox checkBox : checkBoxesServicios) {
            checkBox.setSelected(false);
        }
        txtTotal.setText("");
    }

    private String generateIdReserva() {
        return "RES-" + System.currentTimeMillis();
    }

    // Clase interna NO abstracta para escuchar cambios en Document y disparar Runnable
    private static class SimpleDocumentListener implements javax.swing.event.DocumentListener {
        private final Runnable onChange;
        public SimpleDocumentListener(Runnable onChange) {
            this.onChange = onChange;
        }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { onChange.run(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { onChange.run(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { onChange.run(); }
    }
}
