package swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.jfree.chart.ChartPanel;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.Arrays;


public class ComplexityCalculatorApp extends JFrame {

	private AdvancedTextArea codeTextArea;
    private JButton calculateButton;
    private JPanel resultPanel;
    private JPanel leftPanel;
    private JTabbedPane tabbedPane; // Add a tabbed pane for the right panel
    private JPanel resultPanelTab1;
    private JPanel resultPanelTab2;

    public ComplexityCalculatorApp() {
        setTitle("Java Code Complexity Investigator");
        setPreferredSize(new Dimension(1600, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Create UI components
        codeTextArea = new AdvancedTextArea(2, 4);
        calculateButton = new JButton("Calculate Complexity");
        codeTextArea.setPreferredSize(new Dimension(500, 400));
        resultPanel = new JPanel();
        tabbedPane = new JTabbedPane(); // Initialize the tabbed pane
        
        

        // Apply a custom color scheme
        codeTextArea.setBackground(new Color(240, 240, 240));

        // Create a main panel with BorderLayout to add padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding

        // Create a title label and set its properties
        JLabel titleLabel = new JLabel("Java Code Complexity Investigator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Title font
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create the left panel for code input and calculation button
        leftPanel = new JPanel(new BorderLayout());
        TitledBorder leftBorder = BorderFactory.createTitledBorder("Input Code");
        leftBorder.setTitleFont(new Font("Arial", Font.BOLD, 16)); // Title font
        leftPanel.setBorder(leftBorder);

        // Create a JScrollPane for the code input with hidden scrollbars
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        codeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        codeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Create a panel for the button and place it at the bottom of the left panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);

        leftPanel.add(codeScrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create the right panel with tabs
        TitledBorder rightBorder = BorderFactory.createTitledBorder("Results");
        rightBorder.setTitleFont(new Font("Arial", Font.BOLD, 20)); // Increase font size
        tabbedPane.setBorder(rightBorder); // Set the border to the tabbed pane
        
        tabbedPane.setUI(new CustomTabbedPaneUI());
        
        resultPanelTab1 = new JPanel();
        tabbedPane.addTab("Tab 1", resultPanelTab1);
        
        resultPanelTab2 = new JPanel();
        tabbedPane.addTab("Tab 2", resultPanelTab2);

        // Add tabs to the tabbed pane
        //tabbedPane.addTab("Tab 1", new JPanel()); // Replace 'new JPanel()' wi7th your desired content
        //tabbedPane.addTab("Tab 2", new JPanel()); // Replace 'new JPanel()' with your desired content
        tabbedPane.addTab("Tab 3", new JPanel()); // Replace 'new JPanel()' with your desired content

        // Add left panel and tabbed pane to the main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER); // Add the tabbed pane

        // Set a custom font for the UI components
        Font customFont = new Font("Arial", Font.PLAIN, 14);
        codeTextArea.setFont(customFont);

        // Style the Calculate button
        styleCalculateButton();
        
        // Style the tabs
        styleTabs();

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Add action listener for the Calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateComplexity();
                
                
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void styleCalculateButton() {
        // Customize button appearance
        calculateButton.setPreferredSize(new Dimension(200, 40)); // Set button size
        calculateButton.setBackground(Color.DARK_GRAY); // Green background
        calculateButton.setForeground(Color.BLACK); // White text color
        calculateButton.setFont(new Font("Arial", Font.BOLD, 16)); // Bold font
    }
    
    
    private void styleTabs() {
        UIManager.put("TabbedPane.selected", new ColorUIResource(Color.DARK_GRAY)); // Selected tab background color
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(10, 10, 10, 10)); // Tab content padding

        // Create a custom font for tab titles
        Font tabFont = new Font("Arial", Font.BOLD, 20); // Increase the font size
        UIManager.put("TabbedPane.font", new FontUIResource(tabFont));

        // Create custom colors for tab titles
        UIManager.put("TabbedPane.selectedForeground", new ColorUIResource(Color.WHITE)); // Selected tab title color
        UIManager.put("TabbedPane.foreground", new ColorUIResource(Color.BLACK)); // Unselected tab title color

        // Create custom colors for tab borders
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource(Color.DARK_GRAY)); // Border color
        UIManager.put("TabbedPane.light", new ColorUIResource(Color.LIGHT_GRAY)); // Border highlight color

        // Increase the tab width
        UIManager.put("TabbedPane.tabInsets", new Insets(15, 50, 15, 50)); // Adjust the insets to increase the width
    }


    // Define a method to create the result panel with table, bar chart, and pie charts
    private JPanel createResultPanel(String javaCode) {
        // Create an instance of the CombinedComplexityCalculator class
        CombinedComplexityCalculator calculator = new CombinedComplexityCalculator();

        // Calculate and display complexity using CombinedComplexityCalculator
        calculator.calculateAndShowComplexity(javaCode);

        // Create an instance of the PointerReferenceCalculatorApp class
        PointerReferenceCalculatorApp pointerCalculator = new PointerReferenceCalculatorApp();

        // Calculate pointer/reference complexity and get the result
        pointerCalculator.calculatePointerReferenceCount(javaCode);

        AnalysisComplexity analysisComplexity = new AnalysisComplexity();
        analysisComplexity.calculateAndShowComplexity(javaCode);

        AnalysisComplexityPieChart analysisComplexityPieChart = new AnalysisComplexityPieChart();
        analysisComplexityPieChart.calculateAndShowComplexity(javaCode);

        ClassComplexityPieChart classComplexityPieChart = new ClassComplexityPieChart();
        classComplexityPieChart.calculateAndShowComplexity(javaCode);

        // Use the chart panel from AnalysisComplexity
        ChartPanel analysisChartPanel = analysisComplexity.chartPanel;

        // Create the result panel with the table and pie chart side by side
        JPanel resultPanel = new JPanel(new GridLayout(2, 2)); // Use a 2x2 grid layout

        // Wrap the table in a JPanel and set its preferred size
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.add(calculator, BorderLayout.CENTER);
        tableWrapper.setPreferredSize(new Dimension(50, 200)); // Set preferred size for the table wrapper
        resultPanel.add(tableWrapper);

        JPanel classChartWrapper = new JPanel(new BorderLayout());
        classComplexityPieChart.calculateAndShowComplexity(javaCode); // Calculate pie chart
        classComplexityPieChart.chartPanel.setPreferredSize(new Dimension(100, 200)); // Set preferred size for the chart
        classChartWrapper.add(classComplexityPieChart.chartPanel, BorderLayout.CENTER);
        resultPanel.add(classChartWrapper);

        JPanel analysisChartWrapper = new JPanel(new BorderLayout());
        analysisChartPanel.setPreferredSize(new Dimension(100, 200)); // Set preferred size for the chart
        analysisChartWrapper.add(analysisChartPanel, BorderLayout.CENTER);
        resultPanel.add(analysisChartWrapper);

        JPanel overallComplexity = new JPanel(new BorderLayout());
        analysisComplexityPieChart.calculateAndShowComplexity(javaCode); // Calculate pie chart
        analysisComplexityPieChart.chartPanel.setPreferredSize(new Dimension(100, 200)); // Set preferred size for the chart
        overallComplexity.add(analysisComplexityPieChart.chartPanel, BorderLayout.CENTER);
        resultPanel.add(overallComplexity);

        return resultPanel;
    }
    
    
    // Define a method to create the result panel with table, bar chart, and pie charts
    private JPanel createResultPanel2(String javaCode) {
    	
        // Create an instance of the CombinedComplexityCalculator class
    	ClassesComplexity calculator = new ClassesComplexity();

        // Calculate and display complexity using CombinedComplexityCalculator
        calculator.calculateAndShowComplexity(javaCode);
        
     // Create an instance of the CombinedComplexityCalculator class
        ClassComplexityBarChart classesComplexityBarChart = new ClassComplexityBarChart();

        // Calculate and display complexity using CombinedComplexityCalculator
        classesComplexityBarChart.calculateAndShowComplexity(javaCode);



        // Create the result panel with the table and pie chart side by side
        JPanel resultPanel = new JPanel(new GridLayout(2, 2)); // Use a 2x2 grid layout

        // Wrap the table in a JPanel and set its preferred size
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.add(calculator, BorderLayout.CENTER);
        tableWrapper.setPreferredSize(new Dimension(50, 200)); // Set preferred size for the table wrapper
        resultPanel.add(tableWrapper);
        
     // Wrap the bar chart in a JPanel and set its preferred size
        JPanel barChartWrapper = new JPanel(new BorderLayout());
        barChartWrapper.add(classesComplexityBarChart, BorderLayout.CENTER);
        barChartWrapper.setPreferredSize(new Dimension(500, 400)); // Set preferred size for the bar chart wrapper
        resultPanel.add(barChartWrapper);



        return resultPanel;
    }
    
    
    private JPanel createResultPanel3(String javaCode) {
        HistogramChartApp histogramChartApp = new HistogramChartApp();
        histogramChartApp.calculateAndShowComplexity(javaCode); // Calculate complexity within the HistogramChartApp

        // Create a wrapper panel for the histogramChartApp
        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.add(histogramChartApp, BorderLayout.CENTER);
        chartWrapper.setPreferredSize(new Dimension(500, 400)); // Set preferred size for the chart wrapper

        // Create the result panel and add the chartWrapper
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(chartWrapper, BorderLayout.CENTER);

        return resultPanel;
    }


    // Modify the calculateComplexity method to call createResultPanel
    private void calculateComplexity() {
        String javaCode = codeTextArea.getText();

        if (javaCode.trim().isEmpty()) {
            // Input area is empty, show an alert
            JOptionPane.showMessageDialog(this, "Input area is empty. Please enter Java code.", "Empty Input", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method without performing the calculation
        }
        
        
        // Call the method to create the result panel with the calculated data
        JPanel newResultPanel = createResultPanel(javaCode);
        
        // Call the  method to create the result panel with the calculated data
        JPanel newResultPanel2 = createResultPanel2(javaCode);
        
     // Call the method to create the result panel with the calculated data
        JPanel newResultPanel3 = createResultPanel3(javaCode);

        // Add the new result panel to Tab 1
        tabbedPane.setComponentAt(0, newResultPanel);
        
        // Add the new result panel to Tab 1
        tabbedPane.setComponentAt(1, newResultPanel2);
        
     // Add the new result panel to Tab 2
        tabbedPane.setComponentAt(2, newResultPanel3);
        
    }
    
    
    public class CustomTabbedPaneUI extends BasicTabbedPaneUI {
        private int tabWidth = 200; // Set your desired tab width here
        private int fontSize = 14; // Set your desired font size here
        private int paddingTop = 10; // Set the padding at the top in pixels
        private Color selectedTabColor = Color.WHITE; // Set the selected tab font color to white

        @Override
        protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
            return tabWidth; // Adjust tab width
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            if (isSelected) {
                font = font.deriveFont(Font.BOLD, fontSize); // Adjust font size
                g.setFont(font);
                g.setColor(selectedTabColor); // Set the font color to white
                int x = textRect.x;
                int y = textRect.y + metrics.getAscent() + paddingTop; // Add padding to the top of the text
                g.drawString(title, x, y);
            } else {
                super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                	 // Set your custom UI properties here
                    UIManager.put("TabbedPane.selected", new ColorUIResource(Color.DARK_GRAY));
                    UIManager.put("TabbedPane.contentBorderInsets", new Insets(10, 10, 10, 10));
                    // ... (Other custom properties)

                    // Use the system look and feel for a more native appearance
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ComplexityCalculatorApp app = new ComplexityCalculatorApp();
                app.setVisible(true);
            }
        });
    }
}

