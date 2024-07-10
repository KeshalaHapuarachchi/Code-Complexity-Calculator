package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControlStructureWeightCalculator extends JPanel implements ComplexityCalculator{
	
	public ControlStructureWeightCalculator() {
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
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Line Number", "Wc"}, 0);
        String[] lines = javaCode.split("\n");

        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();
            int size = calculateControlStructureWeight(line);

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

            tableModel.addRow(new Object[]{"Line " + (lineNumber + 1), size >= 0 ? size : ""});
        }

        return tableModel;
    }

    public static int calculateControlStructureWeight(String statement) {
        int controlStructureWeight = 0;

        if (statement.matches("\\bif\\b.*")) {
        	String[] conditions = statement.split("&&");
            // Count each condition separately
            controlStructureWeight = conditions.length;
        } else if (statement.matches("\\b(?:for|while|do)\\b.*")) {
        	String[] conditions = statement.split("&&");
            // Count each condition separately
            controlStructureWeight = conditions.length * 2;
        } else if (statement.matches("\\bswitch\\b.*")) {
            int n = countMatches(statement, "\\bcase\\b.*"); // Count the number of cases
            controlStructureWeight = n; // Switch statement with n cases
        }

        return controlStructureWeight;
    }

    public static int countMatches(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

}
