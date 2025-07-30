package Vista;

import modelo.Reserva;
import modelo.ServicioAdicional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FormularioServiciosAdicionales extends JDialog {

    private JTextField descripcionField;
    private JTextField precioField;
    private JButton btnAgregar;
    private JButton btnCerrar;

    private Reserva reserva;

    public FormularioServiciosAdicionales(Frame parent, Reserva reserva) {
        super(parent, "Agregar Servicio Adicional", true);
        this.reserva = reserva;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Descripción
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Descripción:"), gbc);
        descripcionField = new JTextField(20);
        gbc.gridx = 1;
        add(descripcionField, gbc);

        // Precio
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Precio:"), gbc);
        precioField = new JTextField(10);
        gbc.gridx = 1;
        add(precioField, gbc);

        // Botones
        btnAgregar = new JButton("Agregar Servicio");
        btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCerrar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(panelBotones, gbc);

        btnAgregar.addActionListener(this::agregarServicio);
        btnCerrar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private void agregarServicio(ActionEvent e) {
        String descripcion = descripcionField.getText().trim();
        String precioStr = precioField.getText().trim();

        if (descripcion.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un precio válido (número positivo).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ServicioAdicional servicio = new ServicioAdicional(descripcion, precio);
        reserva.agregarServicioAdicional(servicio);

        JOptionPane.showMessageDialog(this, "Servicio agregado. Total actual: $" + String.format("%.2f", reserva.calcularTotal()));

        // Limpiar campos para agregar otro servicio
        descripcionField.setText("");
        precioField.setText("");
        descripcionField.requestFocus();
    }
}
