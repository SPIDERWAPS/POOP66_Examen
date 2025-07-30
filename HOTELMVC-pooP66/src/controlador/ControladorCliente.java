package controlador;

import modelo.Cliente;
import modelo.ClienteData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControladorCliente {

    private static ControladorCliente instancia;
    private List<Cliente> listaClientes;

    private ControladorCliente() {
        this.listaClientes = ClienteData.cargarClientes(); // Carga desde data/clientes.csv
    }

    public static synchronized ControladorCliente getInstancia() {
        if (instancia == null) {
            instancia = new ControladorCliente();
        }
        return instancia;
    }

    public boolean agregarCliente(Cliente cliente) {
        if (cliente == null
                || cliente.getEmail() == null || cliente.getEmail().isEmpty()
                || cliente.getCedula() == null || cliente.getCedula().isEmpty()) {
            System.out.println("Cliente inválido: falta email o cédula.");
            return false;
        }

        for (Cliente c : listaClientes) {
            if (c.getEmail().equalsIgnoreCase(cliente.getEmail())) {
                System.out.println("El cliente ya está registrado por email.");
                return false;
            }
            if (c.getCedula().equalsIgnoreCase(cliente.getCedula())) {
                System.out.println("El cliente ya está registrado por cédula.");
                return false;
            }
        }

        listaClientes.add(cliente);
        return guardarClientes();
    }

    public List<Cliente> getListaClientes() {
        return new ArrayList<>(listaClientes);
    }

    public Cliente buscarPorApellido(String apellido) {
        if (apellido == null || apellido.isEmpty()) {
            return null;
        }
        for (Cliente c : listaClientes) {
            if (c.getApellido().equalsIgnoreCase(apellido)) {
                return c;
            }
        }
        return null;
    }

    public Cliente buscarPorEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        for (Cliente c : listaClientes) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null;
    }

    public Cliente buscarPorCedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) {
            return null;
        }
        for (Cliente c : listaClientes) {
            if (c.getCedula().equalsIgnoreCase(cedula)) {
                return c;
            }
        }
        return null;
    }

    // ✅ Método agregado con el nombre que tu código espera
    public Cliente buscarClientePorCedula(String cedula) {
        return buscarPorCedula(cedula);
    }

    public boolean eliminarCliente(Cliente cliente) {
        if (cliente == null) {
            return false;
        }

        boolean removed = listaClientes.remove(cliente);
        if (removed) {
            // Elimina reservas asociadas al cliente
            ControladorReserva.getInstancia().eliminarReservasPorCliente(cliente);
            return guardarClientes();
        }
        return false;
    }

    private boolean guardarClientes() {
        try {
            ClienteData.guardarClientes(listaClientes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Nuevo método para recargar clientes desde archivo
    public void recargarClientes() {
        this.listaClientes = ClienteData.cargarClientes();
    }
}
