package controlador;

import modelo.Habitacion;
import modelo.HabitacionData;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ControladorHabitacion {
    private List<Habitacion> listaHabitaciones;

    public ControladorHabitacion() {
        listaHabitaciones = HabitacionData.cargarHabitaciones();
    }

    public List<Habitacion> getListaHabitaciones() {
        return listaHabitaciones;
    }

    public boolean agregarHabitacion(Habitacion hab) {
        if (hab.getNombre() == null || hab.getNombre().isEmpty() || hab.getPrecio() <= 0 || hab.getCantidadDisponible() < 0) {
            JOptionPane.showMessageDialog(null, "Datos de habitación inválidos.");
            return false;
        }

        boolean existe = listaHabitaciones.stream()
                .anyMatch(h -> h.getNombre().equalsIgnoreCase(hab.getNombre()));
        if (existe) {
            JOptionPane.showMessageDialog(null, "Ya existe una habitación con ese nombre.");
            return false;
        }

        listaHabitaciones.add(hab);
        try {
            HabitacionData.guardarHabitaciones(listaHabitaciones);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar habitación.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
