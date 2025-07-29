package vista;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setText("Reservar");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
