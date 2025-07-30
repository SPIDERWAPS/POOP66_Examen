package controlador;

import modelo.Usuario;
import modelo.UsuarioData;
import modelo.HabitacionData;
import Vista.LoginFrame;
import Vista.RegistroUsuarioFrame;
import Vista.VistaPrincipal;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class ControladorLogin {
    private LoginFrame login;

    public ControladorLogin() {
        login = new LoginFrame();

        // Listener botón ingresar
        login.setLoginListener(e -> verificarLogin());

        // Listener botón registrar: abre ventana registro y oculta login
        login.setRegistrarListener(e -> {
            RegistroUsuarioFrame registroFrame = new RegistroUsuarioFrame();
            registroFrame.setVisible(true);
            login.dispose();
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

        List<Usuario> usuarios = UsuarioData.cargarUsuarios();
        Usuario usuarioEncontrado = null;

        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(usuario) && u.getContrasena().equals(pass)) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            JOptionPane.showMessageDialog(login, "Credenciales incorrectas.");
            return;
        }

        // Carga habitaciones si no existe el archivo binario
        File archivoBin = new File("habitaciones.dat");
        if (!archivoBin.exists()) {
            HabitacionData.cargarHabitaciones();
        }

        login.dispose();

        // Abrir la ventana principal con el usuario autenticado
        VistaPrincipal vista = new VistaPrincipal(usuarioEncontrado);
        vista.setVisible(true);
    }
}
