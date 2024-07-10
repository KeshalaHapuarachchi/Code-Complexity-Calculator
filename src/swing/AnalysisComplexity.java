package swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public class AnalysisComplexity extends JPanel implements ComplexityCalculator {
    ChartPanel chartPanel;

    public AnalysisComplexity() {
        setLayout(new BorderLayout());

        chartPanel = new ChartPanel(null);  // Initialize with an empty chart panel
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

        createAndShowBarChart(totalLines, numConditionals, numLoops, numMethods, numThreads, numPointersReferences, numClasses);
    }

    private void createAndShowBarChart(int totalLines, int numConditionals, int numLoops, int numMethods, int numThreads,
            int numPointersReferences, int numClasses) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(numConditionals, "Count", "Conditionals");
        dataset.addValue(numLoops, "Count", "Loops");
        dataset.addValue(numMethods, "Count", "Methods");
        dataset.addValue(numThreads, "Count", "Threads");
        dataset.addValue(numPointersReferences, "Count", "Pointers/References");
        dataset.addValue(numClasses, "Count", "Classes");

        JFreeChart chart = ChartFactory.createBarChart("Code element Analysis", "Complexity Type", "Count", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        CategoryItemRenderer renderer = new BarRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        plot.setRenderer(renderer);

        // Customize the appearance of the bars
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setSeriesPaint(0, new Color(30, 144, 255)); // Dodger Blue

        // Apply font styling to the chart
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));

        // Check if the legend exists before setting its font
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12));
        }

        chartPanel.setChart(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400)); // Adjust dimensions as needed
        chartPanel.revalidate();
        chartPanel.repaint();
        
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
    }


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