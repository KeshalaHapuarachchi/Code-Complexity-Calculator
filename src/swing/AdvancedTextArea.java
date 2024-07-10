package swing;

import javax.swing.*;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;

public class AdvancedTextArea extends JPanel {

    private RSyntaxTextArea textArea;
    private AutoCompletion ac;

    public AdvancedTextArea() {
        setLayout(new BorderLayout());

        // Create the RSyntaxTextArea with syntax highlighting
        textArea = new RSyntaxTextArea(20, 20);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);

        // Enable code suggestions
        AutoCompletion ac = new AutoCompletion(createCompletionProvider());
        ac.install(textArea);

        // Create a scroll pane for the text area
        JScrollPane scrollPane = new RTextScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Add this constructor to accept rows and columns
    public AdvancedTextArea(int rows, int columns) {
        setLayout(new BorderLayout());

        // Create the RSyntaxTextArea with syntax highlighting
        textArea = new RSyntaxTextArea(rows, columns); // Use the provided rows and columns
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);

        // Enable code suggestions
        AutoCompletion ac = new AutoCompletion(createCompletionProvider());
        ac.install(textArea);

        // Create a scroll pane for the text area
        JScrollPane scrollPane = new RTextScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private CompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        // Add code suggestions here
        provider.addCompletion(new BasicCompletion(provider, "if"));
        provider.addCompletion(new BasicCompletion(provider, "for"));
        provider.addCompletion(new BasicCompletion(provider, "while"));
        provider.addCompletion(new BasicCompletion(provider, "while"));
        provider.addCompletion(new BasicCompletion(provider, "do"));
        provider.addCompletion(new BasicCompletion(provider, "switch"));

        return provider;
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}
