package vista;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaPrincipal extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public VistaPrincipal() {
        setTitle("Gestión de Hotel");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{"Habitación", "Disponibles", "Precio", "Reservar"}, 0);
        tabla = new JTable(modelo);
        tabla.getColumn("Reservar").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Reservar").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        cargarHabitaciones();
    }

    public void cargarHabitaciones() {
        modelo.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("data/habitaciones.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                modelo.addRow(new Object[]{datos[0], datos[1], datos[2], "Reservar"});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer habitaciones.");
        }
    }

    public void reservar(String nombreHabitacion) {
        FormularioReserva formulario = new FormularioReserva(nombreHabitacion, this);
        formulario.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaPrincipal().setVisible(true));
    }
}
