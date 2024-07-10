package swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

class CustomTableHeaderRenderer implements TableCellRenderer {
    DefaultTableCellRenderer renderer;

    public CustomTableHeaderRenderer() {
        renderer = (DefaultTableCellRenderer) new JTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER); // Center-align header text
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // Customize header appearance here (e.g., background color, font)
        c.setBackground(new Color(0, 102, 204)); // Change to your desired header background color
        c.setForeground(Color.WHITE); // Change to your desired header text color
        c.setFont(c.getFont().deriveFont(Font.BOLD)); // Make the header text bold
        return c;
    }
}

