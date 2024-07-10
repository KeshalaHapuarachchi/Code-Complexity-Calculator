package swing;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassComplexityPieChart extends JPanel {
    ChartPanel chartPanel;
    private DefaultPieDataset pieDataset;
    private JFreeChart chart;

    // Define different shades of red
    private static final Color[] RED_SHADES = {
            new Color(255, 0, 0),       // Bright Red
            new Color(220, 0, 0),       // Darker Red
            new Color(180, 0, 0),       // Even Darker Red
            new Color(140, 0, 0),       // Deep Red
            new Color(100, 0, 0)        // Very Deep Red
    };

    public ClassComplexityPieChart() {
        setLayout(new BorderLayout());

        // Initialize pie chart dataset
        pieDataset = new DefaultPieDataset();

        // Create a chart based on the dataset
        chart = ChartFactory.createRingChart("Code Eelements Analysis classwise", pieDataset, true, true, false);
        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setSectionDepth(0.35);
        plot.setDirection(Rotation.CLOCKWISE);

        // Customize the chart appearance
        chart.setBackgroundPaint(Color.WHITE);
        chart.setTitle(new TextTitle("Class Complexity Analysis", new Font("Serif", Font.BOLD, 16)));

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        chartPanel.setMouseWheelEnabled(true);

        add(chartPanel, BorderLayout.CENTER);
    }

    public void calculateAndShowComplexity(String javaCode) {
        clearPieChart();
        List<ClassComplexity> classComplexities = analyzeCode(javaCode);

        for (int i = 0; i < classComplexities.size(); i++) {
            ClassComplexity classComplexity = classComplexities.get(i);
            String className = classComplexity.getClassName();
            int complexity = classComplexity.getComplexity();

            // Get a shade of red based on the index
            Color redShade = RED_SHADES[i % RED_SHADES.length];

            pieDataset.setValue(className, complexity);
            PiePlot piePlot = (PiePlot) chart.getPlot();
            piePlot.setSectionPaint(className, redShade);
            
            piePlot.setStartAngle(45);
            piePlot.setBackgroundPaint(Color.WHITE);
            piePlot.setLabelBackgroundPaint(Color.WHITE);
            piePlot.setOutlineVisible(false);
            piePlot.setSectionOutlinesVisible(false);
            piePlot.setLabelLinkMargin(0);
        }
    }

    private void clearPieChart() {
        pieDataset.clear();
    }

    private List<ClassComplexity> analyzeCode(String javaCode) {
        List<ClassComplexity> classComplexities = new ArrayList<>();
        String[] lines = javaCode.split("\n");
        int currentComplexity = 0;
        String currentClassName = null;

        for (String line : lines) {
            line = line.trim();

            if (isClassDeclaration(line)) {
                // A new class declaration is encountered, so save the previous class's complexity
                if (currentClassName != null) {
                    classComplexities.add(new ClassComplexity(currentClassName, currentComplexity));
                }

                // Reset the current class information
                currentClassName = extractClassName(line);
                currentComplexity = 0;
            } else {
                // Calculate complexity of non-class lines (e.g., methods, code inside classes)
                currentComplexity += calculateLineComplexity(line);
            }
        }

        // Add the last class's complexity if it exists
        if (currentClassName != null) {
            classComplexities.add(new ClassComplexity(currentClassName, currentComplexity));
        }

        return classComplexities;
    }

    private boolean isClassDeclaration(String line) {
        Pattern pattern = Pattern.compile("^\\s*(public|private|protected)?\\s*(abstract)?\\s*(class|interface|enum)\\s+[A-Za-z_][A-Za-z0-9_]*\\s*.*\\{?\\s*$");
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private String extractClassName(String line) {
        // Extract the class name from the class declaration
        Pattern pattern = Pattern.compile("\\s*(public|private|protected)?\\s*(abstract)?\\s*(class|interface|enum)\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*.*\\{?\\s*$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return matcher.group(4);
        }
        return null;
    }

    private int calculateLineComplexity(String line) {
        int complexity = 0;

        // Count control structures (e.g., if statements, loops)
        if (line.contains("if") || line.contains("while") || line.contains("for")) {
            complexity++;
        }

        // Count method calls (you can customize this based on your needs)
        if (line.contains("(") && line.contains(")")) {
            complexity++;
        }

        // Count threads (customize this based on your thread detection criteria)
        if (line.contains("Thread") || line.contains("Runnable")) {
            complexity++;
        }

        // Count pointers/references (customize this based on your criteria)
        if (line.contains("*") || line.contains("&")) {
            complexity++;
        }

        // Add other complexity factors as needed

        return complexity;
    }

    private static class ClassComplexity {
        private String className;
        private int complexity;

        public ClassComplexity(String className, int complexity) {
            this.className = className;
            this.complexity = complexity;
        }

        public String getClassName() {
            return className;
        }

        public int getComplexity() {
            return complexity;
        }
    }
}
