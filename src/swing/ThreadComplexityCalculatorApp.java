package swing;
import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class ThreadComplexityCalculatorApp extends JPanel implements ComplexityCalculator {
    private ChartPanel chartPanel;
    private JPanel resultChartPanel;

    public ThreadComplexityCalculatorApp() {
        setLayout(new BorderLayout());
        resultChartPanel = new JPanel(new BorderLayout());
        chartPanel = new ChartPanel(null);  // Initialize with an empty chart panel
        add(chartPanel, BorderLayout.CENTER);
    }

    @Override
    public void calculateAndShowComplexity(String code) {
        int totalLines = code.split("\n").length;
        int numThreadOperations = calculateThreadOperationCount(code);
        double percentage = ((double) numThreadOperations / totalLines) * 100;

        createAndShowPieChart(percentage);
    }

    private void createAndShowPieChart(double percentage) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Thread Operations", percentage);
        dataset.setValue("Other Lines", 100 - percentage);

        JFreeChart chart = ChartFactory.createPieChart3D("Thread Complexity", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Thread Operations", Color.GREEN);
        plot.setSectionPaint("Other Lines", Color.GRAY);
        plot.setBackgroundPaint(Color.WHITE);

        chartPanel.setChart(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
    }

    public static int calculateThreadOperationCount(String code) {
        // Simulate the calculation of thread operation complexity
        int threadOperationCount = 0;
        String[] lines = code.split("\\n");

        for (String line : lines) {
            if (line.contains("Thread")) {
                threadOperationCount++;
            }
        }

        return threadOperationCount;
    }
}
