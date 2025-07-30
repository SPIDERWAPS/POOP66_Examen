package modelo;

public class Administrador extends Usuario {

    public Administrador(String email, String contrasena) {
        super(email, contrasena);
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }
}
