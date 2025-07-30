import javax.swing.SwingUtilities;
import modelo.HabitacionData;
import controlador.ControladorLogin;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            File archivoBin = new File("habitaciones.dat");
            if (!archivoBin.exists()) {
                HabitacionData.cargarHabitaciones();
            }
            new ControladorLogin();
        });
    }
}
