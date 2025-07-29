package vista;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class FormularioReserva extends JFrame {

    private JTextField txtNombre, txtCedula, txtFecha;
    private String habitacion;
    private VistaPrincipal vista;

    public FormularioReserva(String habitacion, VistaPrincipal vista) {
        this.habitacion = habitacion;
        this.vista = vista;

        setTitle("Formulario de Reserva - " + habitacion);
        setSize(300, 200);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Cédula:"));
        txtCedula = new JTextField();
        add(txtCedula);

        add(new JLabel("Fecha:"));
        txtFecha = new JTextField();
        add(txtFecha);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarReserva());
        add(btnGuardar);

        setLocationRelativeTo(null);
    }

    private void guardarReserva() {
        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String fecha = txtFecha.getText();

        if (nombre.isEmpty() || cedula.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/reservas.csv", true))) {
            bw.write(nombre + "," + cedula + "," + habitacion + "," + fecha);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar reserva.");
            return;
        }

        actualizarDisponibilidad();
        if (vista != null) vista.cargarHabitaciones();

        JOptionPane.showMessageDialog(this, "Reserva realizada con éxito.");
        dispose();
    }

    private void actualizarDisponibilidad() {
        File inputFile = new File("data/habitaciones.csv");
        File tempFile = new File("data/temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].equals(habitacion)) {
                    int disponibles = Integer.parseInt(partes[1]) - 1;
                    disponibles = Math.max(disponibles, 0); // evitar negativos
                    writer.write(partes[0] + "," + disponibles + "," + partes[2]);
                } else {
                    writer.write(linea);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar disponibilidad.");
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this, "Error al reemplazar archivo.");
        }
    }
}
