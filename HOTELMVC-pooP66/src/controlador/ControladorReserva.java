package controlador;

import modelo.Cliente;
import modelo.Habitacion;
import modelo.HabitacionData;
import modelo.Reserva;
import modelo.ReservaData;
import modelo.TipoHabitacion;
import modelo.Usuario;
import controlador.ControladorUsuario;
import utils.UtilCSV;
import Vista.FormularioDatosReserva;
import Vista.VistaPrincipal;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorReserva {
    private static ControladorReserva instancia;

    private List<Habitacion> listaHabitaciones;
    private List<Reserva> listaReservas;
    private List<Cliente> listaClientes;

    private final File archivoClientes = new File("data/clientes.csv");

    private VistaPrincipal vista;

    private ControladorReserva() {
        this.listaHabitaciones = HabitacionData.cargarHabitaciones();
        this.listaClientes = UtilCSV.cargarClientesDesdeCSV(archivoClientes);
        this.listaReservas = ReservaData.cargarReservas(listaClientes, listaHabitaciones);
    }

    public static synchronized ControladorReserva getInstancia() {
        if (instancia == null) {
            instancia = new ControladorReserva();
        }
        return instancia;
    }

    public void setVista(VistaPrincipal vista) {
        this.vista = vista;
    }

    public VistaPrincipal getVista() {
        return vista;
    }

    public List<Habitacion> getListaHabitaciones() {
        return listaHabitaciones;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    public boolean agregarReserva(Reserva reserva) {
        Habitacion h = reserva.getHabitacion();

        if (existeReservaSuperpuesta(h, reserva.getFechaInicio(), reserva.getFechaFin())) {
            JOptionPane.showMessageDialog(vista, "La habitación ya está reservada en las fechas indicadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (h.getCantidadDisponible() > 0) {
            listaReservas.add(reserva);
            h.setCantidadDisponible(h.getCantidadDisponible() - 1);

            if (listaClientes.stream().noneMatch(c -> c.getCedula().trim().equalsIgnoreCase(reserva.getCliente().getCedula().trim()))) {
                listaClientes.add(reserva.getCliente());
            }

            try {
                HabitacionData.guardarHabitaciones(listaHabitaciones);
                ReservaData.guardarReservas(listaReservas);
                UtilCSV.guardarClientesEnCSV(listaClientes, archivoClientes);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            actualizarVista();
            return true;
        }
        return false;
    }

    // Nuevo método para validar reservas superpuestas
    public boolean existeReservaSuperpuesta(Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        return listaReservas.stream()
                .filter(r -> r.getHabitacion().equals(habitacion))
                .anyMatch(r -> fechasSeTraslapan(r.getFechaInicio(), r.getFechaFin(), fechaInicio, fechaFin));
    }

    private boolean fechasSeTraslapan(LocalDate inicio1, LocalDate fin1, LocalDate inicio2, LocalDate fin2) {
        // Se traslapan si inicio1 < fin2 y inicio2 < fin1 (intervalos con límites inclusivos)
        return (inicio1.isBefore(fin2) || inicio1.isEqual(fin2)) && (inicio2.isBefore(fin1) || inicio2.isEqual(fin1));
    }

    public void reservarHabitacion(String tipoStr, VistaPrincipal vista) {
        TipoHabitacion tipoSeleccionado;
        try {
            tipoSeleccionado = TipoHabitacion.valueOf(tipoStr.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, "Tipo de habitación no válido.");
            return;
        }

        for (Habitacion h : listaHabitaciones) {
            if (h.getTipo() == tipoSeleccionado && h.getCantidadDisponible() > 0) {
                int confirm = JOptionPane.showConfirmDialog(vista,
                        "¿Confirmar reserva de habitación tipo " + tipoStr + "?",
                        "Confirmar Reserva",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    FormularioDatosReserva formulario = new FormularioDatosReserva(this.vista, h);
                    formulario.setVisible(true);

                    if (formulario.isReservaConfirmada()) {
                        Reserva reserva = formulario.getReserva();
                        if (reserva != null) {
                            agregarReserva(reserva);
                        }
                    }
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "No hay habitaciones disponibles de ese tipo.");
    }

    public List<Reserva> buscarReservasPorCliente(Cliente cliente) {
        String cedulaBusqueda = cliente.getCedula().trim().toLowerCase();
        return listaReservas.stream()
                .filter(r -> r.getCliente().getCedula().trim().toLowerCase().equals(cedulaBusqueda))
                .collect(Collectors.toList());
    }

    public boolean eliminarReserva(Reserva reserva) {
        Usuario usuarioActual = ControladorUsuario.getInstancia().getUsuarioLogueado();

        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debe iniciar sesión en una cuenta de ADMINISTRADOR para eliminar reservas.");
            return false;
        }

        if ("ADMINISTRADOR".equalsIgnoreCase(usuarioActual.getRol())) {
            return eliminarReservaInterna(reserva);
        }

        if ("RECEPCIONISTA".equalsIgnoreCase(usuarioActual.getRol())) {
            JPanel panel = new JPanel(new java.awt.GridLayout(2, 2));
            JTextField emailField = new JTextField();
            JPasswordField passField = new JPasswordField();
            panel.add(new JLabel("Email Administrador:"));
            panel.add(emailField);
            panel.add(new JLabel("Contraseña:"));
            panel.add(passField);

            int option = JOptionPane.showConfirmDialog(vista, panel, "Credenciales Administrador requeridas",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String emailAdmin = emailField.getText().trim();
                String passAdmin = new String(passField.getPassword());

                Usuario admin = ControladorUsuario.getInstancia().buscarUsuarioPorCorreo(emailAdmin);

                if (admin != null && "ADMINISTRADOR".equalsIgnoreCase(admin.getRol())
                        && admin.getContrasena().equals(passAdmin)) {
                    return eliminarReservaInterna(reserva);
                } else {
                    JOptionPane.showMessageDialog(vista, "Credenciales inválidas o no es administrador.");
                    return false;
                }
            } else {
                return false;
            }
        }

        JOptionPane.showMessageDialog(vista, "No tiene permisos para eliminar reservas.");
        return false;
    }

    private boolean eliminarReservaInterna(Reserva reserva) {
        if (listaReservas.remove(reserva)) {
            Habitacion hab = reserva.getHabitacion();
            hab.setCantidadDisponible(hab.getCantidadDisponible() + 1);

            try {
                HabitacionData.guardarHabitaciones(listaHabitaciones);
                ReservaData.guardarReservas(listaReservas);

                Cliente cliente = reserva.getCliente();
                boolean clienteTieneReservas = listaReservas.stream()
                        .anyMatch(r -> r.getCliente().getCedula().trim().equalsIgnoreCase(cliente.getCedula().trim()));

                if (!clienteTieneReservas) {
                    listaClientes.removeIf(c -> c.getCedula().trim().equalsIgnoreCase(cliente.getCedula().trim()));
                    UtilCSV.guardarClientesEnCSV(listaClientes, archivoClientes);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            actualizarVista();
            return true;
        }
        return false;
    }

    public void eliminarReservasPorCliente(Cliente cliente) {
        if (cliente == null) return;

        boolean seElimino = listaReservas.removeIf(reserva -> {
            if (reserva.getCliente().getCedula().trim().equalsIgnoreCase(cliente.getCedula().trim())) {
                Habitacion h = reserva.getHabitacion();
                h.setCantidadDisponible(h.getCantidadDisponible() + 1);
                return true;
            }
            return false;
        });

        if (seElimino) {
            listaClientes.removeIf(c -> c.getCedula().trim().equalsIgnoreCase(cliente.getCedula().trim()));
            try {
                HabitacionData.guardarHabitaciones(listaHabitaciones);
                ReservaData.guardarReservas(listaReservas);
                UtilCSV.guardarClientesEnCSV(listaClientes, archivoClientes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            actualizarVista();
        }
    }

    private void actualizarVista() {
        if (vista != null) {
            vista.actualizarTablaHabitaciones();
            vista.actualizarTablaReservas();
            vista.actualizarTablaClientes();
        }
    }
}
