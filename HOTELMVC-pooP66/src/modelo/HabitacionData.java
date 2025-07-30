package modelo;

import java.io.*;
import java.util.List;

public class HabitacionData {

    private static final String ARCHIVO_CSV = "data/habitaciones.csv";
    // Eliminamos la constante para el archivo binario

    // Carga las habitaciones siempre desde CSV
    public static List<Habitacion> cargarHabitaciones() {
        return cargarDesdeCSV();
    }

    // Carga habitaciones desde CSV
    private static List<Habitacion> cargarDesdeCSV() {
        List<Habitacion> lista = new java.util.ArrayList<>();
        File fileCSV = new File(ARCHIVO_CSV);
        if (!fileCSV.exists()) {
            System.err.println("No se encontró el archivo CSV en la ruta especificada: " + ARCHIVO_CSV);
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String nombre = partes[0].trim();
                    int cantidad = Integer.parseInt(partes[1].trim());
                    double precio = Double.parseDouble(partes[2].trim());
                    TipoHabitacion tipo = mapearTipo(nombre);
                    Habitacion hab = new Habitacion(nombre, tipo, precio, cantidad);
                    lista.add(hab);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Guarda las habitaciones en CSV (reescribe completamente)
    public static void guardarHabitaciones(List<Habitacion> listaHabitaciones) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CSV, false))) {
            for (Habitacion hab : listaHabitaciones) {
                // Formato: nombre,cantidadDisponible,precio
                String linea = hab.getNombre() + "," + hab.getCantidadDisponible() + "," + hab.getPrecio();
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    private static TipoHabitacion mapearTipo(String nombre) {
        String lower = nombre.toLowerCase();

        if (lower.contains("penthouse")) return TipoHabitacion.PENTHOUSE;
        if (lower.contains("deluxe")) return TipoHabitacion.DELUXE;
        if (lower.contains("suite ejecutiva")) return TipoHabitacion.SUITE_EJECUTIVA;
        if (lower.contains("suite")) return TipoHabitacion.SUITE;
        if (lower.contains("familiar")) return TipoHabitacion.FAMILIAR;
        if (lower.contains("cuádruple") || lower.contains("cuadruple")) return TipoHabitacion.FAMILIAR;
        if (lower.contains("triple")) return TipoHabitacion.TRIPLE;
        if (lower.contains("doble")) return TipoHabitacion.DOBLE;
        if (lower.contains("simple") || lower.contains("individual")) return TipoHabitacion.INDIVIDUAL;
        if (lower.contains("económica") || lower.contains("economica")) return TipoHabitacion.ECONOMICA;

        return TipoHabitacion.ECONOMICA;
    }
}
