package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControlStructureNestingLevelCalculator extends JPanel implements ComplexityCalculator {

    public ControlStructureNestingLevelCalculator() {
        setLayout(new BorderLayout());
    }

    @Override
    public void calculateAndShowComplexity(String javaCode) {
        DefaultTableModel tableModel = createTableModel(javaCode);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Adjust dimensions as needed

        removeAll();
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    int currentNestingLevel = 0;
    public DefaultTableModel createTableModel(String javaCode) {
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"Line Number", "Nesting Level Weight"},
                0
        );
        String[] lines = javaCode.split("\n");

       
        
        Stack<Integer> nestingLevelStack = new Stack<>();

        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();

            if (line.startsWith("if (") || line.startsWith("else if (") || line.startsWith("if(") ||  line.startsWith("else if(")) {
                nestingLevelStack.push(currentNestingLevel + 1);
                currentNestingLevel++;
            } else if (line.equals("}") && !nestingLevelStack.isEmpty()) {
                currentNestingLevel = nestingLevelStack.pop();
            } else if(line.startsWith("public") || line.startsWith("private") || line.startsWith("protected")) {
            	currentNestingLevel = 0;
            }
            
            if (line.startsWith("for (") || line.startsWith("for(")) {
                nestingLevelStack.push(currentNestingLevel + 1);
                currentNestingLevel++;
            } else if (line.equals("}") && !nestingLevelStack.isEmpty()) {
                currentNestingLevel = nestingLevelStack.pop();
            } else if(line.startsWith("public") || line.startsWith("private") || line.startsWith("protected")) {
            	currentNestingLevel = 0;
            }
            
            if (line.startsWith("while (") || line.startsWith("while(")) {
                nestingLevelStack.push(currentNestingLevel + 1);
                currentNestingLevel++;
            } else if (line.equals("}") && !nestingLevelStack.isEmpty()) {
                currentNestingLevel = nestingLevelStack.pop();
            } else if(line.startsWith("public") || line.startsWith("private") || line.startsWith("protected")) {
            	currentNestingLevel = 0;
            }

            
            int nestingLevelWeight = currentNestingLevel;
            
            if (line.startsWith("if (") || line.startsWith("else if (")) {
                nestingLevelWeight = currentNestingLevel;
            }

            if (line.startsWith("else") || line.equals("}") || line.contains("else{") ||line.contains("else {") ) {
                nestingLevelWeight = 0; // Set weight to 0 for sequential lines
            }

            tableModel.addRow(new Object[]{"Line " + (lineNumber + 1), nestingLevelWeight}); 
        }

        return tableModel;
    }
}