import controller.GameManager;
import javax.swing.SwingUtilities;

/**
 * Application entry point.
 */
public class Main {
    /**
     * Starts the Swing app on the EDT.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameManager().show());
    }
}
