package swing;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;

public class PointerReferenceCalculatorApp extends JPanel implements ComplexityCalculator {
    ChartPanel chartPanel;

    public PointerReferenceCalculatorApp() {
        setLayout(new BorderLayout());

        chartPanel = new ChartPanel(null);  // Initialize with an empty chart panel
        add(chartPanel, BorderLayout.CENTER);
    }

    @Override
    public void calculateAndShowComplexity(String code) {
        int totalLines = code.split("\n").length;
        int numPointersReferences = calculatePointerReferenceCount(code);
        double percentage = ((double) numPointersReferences / totalLines) * 100;

        createAndShowPieChart(percentage);
    }

    private void createAndShowPieChart(double percentage) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Pointer/Reference Lines", percentage);
        dataset.setValue("Other Lines", 100 - percentage);

        JFreeChart chart = ChartFactory.createPieChart3D("Pointer and Reference Complexity", dataset, true, true, false);
        chart.setAntiAlias(true);  // Enable anti-aliasing for smoother rendering

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Pointer/Reference Lines", new Color(30, 144, 255)); // Dodger Blue
        plot.setSectionPaint("Other Lines", new Color(192, 192, 192)); // Silver
        plot.setBackgroundPaint(Color.WHITE);
        
        // Set a clear border for the pie sections
        plot.setSectionOutlinesVisible(true);
        plot.setSectionOutlinePaint("Pointer/Reference Lines", Color.WHITE);
        plot.setSectionOutlinePaint("Other Lines", Color.WHITE);
        plot.setSectionOutlineStroke("Pointer/Reference Lines", new BasicStroke(2.0f));
        plot.setSectionOutlineStroke("Other Lines", new BasicStroke(2.0f));

        // Adjust section label settings for better readability
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));
        plot.setLabelFont(plot.getLabelFont().deriveFont(Font.BOLD, 12f));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})", NumberFormat.getInstance(), NumberFormat.getPercentInstance()));
        
        // Apply font styling to the chart
     // Apply font styling to the chart
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));
        chart.getLegend().setItemFont(new Font("Arial", Font.PLAIN, 12));

        // Adjust legend alignment
        chart.getLegend().setPosition(RectangleEdge.BOTTOM);
        chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);

        chartPanel.setChart(chart);
        //chartPanel.setPreferredSize(new Dimension(200, 100));
        //chartPanel.setPreferredSize(new Dimension(200, 150)); // Adjust dimensions as needed
       // chartPanel.setPreferredSize(new Dimension(200, 150)); // Adjust dimensions as needed
        chartPanel.revalidate();
        chartPanel.repaint();

    }


    public int calculatePointerReferenceCount(String code) {
        // Simulate the calculation of pointer/reference complexity
        int pointerReferenceCount = 0;
        String[] lines = code.split("\\n");

        for (String line : lines) {
            if (line.contains("*")) {
                pointerReferenceCount++;
            }
        }

        return pointerReferenceCount;
    }
}
