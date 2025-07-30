package Vista;

import controlador.ControladorCliente;
import controlador.ControladorReserva;
import modelo.Cliente;
import modelo.Habitacion;
import modelo.Reserva;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class FormularioDatosReserva extends JDialog {

    private JTextField nombreField;
    private JTextField cedulaField;
    private JFormattedTextField fechaInicioField;
    private JTextField numeroPersonasField;
    private JTextField diasField;
    private JTextField correoField;
    private JTextField telefonoField;
    private JTextArea observacionesArea;
    private JTextField precioTotalField;  // NUEVO

    private boolean reservaConfirmada = false;
    private Habitacion habitacion;
    private Reserva reserva;

    public FormularioDatosReserva(Frame parent, Habitacion habitacion) {
        super(parent, "Formulario de Reserva", true);
        this.habitacion = habitacion;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Tipo de habitación (solo etiqueta)
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel("Tipo de Habitación:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(habitacion.getTipo().toString()), gbc);

        // Nombre Completo
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nombreField = new JTextField(20);
        panel.add(nombreField, gbc);

        // Cédula
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cedulaField = new JTextField(20);
        panel.add(cedulaField, gbc);

        // Fecha Inicio con máscara
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Fecha Inicio (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            fechaInicioField = new JFormattedTextField(mask);
            fechaInicioField.setColumns(20);
        } catch (ParseException ex) {
            fechaInicioField = new JFormattedTextField();
            fechaInicioField.setColumns(20);
        }
        panel.add(fechaInicioField, gbc);

        // Número de personas
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Número de personas:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        numeroPersonasField = new JTextField(20);
        panel.add(numeroPersonasField, gbc);

        // Días de estadía
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Días de estadía:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        diasField = new JTextField(20);
        panel.add(diasField, gbc);

        // Precio total (nuevo campo, no editable)
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Precio Total:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        precioTotalField = new JTextField(20);
        precioTotalField.setEditable(false);
        precioTotalField.setForeground(Color.BLUE);
        panel.add(precioTotalField, gbc);

        // Correo
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        correoField = new JTextField(20);
        panel.add(correoField, gbc);

        // Teléfono
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        telefonoField = new JTextField(20);
        panel.add(telefonoField, gbc);

        // Observaciones
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        observacionesArea = new JTextArea(4, 20);
        panel.add(new JScrollPane(observacionesArea), gbc);

        add(panel, BorderLayout.CENTER);

        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnConfirmar = new JButton("Confirmar Reserva");
        JButton btnCancelar = new JButton("Cancelar");
        botonPanel.add(btnConfirmar);
        botonPanel.add(btnCancelar);
        add(botonPanel, BorderLayout.SOUTH);

        // Listener para actualizar precio total cuando cambian los días
        diasField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { actualizarPrecioTotal(); }
            @Override
            public void removeUpdate(DocumentEvent e) { actualizarPrecioTotal(); }
            @Override
            public void changedUpdate(DocumentEvent e) { actualizarPrecioTotal(); }
        });

        btnConfirmar.addActionListener(_ -> guardarReserva());
        btnCancelar.addActionListener(_ -> dispose());
    }

    private void actualizarPrecioTotal() {
        String diasStr = diasField.getText().trim();
        if (diasStr.isEmpty()) {
            precioTotalField.setText("");
            return;
        }
        try {
            int dias = Integer.parseInt(diasStr);
            if (dias <= 0) {
                precioTotalField.setText("Días inválidos");
                return;
            }
            double precioNoche = habitacion.getPrecio();
            double total = precioNoche * dias;
            precioTotalField.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            precioTotalField.setText("Número inválido");
        }
    }

    private void guardarReserva() {
        String nombre = nombreField.getText().trim();
        String cedula = cedulaField.getText().trim();
        String fechaInicioStr = fechaInicioField.getText().trim();
        String numeroPersonasStr = numeroPersonasField.getText().trim();
        String diasStr = diasField.getText().trim();
        String correo = correoField.getText().trim();
        String telefono = telefonoField.getText().trim();
        @SuppressWarnings("unused")
        String observaciones = observacionesArea.getText().trim();

        if (nombre.isEmpty() || cedula.isEmpty() || fechaInicioStr.isEmpty() ||
            numeroPersonasStr.isEmpty() || diasStr.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos obligatorios.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarFecha(fechaInicioStr)) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha incorrecto. Use dd/MM/yyyy",
                "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que la fecha de inicio no sea pasada
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date inicioDate = sdf.parse(fechaInicioStr);
            Date hoy = new Date();
            // Compara solo fecha (sin hora)
            if (inicioDate.before(truncarFecha(hoy))) {
                JOptionPane.showMessageDialog(this,
                    "La fecha de inicio no puede ser anterior a hoy.",
                    "Fecha inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Error al validar la fecha de inicio.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!telefono.isEmpty() && !telefono.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                "El teléfono solo debe contener números.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                "Formato de correo electrónico inválido.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numPersonas;
        try {
            numPersonas = Integer.parseInt(numeroPersonasStr);
            if (numPersonas <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El número de personas debe ser un entero positivo.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int dias;
        try {
            dias = Integer.parseInt(diasStr);
            if (dias <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Los días de estadía deben ser un entero positivo.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date inicioDate = sdf.parse(fechaInicioStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(inicioDate);
            cal.add(Calendar.DAY_OF_MONTH, dias);
            Date finDate = cal.getTime();

            LocalDate fechaInicio = inicioDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaFin = finDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Validar que no existan reservas superpuestas para la misma habitación
            if (ControladorReserva.getInstancia().existeReservaSuperpuesta(habitacion, fechaInicio, fechaFin)) {
                JOptionPane.showMessageDialog(this,
                        "La habitación ya está reservada en las fechas seleccionadas.",
                        "Reserva inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] partes = nombre.split(" ", 2);
            String nombreCliente = partes[0];
            String apellidoCliente = partes.length > 1 ? partes[1] : "";

            Cliente clienteNuevo = new Cliente(nombreCliente, apellidoCliente, cedula, telefono, correo);

            Cliente clienteExistente = ControladorCliente.getInstancia().buscarPorCedula(cedula);

            Cliente clienteParaUsar = clienteExistente != null ? clienteExistente : clienteNuevo;

            reserva = new Reserva(clienteParaUsar, habitacion, fechaInicio, fechaFin);

            reservaConfirmada = true;
            JOptionPane.showMessageDialog(this,
                "Reserva confirmada desde " + fechaInicioStr + " hasta " + sdf.format(finDate) + ".\nPrecio total: $" + precioTotalField.getText(),
                "Reserva Confirmada", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Error al calcular fecha final.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFecha(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private Date truncarFecha(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public boolean isReservaConfirmada() {
        return reservaConfirmada;
    }

    public Reserva getReserva() {
        return reserva;
    }
}
