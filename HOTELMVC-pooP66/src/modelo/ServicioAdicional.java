package modelo;

public class ServicioAdicional implements Pago {
    private String descripcion;
    private double precio;

    public ServicioAdicional(String descripcion, double precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public double calcularTotal() {
        return precio;
    }

    @Override
    public String toString() {
        return descripcion + " ($" + precio + ")";
    }
}
