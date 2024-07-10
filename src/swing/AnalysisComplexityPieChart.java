package swing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.Rotation;

public class AnalysisComplexityPieChart extends JPanel implements ComplexityCalculator {
    ChartPanel chartPanel;

    public AnalysisComplexityPieChart() {
        setLayout(new BorderLayout());

        chartPanel = new ChartPanel(null);  // Initialize with an empty chart panel
        chartPanel.setBackground(Color.WHITE); 
        add(chartPanel, BorderLayout.CENTER);
    }

    @Override
    public void calculateAndShowComplexity(String code) {
        int totalLines = code.split("\n").length;
        int numConditionals = calculateConditionalCount(code);
        int numLoops = calculateLoopCount(code);
        int numMethods = calculateMethodCount(code);
        int numThreads = calculateThreadCount(code);
        int numPointersReferences = calculatePointerReferenceCount(code);
        int numClasses = calculateClassCount(code);

        createAndShow3DPieChart(totalLines, numConditionals, numLoops, numMethods, numThreads, numPointersReferences, numClasses);
    }

    private void createAndShow3DPieChart(int totalLines, int numConditionals, int numLoops, int numMethods, int numThreads,
            int numPointersReferences, int numClasses) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue("Conditionals", numConditionals);
        dataset.setValue("Loops", numLoops);
        dataset.setValue("Methods", numMethods);
        dataset.setValue("Threads", numThreads);
        dataset.setValue("Pointers/References", numPointersReferences);
        dataset.setValue("Classes", numClasses);

        JFreeChart chart = ChartFactory.createPieChart3D("Code Complexity Analysis", dataset, true, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        
        
        
        
        // Set chart title
        chart.setTitle(new TextTitle("Code Element Count as a Percentage", new Font("Serif", Font.BOLD, 16)));
        chart.getTitle().setPaint(Color.BLACK);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setDepthFactor(0.15);
        
       
        
        plot.setStartAngle(45);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setSectionOutlinesVisible(false);
        plot.setLabelLinkMargin(0);

        
        plot.setDirection(Rotation.CLOCKWISE); // Reverse the order of sections

        

        // Remove section outlines
        plot.setSectionOutlinesVisible(false);

        // Set custom color codes for sections
        plot.setSectionPaint("Conditionals", Color.decode("#014421")); // Orange
        plot.setSectionPaint("Loops", Color.decode("#175703")); // Deep Sky Blue
        plot.setSectionPaint("Methods", Color.decode("#247303")); // Lime Green
        plot.setSectionPaint("Threads", Color.decode("#53B002")); // Gold
        plot.setSectionPaint("Pointers/References", Color.decode("#7FD404")); // Blue Violet
        plot.setSectionPaint("Classes", Color.decode("#A2E009")); // Deep Pink
        
        

        chartPanel.setChart(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400)); // Adjust dimensions as needed
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // Other methods remain the same

    public int calculateConditionalCount(String code) {
        // Count conditional statements in the code
        int count = 0;
        Pattern pattern = Pattern.compile("if\\s*\\(|else\\s*\\(|switch\\s*\\(");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public int calculateLoopCount(String code) {
        // Count loops in the code
        int count = 0;
        Pattern pattern = Pattern.compile("(for\\s*\\(|while\\s*\\(|do\\s*\\()");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public int calculateMethodCount(String code) {
        // Count methods in the code
        int count = 0;
        Pattern pattern = Pattern.compile("void\\s+\\w+\\s*\\(|int\\s+\\w+\\s*\\(|String\\s+\\w+\\s*\\(");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public int calculateThreadCount(String code) {
        // Count threads in the code (you can customize the pattern for your use case)
        int count = 0;
        Pattern pattern = Pattern.compile("Thread\\s+\\w+\\s*=");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public int calculatePointerReferenceCount(String code) {
        // Count pointer/reference lines in the code
        int count = 0;
        String[] lines = code.split("\\n");

        for (String line : lines) {
            if (line.contains("*")) {
                count++;
            }
        }

        return count;
    }

    public int calculateClassCount(String code) {
        // Count class definitions in the code
        int count = 0;
        Pattern pattern = Pattern.compile("class\\s+\\w+\\s*\\{");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            count++;
        }

        return count;
    }
}


