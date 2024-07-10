package swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HighlightLastRowTable extends JTable {

    public HighlightLastRowTable(DefaultTableModel model) {
        super(model);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        
        // Check if the current row is the last row
        int lastRow = getModel().getRowCount() - 1;
        if (row == lastRow) {
            // Highlight the last row
            c.setBackground(Color.YELLOW); // Highlight color for the last row
            // Set text to bold for the last row
            Font currentFont = c.getFont();
            Font boldFont = new Font(currentFont.getName(), Font.BOLD, currentFont.getSize());
            c.setFont(boldFont);
        } else {
            // Reset background color and font for other rows
            c.setBackground(Color.WHITE); // Default background color for other rows
            c.setFont(getFont()); // Reset font to the default for other rows
        }

        return c;
    }
}


