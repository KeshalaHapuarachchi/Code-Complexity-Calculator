package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Stack;

public class NestingLevelControlStructure extends JPanel implements ComplexityCalculator {

    public NestingLevelControlStructure() {
        setLayout(new BorderLayout());
    }

    @Override
    public void calculateAndShowComplexity(String javaCode) {
        DefaultTableModel tableModel = createTableModel(javaCode);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        removeAll();
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    Stack<Integer> nestingLevelStack = new Stack<>();
    int currentNestingLevel = 0;

    public DefaultTableModel createTableModel(String javaCode) {
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"Line Number", "Nesting Level"},
                0
        );
        String[] lines = javaCode.split("\n");

        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();
            int nestingLevel = calculateNestingLevel(line);

            tableModel.addRow(new Object[]{"Line " + (lineNumber + 1), nestingLevel});
        }

        return tableModel;
    }

    public int calculateNestingLevel(String line) {
        int nestingLevel = 0;

        if (line.contains("if (") || line.startsWith("else if (") || line.startsWith("for (") || line.startsWith("while (")) {
            nestingLevelStack.push(currentNestingLevel + 1);
            currentNestingLevel++;
        } else if (line.equals("}") && !nestingLevelStack.isEmpty()) {
            currentNestingLevel = nestingLevelStack.pop();
        }

        nestingLevel = currentNestingLevel;
        return nestingLevel;
    }

}
