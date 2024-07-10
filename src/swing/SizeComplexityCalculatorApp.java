package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class SizeComplexityCalculatorApp extends JPanel implements ComplexityCalculator {

    boolean classContainsThreadsOrRunnable = false;

    public SizeComplexityCalculatorApp() {
        setLayout(new BorderLayout());
    }

    @Override
    public void calculateAndShowComplexity(String javaCode) {
        javaCode = adjustCodeAfterClassDeclaration(javaCode);

        DefaultTableModel tableModel = createTableModel(javaCode);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Adjust dimensions as needed

        removeAll();
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static String adjustCodeAfterClassDeclaration(String javaCode) {
        int classDeclarationIndex = javaCode.indexOf("class");

        if (classDeclarationIndex != -1) {
            return javaCode.substring(classDeclarationIndex);
        } else {
            return ""; // Return an empty string if no class declaration is found
        }
    }

    boolean containsThreadsOrRunnable = false;

    public DefaultTableModel createTableModel(String javaCode) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Line Number", "S"}, 0);
        String[] lines = javaCode.split("\n");

        for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
            String line = lines[lineNumber].trim();

            int size = calculateSize(line);

            // Check if the line is a class declaration
            if (line.startsWith("class ")) {
                size = -1; // Set size complexity to zero for class declaration lines
            } else if (line.equals("}")) {
                size = -1;
            } else if (line.equals("{")) {
                size = -1;
            } else if (line.trim().isEmpty()) {
                size = -1; // Return 0 for empty lines
            }
            if (line.equals("else")) {
                size = -1; // Return 0 for empty lines
            }

            if (line.matches("^(\\s*}\\s*else\\s*\\{\\s*|\\s*else\\s*\\{\\s*|\\s*else\\s*\\s*\\{\\s*|\\s*}\\s*else\\s*\\s*\\{\\s*)$")) {
                size = -1;
            }

            tableModel.addRow(new Object[]{"Line " + (lineNumber + 1), size >= 0 ? size : ""});
        }

        // Set the classContainsThreadsOrRunnable flag if necessary
        setClassContainsThreadsOrRunnable(containsThreadsOrRunnable);

        return tableModel;
    }

    public static int calculateSize(String statement) {
        statement = statement.replaceAll("\"(?:\\\\.|[^\\\\\"])*\"|'(?:\\\\.|[^\\\\'])+'", "")
                .replaceAll("\\[(.*?)\\]", "")
                .replaceAll("\\s*\\{", "")
                .replaceAll("[{}]", "")
                .replaceAll("else\\s*\\{", "else{")
                .replaceAll("else\\\\s*if", "else-if");

        int size = 0;
        boolean success = false;

        String objectThread = "(\\w+)\\s+(\\w+)\\s*=\\s*new\\s+(\\w+)\\(";
        String classThread = "\\bclass\\s+(\\w+)\\s+extends\\s+Thread\\b";

        if (statement.matches(objectThread)) {
            size = 10;
            success = true;
        }

        // Check if the line is a class declaration
        if (statement.contains("class")) {
            return 0; // Set size complexity to zero for lines containing "class"
        }

        // Count strings (characters inside quotes as a single token)
        String stringsPattern = "\"(?:\\\\.|[^\\\\\"])*\"|'(?:\\\\.|[^\\\\'])+'";
        size += countMatches(statement, stringsPattern);

        // Count identifiers and numerical values
        String identifiersAndNumericalsPattern = "\\b[a-zA-Z_]\\w*\\b|\\b-?\\d+(\\.\\d+)?(?!\\s*\\()|(?<=\\s)-\\d+\\b";
        size += countMatches(statement, identifiersAndNumericalsPattern);

        // Count operators (including <=, >=, commas, and '.')
        String operatorsPattern = "[+\\*/%=<>,.]";
        size += countMatches(statement, operatorsPattern);

        //consider minus operator
        String minusOperator = "\\s-\\s";
        size += countMatches(statement, minusOperator);

        // Count other exceptional cases
        String exceptionalCasesPattern = "\\b(end|goto)\\b";
        size += countMatches(statement, exceptionalCasesPattern);

        // Remove '*' notations used for pointer declaration
        statement = statement.replaceAll("\\*", "");

        // Count array names and square brackets as one token
        String arrayPattern = "\\b[a-zA-Z_]\\w*\\s*\\[\\s*\\]";
        size += countMatches(statement, arrayPattern);

        // Count manipulators as tokens
        String manipulatorsPattern = "\\b(endl|\"\\\\n\")\\b";
        size += countMatches(statement, manipulatorsPattern);

        // Exclude 'return' keyword
        if (statement.equals("return")) {
            size--;
        }

        // Count 'case :' and 'default :' tokens as separate tokens
        String caseDefaultPattern = "\\b(case|default)\\s*:";
        size += countMatches(statement, caseDefaultPattern);

        // Exclude components inside round brackets for decisional statements
        String componentsInsideDecisionalPattern = "\\b(if|else|for|while|switch)\\s*\\(.*\\)";
        if (statement.matches(componentsInsideDecisionalPattern)) {
            size -= countMatches(statement, "(?:if|else|for|while|switch)\\s*\\(\\s*\\)\\s*");
        }

        // Count decisional keywords
        String decisionalPattern = "\\b(else-if|if|else|for|while|switch)\\s*\\(.*\\)";
        if (statement.matches(decisionalPattern)) {
            size += countMatches(statement, decisionalPattern); // Count tokens inside parentheses
        }

        // Exclude statements only containing 'else' or 'do'
        if (statement.equals("else") || statement.equals("do")) {
            size = 0;
        }

        // Count 'catch' keyword and round brackets as one token
        String catchPattern = "\\bcatch\\s*\\(.*\\)";
        if (statement.matches(catchPattern)) {
            size += 1; // Count 'catch' keyword and round brackets as one token
        }

        // Exclude statements only containing 'try'
        if (statement.equals("try")) {
            size = 0;
        }

        // Count '.' operator and connected names as separate tokens
        String dotOperatorPattern = "\\b[a-zA-Z_]\\w*\\s*\\.\\s*[a-zA-Z_]\\w*";
        size += countMatches(statement, dotOperatorPattern);

        // Exclude opening curly brace '{'
        if (statement.equals("{")) {
            size = 0;
        }

        // Exclude keywords 'public', 'private', 'default', 'static', 'protected'
        String keywordsPattern = "\\b(public|private|default|static|protected|final|class|int|double|float|char|boolean|long|short|byte|String)\\b";
        size -= countMatches(statement, keywordsPattern);

        String returnPattern = "\\breturn\\b";
        size -= countMatches(statement, returnPattern);

        // Exclude round bracket and commas
        String roundBracketNumberAndCommas = "\\(\\s*(?:\\b\\w+\\b|\\d+)(?:\\s*,\\s*(?:\\b\\w+\\b|\\d+))*\\s*\\)";
        size -= countMatches(statement, roundBracketNumberAndCommas);

        String dataTypePattern = "\\(\\s*\\b\\w+\\s+\\w+\\b\\s*\\)";
        size -= countMatches(statement, dataTypePattern);

        if (statement.contains("start()")) {
            size += 1;
        }

        if (statement.contains("Thread()")) {
            size += 2;
        }

        if (statement.matches(classThread)) {
            size += 10;
            success = true;
        }
        if (statement.matches(objectThread) && success == true) {
            size += 2;
        }

        String methodPattern = "\\b(\\w+)\\s+(\\w+)\\s*\\(([^)]*)\\)";

        Matcher matcher = Pattern.compile(methodPattern).matcher(statement);

        if (matcher.matches()) {
            String methodName = matcher.group(1);
            String parameters = matcher.group(2);
            String[] parameterTokens = parameters.split("\\s*,\\s*");
            for (String parameter : parameterTokens) {
                String[] parts = parameter.split("\\s+");
                if (parts.length >= 2) {
                    String variableName = parts[parts.length - 1];
                    String variableNameToCompare = variableName;
                    String comparePattern = "\\b(" + variableNameToCompare + ")\\s*==\\s*0\\b";
                }
            }
        }

        return size;
    }

    // Setter method to set the classContainsThreadsOrRunnable flag
    private void setClassContainsThreadsOrRunnable(boolean flag) {
        classContainsThreadsOrRunnable = flag;
    }

    public static int countMatches(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

}
