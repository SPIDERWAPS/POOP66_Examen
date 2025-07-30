package Vista;

import modelo.Administrador;
import modelo.Recepcionista;
import modelo.Usuario;
import modelo.UsuarioData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RegistroUsuarioFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> rolCombo;

    public RegistroUsuarioFrame() {
        setTitle("Registro de Usuario");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCampos.add(new JLabel("Email:"));
        emailField = new JTextField();
        panelCampos.add(emailField);

        panelCampos.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panelCampos.add(passwordField);

        panelCampos.add(new JLabel("Rol:"));
        rolCombo = new JComboBox<>(new String[]{"ADMINISTRADOR", "RECEPCIONISTA"});
        panelCampos.add(rolCombo);

        add(panelCampos, BorderLayout.CENTER);

        JButton btnRegistrar = new JButton("Registrar");
        add(btnRegistrar, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(_ -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String rol = (String) rolCombo.getSelectedItem();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Usuario> usuarios = UsuarioData.cargarUsuarios();

            boolean existe = usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
            if (existe) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario;
            if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
                nuevoUsuario = new Administrador(email, password);
            } else {
                nuevoUsuario = new Recepcionista(email, password);
            }

            usuarios.add(nuevoUsuario);

            try {
                UsuarioData.guardarUsuarios(usuarios);
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito");
                // Al registrar, volvemos al LoginFrame
                new LoginFrame().setVisible(true);
                this.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar usuario", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}
