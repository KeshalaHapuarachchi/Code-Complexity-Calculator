package swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassesComplexity extends JPanel implements ComplexityCalculator {

    private JTable table;
    private DefaultTableModel tableModel;

    public ClassesComplexity() {
        setLayout(new BorderLayout());
//        initializeTable();
        
        //Create a container panel for the table panel
        JPanel tableContainer = new JPanel(new BorderLayout());
        //tableContainer.setPreferredSize(new Dimension(400, getHeight()));
        
        // Create a JSplitPane to divide the window into four parts
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Set the initial size of the top and bottom parts

        // Create the table and add it to the bottom part
        initializeTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        splitPane.setBottomComponent(tableScrollPane);
        
     // Create a title label and set its properties
        JLabel titleLabel = new JLabel("Classes Wise Complexity", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Title font
       

        add(splitPane, BorderLayout.CENTER);
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(
                new String[]{"Class Name", "ICB Value"}, 0
        );

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        // Set the font and row height
        Font tableFont = new Font("Arial", Font.PLAIN, 16); // Change font size here
        table.setFont(tableFont);
        table.setRowHeight(40); // Change row height here

        // Set custom cell renderer for cell appearance
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // Adjust dimensions as needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create a heading panel with a label
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headingLabel = new JLabel("Classes Wise Complexity", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Customize the font
        headingPanel.add(headingLabel);

        // Create a container panel for the table panel and heading panel
        JPanel containerPanel = new JPanel(new BorderLayout());

        // Add the heading panel to the container panel
        containerPanel.add(headingPanel, BorderLayout.NORTH);

        // Add the scroll pane containing the table to the center of the container panel
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        // Now, add the container panel to the main panel
        add(containerPanel);

        // Set custom header renderer
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setDefaultRenderer(new CustomTableHeaderRenderer());

        table.setBorder(null);

        // Add alternate row colors
        table.setDefaultRenderer(Object.class, new AlternatingRowColorRenderer());

        // Disable auto-resizing of the table columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set an empty border to hide the outline of the container panel
        containerPanel.setBorder(BorderFactory.createEmptyBorder());
    }


    
    

    @Override
    public void calculateAndShowComplexity(String javaCode) {
        clearTable();

        String[] lines = javaCode.split("\n");
        int lineNumber = 0;
        int sumOfWc = 0;
        int openBraceCount = 0; // Count of open curly braces
        String currentClassName = null;

        while (lineNumber < lines.length) {
            String line = lines[lineNumber].trim();
            int size = SizeComplexityCalculatorApp.calculateSize(line);
            int controlStructureWeight = ControlStructureWeightCalculator.calculateControlStructureWeight(line);
            int inheritanceWeight = InheritanceLevelCalculator.CalculateInheritanceLevelCalculator(line);
            int nestingWeight = calculateNesting(line);
            int Wt = controlStructureWeight + inheritanceWeight + nestingWeight;
            int Wc = Wt * size;

            boolean isClassDeclaration = isClassDeclaration(line);
            boolean isEmptyLine = line.trim().isEmpty();

            if (isClassDeclaration) {
                // Extract and store the class name when a class declaration is found
                currentClassName = extractClassName(line); // Implement a method to extract the class name
            }

            // Update the openBraceCount based on curly braces
            openBraceCount += countOpenBraces(line);
            if (openBraceCount == 0 && currentClassName != null) {
                // End of the class is reached
                sumOfWc += (Wc >= 0 ? Wc : 0);
                addClassToTable(currentClassName, sumOfWc);
                currentClassName = null; // Reset the class name
                sumOfWc = 0; // Reset sumOfWc for the next class
            } else if (openBraceCount == 0 && currentClassName == null) {
                // Calculate complexity outside of classes and add to the table
                tableModel.addRow(new Object[]{
                        "Line " + (lineNumber + 1),
                        size >= 0 ? size : "",
                        controlStructureWeight >= 0 ? controlStructureWeight : "",
                        inheritanceWeight >= 0 ? inheritanceWeight : "",
                        nestingWeight >= 0 ? nestingWeight : "",
                        Wt >= 0 ? Wt : "",
                        Wc >= 0 ? Wc : ""
                });
            } else {
                // Accumulate Wc within the current class
                sumOfWc += (Wc >= 0 ? Wc : 0);
            }

            // Check for unmatched closing braces (syntax error)
            if (openBraceCount < 0) {
                System.out.println("Syntax error: Unmatched '}' at line " + (lineNumber + 1));
                openBraceCount = 0; // Reset brace count to avoid further errors
            }
            
         // Adjust column widths
            TableColumn column;
            for (int i = 0; i < table.getColumnCount(); i++) {
                column = table.getColumnModel().getColumn(i);
                if (i == 0) {
                    column.setPreferredWidth(500); // Adjust the first column width
                } else {
                    column.setPreferredWidth(470); // Adjust other column widths
                }
            }

            lineNumber++;
        }
    }

    // Helper method to count open curly braces in a line
    private int countOpenBraces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') {
                count++;
            } else if (c == '}') {
                count--;
            }
        }
        return count;
    }

    // Helper method to add class name and sumOfWc to the table
    private void addClassToTable(String className, int sumOfWc) {
        tableModel.addRow(new Object[]{
                "Class: " + className,
                
                sumOfWc
        });
    }


    private void addClassToTable(int startLine, int endLine, String className, int sumOfWc) {
        // Add the class data to the table
        tableModel.addRow(new Object[]{
                "Class: " + className,
                
                sumOfWc
        });
    }

    private boolean isClassDeclaration(String line) {
        Pattern pattern = Pattern.compile("^\\s*(public|private|protected)?\\s*(abstract)?\\s*(class|interface|enum)\\s+[A-Za-z_][A-Za-z0-9_]*\\s*.*\\{?\\s*$");
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private String extractClassName(String line) {
        // Extract the class name from a class declaration
        Pattern pattern = Pattern.compile("class\\s+([A-Za-z_][A-Za-z0-9_]*)");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return null; // Return null if no class name is found
    }

    private int calculateNesting(String line) {
        int nestingLevel = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') {
                nestingLevel++;
            } else if (c == '}') {
                nestingLevel--;
            }
        }
        return nestingLevel;
    }

    private void clearTable() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    // Custom cell renderer for cell appearance
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            // Customize cell appearance here (e.g., font, background color, alignment)
            c.setBackground(isSelected ? Color.YELLOW : Color.WHITE);
            c.setForeground(Color.BLACK);
            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    // Custom cell renderer for alternating row colors
    private class AlternatingRowColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            // Set alternating row background colors
            if (row % 2 == 0) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                c.setBackground(Color.WHITE);
            }
            return c;
        }
    }
}
