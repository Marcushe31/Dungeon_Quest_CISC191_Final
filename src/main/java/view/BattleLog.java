package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import util.ColorPalette;

/**
 * Styled scrolling battle log that keeps the most recent actions visible.
 */
public class BattleLog extends JPanel {
    private static final int MAX_VISIBLE_LINES = 6;
    private static final int FADE_DELAY = 25;

    private final JTextArea textArea;
    private float alpha;

    public BattleLog() {
        super(new BorderLayout());
        alpha = 1.0f;
        textArea = new JTextArea(MAX_VISIBLE_LINES, 32);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(ColorPalette.FONT_FAMILY, Font.PLAIN, 12));
        textArea.setBackground(ColorPalette.PANEL_OVERLAY);
        textArea.setForeground(ColorPalette.TEXT_PRIMARY);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_GOLD, 2),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        scrollPane.getViewport().setBackground(ColorPalette.PANEL_OVERLAY);
        add(scrollPane, BorderLayout.CENTER);
        setOpaque(false);
    }

    /**
     * Replaces the log entries and starts a small fade-in.
     *
     * @param entries battle entries
     */
    public void setEntries(List<String> entries) {
        if (entries == null || entries.isEmpty()) {
            textArea.setText("");
            return;
        }
        int start = Math.max(0, entries.size() - MAX_VISIBLE_LINES);
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < entries.size(); i++) {
            builder.append("> ").append(entries.get(i)).append(System.lineSeparator());
        }
        textArea.setText(builder.toString());
        textArea.setCaretPosition(textArea.getDocument().getLength());
        fadeIn();
    }

    private void fadeIn() {
        alpha = 0.35f;
        Timer timer = new Timer(FADE_DELAY, null);
        timer.addActionListener(event -> {
            alpha = Math.min(1.0f, alpha + 0.08f);
            textArea.setForeground(withAlpha(ColorPalette.TEXT_PRIMARY, alpha));
            if (alpha >= 1.0f) {
                timer.stop();
            }
        });
        timer.start();
    }

    private java.awt.Color withAlpha(java.awt.Color color, float alphaValue) {
        return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(alphaValue * 255));
    }
}
