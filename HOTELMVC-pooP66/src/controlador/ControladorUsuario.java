package controlador;

import modelo.Administrador;
import modelo.Recepcionista;
import modelo.Usuario;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ControladorUsuario {

    private static ControladorUsuario instancia;

    private final String rutaUsuarios = "data/usuarios.csv";
    private List<Usuario> listaUsuarios;
    private Usuario usuarioLogueado;

    private ControladorUsuario() {
        listaUsuarios = new ArrayList<>();
        cargarUsuarios();
    }

    public static synchronized ControladorUsuario getInstancia() {
        if (instancia == null) {
            instancia = new ControladorUsuario();
        }
        return instancia;
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        File archivo = new File(rutaUsuarios);
        if (!archivo.exists()) {
            return; // Archivo no existe, lista vacía
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String email = partes[0].trim();
                    String contrasena = partes[1].trim();
                    String rol = partes[2].trim();

                    if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
                        listaUsuarios.add(new Administrador(email, contrasena));
                    } else if ("RECEPCIONISTA".equalsIgnoreCase(rol)) {
                        listaUsuarios.add(new Recepcionista(email, contrasena));
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarUsuarios() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(rutaUsuarios), StandardCharsets.UTF_8))) {
            for (Usuario u : listaUsuarios) {
                String linea = String.format("%s,%s,%s", u.getEmail(), u.getContrasena(), u.getRol());
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean registrarUsuario(String email, String contrasena, String rol) {
        if (buscarUsuarioPorCorreo(email) != null) {
            return false; // Ya existe usuario con ese correo
        }

        Usuario nuevoUsuario;
        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
            nuevoUsuario = new Administrador(email, contrasena);
        } else if ("RECEPCIONISTA".equalsIgnoreCase(rol)) {
            nuevoUsuario = new Recepcionista(email, contrasena);
        } else {
            return false; // Rol inválido
        }

        listaUsuarios.add(nuevoUsuario);
        guardarUsuarios();
        return true;
    }

    public Usuario buscarUsuarioPorCorreo(String email) {
        for (Usuario u : listaUsuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public boolean validarCredenciales(String email, String contrasena) {
        Usuario u = buscarUsuarioPorCorreo(email);
        if (u != null && u.getContrasena().equals(contrasena)) {
            usuarioLogueado = u;
            return true;
        }
        return false;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void logout() {
        usuarioLogueado = null;
    }
}
