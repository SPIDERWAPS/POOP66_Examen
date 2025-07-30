package modelo;

public abstract class Usuario {
    protected String email;
    protected String contrasena;

    public Usuario(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public abstract String getRol(); // Este lo implementan Administrador y Recepcionista
}
