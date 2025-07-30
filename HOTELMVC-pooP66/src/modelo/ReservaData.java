package modelo;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservaData {

    private static final File ARCHIVO_RESERVAS = new File("data/reservas.csv");

    public static List<Reserva> cargarReservas(List<Cliente> clientes, List<Habitacion> habitaciones) {
        List<Reserva> reservas = new ArrayList<>();
        if (!ARCHIVO_RESERVAS.exists()) return reservas;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_RESERVAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);

                if (partes.length < 5) continue; // solo esperamos 5 columnas

                String tipoHabitacionStr = partes[0].trim();
                //String nombreCliente = partes[1].trim(); // solo para info, no se usa
                String cedulaCliente = partes[2].trim();
                String fechaInicioStr = partes[3].trim();
                String duracionStr = partes[4].trim();

                // Buscar cliente por cédula
                Cliente cliente = clientes.stream()
                        .filter(c -> c.getCedula().equalsIgnoreCase(cedulaCliente))
                        .findFirst()
                        .orElse(null);

                if (cliente == null) {
                    System.out.println("Cliente no encontrado para línea: " + linea);
                    continue;
                }

                try {
                    LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
                    int duracion = Integer.parseInt(duracionStr);
                    LocalDate fechaFin = fechaInicio.plusDays(duracion);

                    // Buscar habitación por tipo, sin modificar disponibilidad al cargar
                    Habitacion habitacion = null;
                    for (Habitacion h : habitaciones) {
                        if (h.getTipo().toString().equalsIgnoreCase(tipoHabitacionStr)) {
                            habitacion = h;
                            break;
                        }
                    }

                    if (habitacion == null) {
                        System.out.println("No se encontró habitación para la reserva: " + linea);
                        continue;
                    }

                    // Crear reserva
                    Reserva reserva = new Reserva(cliente, habitacion, fechaInicio, fechaFin);

                    reservas.add(reserva);
                } catch (Exception e) {
                    System.out.println("Error procesando línea: " + linea);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    public static void guardarReservas(List<Reserva> reservas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_RESERVAS, false))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Reserva r : reservas) {
                String tipoHabitacion = r.getHabitacion().getTipo().toString();
                String nombreCliente = r.getCliente().getNombre() + " " + r.getCliente().getApellido();
                String cedulaCliente = r.getCliente().getCedula();
                String fechaInicio = r.getFechaInicio().format(formatter);
                long duracion = java.time.temporal.ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin());

                String linea = String.join(",",
                        tipoHabitacion,
                        nombreCliente,
                        cedulaCliente,
                        fechaInicio,
                        String.valueOf(duracion)
                );

                bw.write(linea);
                bw.newLine();
            }
        }
    }
}
