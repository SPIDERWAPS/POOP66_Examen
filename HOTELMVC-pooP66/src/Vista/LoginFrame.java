package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistrar;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        txtContraseña = new JPasswordField();
        add(txtContraseña);

        btnLogin = new JButton("Ingresar");
        add(btnLogin);

        btnRegistrar = new JButton("Registrarse");
        add(btnRegistrar);
    }

    // Listener para botón ingresar
    public void setLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    // Listener para botón registrar
    public void setRegistrarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContraseña() {
        return new String(txtContraseña.getPassword());
    }
}
