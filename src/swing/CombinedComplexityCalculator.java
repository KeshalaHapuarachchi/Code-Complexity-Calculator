package swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombinedComplexityCalculator extends JPanel implements ComplexityCalculator {

    private JTable table;
    private DefaultTableModel tableModel;
    private NestingLevelControlStructure nestingLevelControlStructure;
    private ControlStructureNestingLevelCalculator controlStructureNestingLevelCalculator; // Add this field


    public CombinedComplexityCalculator() {
        setLayout(new BorderLayout());
//        initializeTable();
        
        nestingLevelControlStructure = new NestingLevelControlStructure();
        controlStructureNestingLevelCalculator = new ControlStructureNestingLevelCalculator();
        
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
        
      

        add(splitPane, BorderLayout.CENTER);
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(
                new String[]{"Line Number", "S", "Wc", "Wi", "Wn", "Wt", "Wc"}, 0
        );

        
        table = new JTable(tableModel);
        
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };


        // Set the font and row height
        Font tableFont = new Font("Arial", Font.PLAIN, 16); // Change font size here
        table.setFont(tableFont);
        table.setRowHeight(24); // Change row height here

        // Set custom cell renderer for cell appearance
       // table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // Adjust dimensions as needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
        
        // Set custom cell renderer for cell appearance
       // table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        // Set custom header renderer
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setDefaultRenderer(new CustomTableHeaderRenderer());


        table.setBorder(null);


        // Add alternate row colors
        table.setDefaultRenderer(Object.class, new AlternatingRowColorRenderer());




        // Disable auto-resizing of the table columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        add(scrollPane, BorderLayout.CENTER);
    }
    
    

    @Override
    public void calculateAndShowComplexity(String javaCode) {
        clearTable();
        String[] lines = javaCode.split("\n");
        int sumOfWc = 0;

        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();
            
            DefaultTableModel nestingLevelModel = controlStructureNestingLevelCalculator.createTableModel(line);
           // NestingLevelControlStructure nestingLevelControlStructure = new NestingLevelControlStructure();

            int size = SizeComplexityCalculatorApp.calculateSize(line);
            int controlStructureWeight = ControlStructureWeightCalculator.calculateControlStructureWeight(line);
            int inheritanceWeight = InheritanceLevelCalculator.CalculateInheritanceLevelCalculator(line);
            
            int nestingWeight = (int) nestingLevelModel.getValueAt(0, 1);
            //System.out.println("New Nesting " + nestingWeight);
            
            //int nestingWeight = nestingLevelControlStructure.calculateNestingLevel(line);
            int Wt = controlStructureWeight + inheritanceWeight + nestingWeight;
            int Wc = Wt * size;

            boolean isClassDeclaration = isClassDeclaration(line);
            boolean isEmptyLine = line.trim().isEmpty();

            if (isClassDeclaration || line.equals("}") || line.equals("{") || isEmptyLine || line.equals("else") ||
                    line.matches("^(\\s*}\\s*else\\s*\\{\\s*|\\s*else\\s*\\{\\s*|\\s*else\\s*\\s*\\{\\s*|\\s*}\\s*else\\s*\\s*\\{\\s*)$")) {
                size = -1;
                controlStructureWeight = -1;
                inheritanceWeight = -1;
                nestingWeight = -1;
                Wt = -1;
                Wc = -1;
            }

            sumOfWc += (Wc >= 0 ? Wc : 0);

            tableModel.addRow(new Object[]{
                    "Line " + (lineNumber + 1),
                    size >= 0 ? size : "",
                    controlStructureWeight >= 0 ? controlStructureWeight : "",
                    inheritanceWeight >= 0 ? inheritanceWeight : "",
                    nestingWeight >= 0 ? nestingWeight : "",
                    Wt >= 0 ? Wt : "",
                    Wc >= 0 ? Wc : ""
            });
        }

        tableModel.addRow(new Object[]{"ICB Value", "", "", "", "", "", sumOfWc});

        // Adjust column widths
        TableColumn column;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(140); // Adjust the first column width
            } else {
                column.setPreferredWidth(57); // Adjust other column widths
            }
        }
    }

    private boolean isClassDeclaration(String line) {
        Pattern pattern = Pattern.compile("^\\s*(public|private|protected)?\\s*(abstract)?\\s*(class|interface|enum)\\s+[A-Za-z_][A-Za-z0-9_]*\\s*.*\\{?\\s*$");
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }



    private void clearTable() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

//    // Custom cell renderer for cell appearance
//    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                       boolean hasFocus, int row, int column) {
//            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//            // Customize cell appearance here (e.g., font, background color, alignment)
//            c.setBackground(isSelected ? Color.YELLOW : Color.WHITE);
//            c.setForeground(Color.BLACK);
//            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
//            return c;
//        }
//    }

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
