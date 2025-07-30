package utils;

import modelo.Cliente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UtilCSV {

    // Leer clientes desde CSV
    public static List<Cliente> cargarClientesDesdeCSV(File archivo) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 5) {
                    String nombre = campos[0].trim();
                    String apellido = campos[1].trim();
                    String cedula = campos[2].trim();
                    String telefono = campos[3].trim();
                    String correo = campos[4].trim();

                    Cliente cliente = new Cliente(nombre, apellido, cedula, telefono, correo);
                    clientes.add(cliente);
                } else {
                    System.out.println("Línea ignorada (campos insuficientes): " + linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // Guardar lista completa de clientes en CSV
    public static void guardarClientesEnCSV(List<Cliente> clientes, File archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {

            for (Cliente cliente : clientes) {
                String linea = String.join(",",
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getCedula(),
                        cliente.getTelefono(),
                        cliente.getEmail()
                );
                bw.write(linea);
                bw.newLine();
            }

        }
    }
}
