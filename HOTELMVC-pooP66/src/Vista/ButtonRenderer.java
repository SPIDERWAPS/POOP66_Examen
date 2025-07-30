package Vista;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value != null) {
            String texto = value.toString();
            setText(texto);
            if ("No disponible".equals(texto)) {
                setEnabled(false);
                setBackground(Color.LIGHT_GRAY);
            } else {
                setEnabled(true);
                setBackground(UIManager.getColor("Button.background"));
            }
        } else {
            setText("");
        }

        return this;
    }
}
