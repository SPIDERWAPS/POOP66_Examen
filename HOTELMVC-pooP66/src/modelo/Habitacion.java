package modelo;

import java.io.Serializable;

public class Habitacion implements Serializable {
    private String nombre;
    private TipoHabitacion tipo;
    private double precio;
    private int cantidadDisponible;

    public Habitacion(String nombre, TipoHabitacion tipo, double precio, int cantidadDisponible) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return nombre + " - " + tipo;
    }
}
