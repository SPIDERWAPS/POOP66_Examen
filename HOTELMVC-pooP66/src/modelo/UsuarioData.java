package modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UsuarioData {

    private static final String RUTA_USUARIOS = "data/usuarios.csv";

    public static List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        File archivo = new File(RUTA_USUARIOS);
        if (!archivo.exists()) {
            // Si no existe el archivo, devolvemos lista vacía sin error
            return lista;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String email = partes[0].trim();
                    String contrasena = partes[1].trim();
                    String rol = partes[2].trim();

                    if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
                        lista.add(new Administrador(email, contrasena));
                    } else if ("RECEPCIONISTA".equalsIgnoreCase(rol)) {
                        lista.add(new Recepcionista(email, contrasena));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_USUARIOS), StandardCharsets.UTF_8))) {

            for (Usuario u : usuarios) {
                String linea = String.format("%s,%s,%s", u.getEmail(), u.getContrasena(), u.getRol());
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}
