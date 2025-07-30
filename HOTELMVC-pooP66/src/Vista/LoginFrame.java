package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistrar;
    private JButton btnRefrescarUsuarios; // nuevo botón

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 5, 5));

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCampos.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        txtContraseña = new JPasswordField();
        panelCampos.add(txtContraseña);
        add(panelCampos);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnLogin = new JButton("Ingresar");
        panelBotones.add(btnLogin);

        btnRegistrar = new JButton("Registrarse");
        panelBotones.add(btnRegistrar);

        btnRefrescarUsuarios = new JButton("Refrescar usuarios");
        panelBotones.add(btnRefrescarUsuarios);

        add(panelBotones);
    }

    // Listener para botón ingresar
    public void setLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    // Listener para botón registrar
    public void setRegistrarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }

    // Listener para botón refrescar usuarios
    public void setRefrescarUsuariosListener(ActionListener listener) {
        btnRefrescarUsuarios.addActionListener(listener);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContraseña() {
        return new String(txtContraseña.getPassword());
    }
}
