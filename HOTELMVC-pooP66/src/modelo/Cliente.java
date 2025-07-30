package modelo;

public class Cliente {
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private String email;

    // Constructor completo
    public Cliente(String nombre, String apellido, String cedula, String telefono, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.email = email;
    }

    // Constructor vacío
    public Cliente() {
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCedula() { return cedula; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", cedula='" + cedula + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Permite comparar clientes por cédula
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return cedula != null && cedula.equalsIgnoreCase(cliente.cedula);
    }

    @Override
    public int hashCode() {
        return cedula != null ? cedula.toLowerCase().hashCode() : 0;
    }
}
