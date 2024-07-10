package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InheritanceLevelCalculator extends JPanel implements ComplexityCalculator {

    public InheritanceLevelCalculator() {
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

    public DefaultTableModel createTableModel(String javaCode) {
        DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Line Number", "Wi"},
            0
        );
        String[] lines = javaCode.split("\n");


        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();

            int size = CalculateInheritanceLevelCalculator(line);

            // Check if the line is a class declaration
            if (line.startsWith("class ") || line.contains("class")) {
                size = -1; // Set size complexity to zero for class declaration lines
            }else if(line.equals("}")) {
            	size = -1;
            }else if (line.trim().isEmpty()) {
                size = -1; // Return 0 for empty lines
            }
            
            if (line.equals("else")) {
                size = -1; // Return 0 for empty lines
             }
            
            if (line.matches("^(\\s*}\\s*else\\s*\\{\\s*|\\s*else\\s*\\{\\s*|\\s*else\\s*\\s*\\{\\s*|\\s*}\\s*else\\s*\\s*\\{\\s*)$")) {
            	size = -1;
            }

            //String inheritanceLevelWeight = (line.equals("}") || line.contains("class")) || line.contains("else") ? "" : Integer.toString(currentInheritanceLevel);
            tableModel.addRow(new Object[]{"Line " + (lineNumber + 1), size >= 0 ? size : ""});
        }

        return tableModel;
    }
    
    
    public static int CalculateInheritanceLevelCalculator(String line) {
    	line = line.replaceAll("\"(?:\\\\.|[^\\\\\"])*\"|'(?:\\\\.|[^\\\\'])+'", "")
                .replaceAll("\\[(.*?)\\]", "")
                .replaceAll("\\s*\\{", "");// Remove square brackets
//                .replaceAll("\\((.*?)\\)", "");
    	
    	line = line.replaceAll("else\\s*\\{", "else{");
    	line  = line.replaceAll("else\\\\s*if", "else-if");
    	
    	int currentInheritanceLevel = 0;
        Stack<Integer> inheritanceLevelStack = new Stack<>();

            if (line.startsWith("class ") || line.contains("class")) {
                int classOpenBraceIndex = line.indexOf('{');
                if (classOpenBraceIndex != -1) {
                    inheritanceLevelStack.push(currentInheritanceLevel);
                    currentInheritanceLevel = 0;
                }
            } else if (line.equals("}")) {
                if (!inheritanceLevelStack.isEmpty()) {
                    currentInheritanceLevel = inheritanceLevelStack.pop();
                } else {
                    currentInheritanceLevel = 0;
                }
            }  else if (!line.isEmpty()) {
                currentInheritanceLevel = Math.max(currentInheritanceLevel, 1);
            }
    	
    	
    	return currentInheritanceLevel;
    }
}
