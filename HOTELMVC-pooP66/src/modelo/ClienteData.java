package modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClienteData {

    private static final String RUTA_CLIENTES = "data/clientes.csv";

    public static List<Cliente> cargarClientes() {
        List<Cliente> lista = new ArrayList<>();
        File archivo = new File(RUTA_CLIENTES);

        if (!archivo.exists()) {
            System.out.println("Archivo de clientes no encontrado. Se creará uno nuevo.");
            return lista;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {  // Cambió a 5 columnas
                    String nombre = datos[0].trim();
                    String apellido = datos[1].trim();
                    String cedula = datos[2].trim();
                    String telefono = datos[3].trim();
                    String email = datos[4].trim();

                    Cliente c = new Cliente(nombre, apellido, cedula, telefono, email);
                    lista.add(c);
                } else {
                    System.out.println("Registro inválido en archivo clientes.csv: " + linea);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de clientes:");
            e.printStackTrace();
        }

        return lista;
    }

    public static void guardarClientes(List<Cliente> clientes) throws IOException {
        File archivo = new File(RUTA_CLIENTES);

        File carpeta = archivo.getParentFile();
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {

            for (Cliente c : clientes) {
                String nombre = c.getNombre().replace(",", "");
                String apellido = c.getApellido().replace(",", "");
                String cedula = c.getCedula().replace(",", "");
                String telefono = c.getTelefono().replace(",", "");
                String email = c.getEmail().replace(",", "");

                // Guardar con 5 columnas separadas
                String linea = String.format("%s,%s,%s,%s,%s", nombre, apellido, cedula, telefono, email);
                bw.write(linea);
                bw.newLine();
            }
        }

        System.out.println("Clientes guardados exitosamente en: " + archivo.getAbsolutePath());
    }
}
