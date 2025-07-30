package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva implements Pago {
    private String idReserva;
    private Cliente cliente;
    private Habitacion habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precioTotal;

    private List<ServicioAdicional> serviciosAdicionales = new ArrayList<>();

    // Constructor con idReserva y servicios adicionales
    public Reserva(String idReserva, Cliente cliente, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, List<ServicioAdicional> serviciosAdicionales) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.serviciosAdicionales = serviciosAdicionales != null ? serviciosAdicionales : new ArrayList<>();
        this.precioTotal = calcularTotal();
    }

    // Constructor con idReserva sin servicios adicionales
    public Reserva(String idReserva, Cliente cliente, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        this(idReserva, cliente, habitacion, fechaInicio, fechaFin, new ArrayList<>());
    }

    // Constructor sin idReserva (genera id automáticamente)
    public Reserva(Cliente cliente, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        this("R" + System.currentTimeMillis(), cliente, habitacion, fechaInicio, fechaFin, new ArrayList<>());
    }

    // Getters y setters
    public String getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public List<ServicioAdicional> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    // Agregar servicio adicional
    public void agregarServicioAdicional(ServicioAdicional servicio) {
        serviciosAdicionales.add(servicio);
        this.precioTotal = calcularTotal();
    }

    // Eliminar servicio adicional
    public void eliminarServicioAdicional(ServicioAdicional servicio) {
        serviciosAdicionales.remove(servicio);
        this.precioTotal = calcularTotal();
    }

    @Override
    public double calcularTotal() {
        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        if (dias <= 0) dias = 1; // mínimo un día
        double totalHabitacion = habitacion.getPrecio() * dias;

        double totalServicios = serviciosAdicionales.stream()
            .mapToDouble(ServicioAdicional::getPrecio)
            .sum();

        return totalHabitacion + totalServicios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva r = (Reserva) o;
        return idReserva != null && idReserva.equals(r.getIdReserva());
    }

    @Override
    public int hashCode() {
        return idReserva == null ? 0 : idReserva.hashCode();
    }

    @Override
    public String toString() {
        return "Reserva ID: " + idReserva +
               ", Cliente: " + cliente.getNombre() + " " + cliente.getApellido() +
               ", Habitación: " + habitacion.getNombre() +
               ", Desde: " + fechaInicio +
               ", Hasta: " + fechaFin +
               ", Precio Total: $" + String.format("%.2f", precioTotal);
    }
}
