//package swing;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PiePlot;
//import org.jfree.data.general.DefaultPieDataset;
//
//public class PointerReferenceCalculatorApp extends JFrame {
//    private JTextArea codeInputArea;
//    private JButton calculateButton;
//    private JPanel resultChartPanel;  // New panel for chart results
//
//    public PointerReferenceCalculatorApp() {
//        setTitle("Pointer and Reference Complexity Calculator");
//        setSize(800, 600); // Adjust window size as needed
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        codeInputArea = new JTextArea(15, 50);  // Adjust input area size as needed
//        JScrollPane scrollPane = new JScrollPane(codeInputArea);
//        mainPanel.add(scrollPane, BorderLayout.NORTH);
//
//        calculateButton = new JButton("Calculate Pointer and Reference Complexity");
//        calculateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String userCode = codeInputArea.getText();
//                int totalLines = userCode.split("\n").length;
//                int numPointersReferences = calculatePointerReferenceCount(userCode);
//                double percentage = ((double) numPointersReferences / totalLines) * 100;
//
//                createChartPanel(percentage);
//
//                // Update the chart results panel with the new chart
//                resultChartPanel.removeAll();
//                resultChartPanel.add(createChartPanel(percentage), BorderLayout.CENTER);
//                resultChartPanel.revalidate();
//                resultChartPanel.repaint();
//            }
//        });
//
//        resultChartPanel = new JPanel(new BorderLayout());  // Initialize the resultChartPanel
//        mainPanel.add(calculateButton, BorderLayout.SOUTH);
//        mainPanel.add(resultChartPanel, BorderLayout.CENTER);  // Add the resultChartPanel to the mainPanel
//
//        add(mainPanel);
//    }
//
//    private JPanel createChartPanel(double percentage) {
//        DefaultPieDataset dataset = new DefaultPieDataset();
//        dataset.setValue("Pointer/Reference Lines", percentage);
//        dataset.setValue("Other Lines", 100 - percentage);
//
//        JFreeChart chart = ChartFactory.createPieChart3D("Pointer and Reference Lines", dataset, true, true, false);
//        PiePlot plot = (PiePlot) chart.getPlot();
//        plot.setSectionPaint("Pointer/Reference Lines", Color.BLUE);
//        plot.setSectionPaint("Other Lines", Color.GRAY);
//
//        ChartPanel chartPanel = new ChartPanel(chart);
//        return chartPanel;
//    }
//
//
//    public static int calculatePointerReferenceCount(String code) {
//        // Simulate the calculation of pointer/reference complexity
//        int pointerReferenceCount = 0;
//        String[] lines = code.split("\\n");
//
//        for (String line : lines) {
//            if (line.contains("*")) {
//                pointerReferenceCount++;
//            }
//        }
//
//        return pointerReferenceCount;
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new PointerReferenceCalculatorApp().setVisible(true);
//            }
//        });
//    }
//}
