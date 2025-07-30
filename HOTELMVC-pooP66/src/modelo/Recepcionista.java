package modelo;

public class Recepcionista extends Usuario {

    public Recepcionista(String email, String contrasena) {
        super(email, contrasena);
    }

    @Override
    public String getRol() {
        return "RECEPCIONISTA";
    }
}
