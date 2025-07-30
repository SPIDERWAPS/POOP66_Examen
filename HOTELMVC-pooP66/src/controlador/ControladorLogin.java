package controlador;

import modelo.Usuario;
//import controlador.ControladorUsuario;
import modelo.HabitacionData;
import modelo.Sesion;
import Vista.LoginFrame;
import Vista.RegistroUsuarioFrame;
import Vista.VistaPrincipal;

import javax.swing.*;
import java.io.File;

public class ControladorLogin {
    private LoginFrame login;

    public ControladorLogin() {
        login = new LoginFrame();

        // Listener botón ingresar
        login.setLoginListener(_ -> verificarLogin());

        // Listener botón registrar: abre ventana de registro y cierra login
        login.setRegistrarListener(_ -> {
            RegistroUsuarioFrame registroFrame = new RegistroUsuarioFrame();

            // Añadimos listener para cuando se cierre el registro, abrir login nuevo
            registroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // Al cerrar registro, abrir una nueva instancia de ControladorLogin
                    new ControladorLogin();
                }
            });

            registroFrame.setVisible(true);
            login.dispose();
        });

        // Listener botón refrescar usuarios
        login.setRefrescarUsuariosListener(_ -> {
            ControladorUsuario.getInstancia().recargarUsuarios();
            JOptionPane.showMessageDialog(login, "Lista de usuarios actualizada");
        });

        login.setVisible(true);
    }

    private void verificarLogin() {
        String usuario = login.getUsuario();
        String pass = login.getContraseña();

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(login, "Por favor, complete usuario y contraseña.");
            return;
        }

        ControladorUsuario controladorUsuario = ControladorUsuario.getInstancia();

        // Recargar usuarios para tener la lista actualizada
        controladorUsuario.recargarUsuarios();

        if (!controladorUsuario.validarCredenciales(usuario, pass)) {
            JOptionPane.showMessageDialog(login, "Credenciales incorrectas.");
            return;
        }

        Usuario usuarioEncontrado = controladorUsuario.buscarUsuarioPorCorreo(usuario);

        if (usuarioEncontrado == null) {
            JOptionPane.showMessageDialog(login, "Usuario no encontrado.");
            return;
        }

        // Guardar usuario autenticado en sesión
        Sesion.setUsuarioActual(usuarioEncontrado);

        // Cargar habitaciones si no existe el archivo binario
        File archivoBin = new File("habitaciones.dat");
        if (!archivoBin.exists()) {
            HabitacionData.cargarHabitaciones();
        }

        // Cerrar login y abrir la vista principal
        login.dispose();
        VistaPrincipal vista = new VistaPrincipal(usuarioEncontrado);
        vista.setVisible(true);
    }
}

