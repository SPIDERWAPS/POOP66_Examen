package vista;

import java.awt.*;
import javax.swing.*;

public class ButtonEditor extends DefaultCellEditor {

    private JButton button;
    private VistaPrincipal app;
    private String tipo;

    public ButtonEditor(JCheckBox checkBox, VistaPrincipal app) {
        super(checkBox);
        this.app = app;
        button = new JButton("Reservar");
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        tipo = table.getValueAt(row, 0).toString();
        return button;
    }

    public Object getCellEditorValue() {
        app.reservar(tipo);
        return "Reservar";
    }
}
