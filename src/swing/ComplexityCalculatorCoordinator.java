package swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComplexityCalculatorCoordinator extends JFrame {
    private AdvancedTextArea codeInputArea;
    private JButton calculateButton;
    private ComplexityCalculator pointerReferenceApp;
    private ComplexityCalculator threadComplexityApp;
    
    private ComplexityCalculator sizeComplexityApp;
    private ComplexityCalculator structureComplexityApp;
    private ComplexityCalculator nestingComplexityApp;
    private ComplexityCalculator inheritanceComplexityApp;

    public ComplexityCalculatorCoordinator() {
        setTitle("Complexity Calculator Coordinator");
        setSize(1200, 830); // Adjust window size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        codeInputArea = new AdvancedTextArea(25, 60);  // Adjust input area size as needed
        JScrollPane scrollPane = new JScrollPane(codeInputArea);
        mainPanel.add(scrollPane, BorderLayout.NORTH);

        calculateButton = new JButton("Calculate Complexity");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userCode = codeInputArea.getText();

                // Calculate and update the complexities in the respective apps
                pointerReferenceApp.calculateAndShowComplexity(userCode);
                threadComplexityApp.calculateAndShowComplexity(userCode);
                
                sizeComplexityApp.calculateAndShowComplexity(userCode);
                structureComplexityApp.calculateAndShowComplexity(userCode);
                nestingComplexityApp.calculateAndShowComplexity(userCode);
                inheritanceComplexityApp.calculateAndShowComplexity(userCode);
            }
        });

        JPanel appPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        pointerReferenceApp = new PointerReferenceCalculatorApp();
        threadComplexityApp = new ThreadComplexityCalculatorApp();
        
        sizeComplexityApp = new SizeComplexityCalculatorApp();
        structureComplexityApp = new ControlStructureWeightCalculator();
        nestingComplexityApp = new ControlStructureNestingLevelCalculator();
        inheritanceComplexityApp = new InheritanceLevelCalculator();

        appPanel.add((JPanel) pointerReferenceApp); // Cast to JPanel if necessary
        appPanel.add((JPanel) threadComplexityApp); // Cast to JPanel if necessary
        
        appPanel.add((JPanel) sizeComplexityApp); // Cast to JPanel if necessary
        appPanel.add((JPanel) structureComplexityApp); // Cast to JPanel if necessary
        appPanel.add((JPanel) nestingComplexityApp); // Cast to JPanel if necessary
        appPanel.add((JPanel) inheritanceComplexityApp); // Cast to JPanel if necessary

        mainPanel.add(appPanel, BorderLayout.CENTER);
        mainPanel.add(calculateButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ComplexityCalculatorCoordinator().setVisible(true);
            }
        });
    }
}
