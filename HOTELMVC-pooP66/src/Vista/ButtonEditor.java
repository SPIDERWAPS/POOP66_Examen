package Vista;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {

    private JButton button;
    private VistaPrincipal vistaPrincipal;
    private String tipoHabitacion;

    public ButtonEditor(JCheckBox checkBox, VistaPrincipal vistaPrincipal) {
        super(checkBox);
        this.vistaPrincipal = vistaPrincipal;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        tipoHabitacion = table.getValueAt(row, 0).toString();
        button.setText(value != null ? value.toString() : "");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if ("Reservar".equals(button.getText())) {
            vistaPrincipal.reservar(tipoHabitacion);
        }
        return button.getText();
    }
}
