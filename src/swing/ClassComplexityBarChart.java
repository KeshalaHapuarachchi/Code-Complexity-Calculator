package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

public class ClassComplexityBarChart extends JPanel {
    ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;
    private JFreeChart chart;

    public ClassComplexityBarChart() {
        setLayout(new BorderLayout());

        // Initialize bar chart dataset
        dataset = new DefaultCategoryDataset();

        // Create a 3D bar chart based on the dataset
        chart = ChartFactory.createBarChart(
                "Code Elements Analysis classwise",
                "Class Name",
                "Complexity",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);

        // Customize the chart appearance
        chart.setBackgroundPaint(Color.WHITE);
        TextTitle title = new TextTitle("Class Complexity Analysis", new Font("Serif", Font.BOLD, 16));
        chart.setTitle(title);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        chartPanel.setMouseWheelEnabled(true);

        add(chartPanel, BorderLayout.CENTER);
    }

    public void calculateAndShowComplexity(String javaCode) {
        clearBarChart();
        List<ClassComplexity> classComplexities = analyzeCode(javaCode);

        for (ClassComplexity classComplexity : classComplexities) {
            String className = classComplexity.getClassName();
            int complexity = classComplexity.getComplexity();
            dataset.addValue(complexity, "Complexity", className);
        }
        
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        chartPanel.setMouseWheelEnabled(false);

        CategoryItemRenderer renderer = new BarRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        plot.setRenderer(renderer);

     // Customize the appearance of the bars
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setSeriesPaint(0, new Color(206, 20, 106)); // Dodger Blue
        
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
    }

    private void clearBarChart() {
        dataset.clear();
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

