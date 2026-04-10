/**
 * Entry point for the Ray Tracer application.
 *
 * <p>Bootstraps the Swing GUI on the Event Dispatch Thread (EDT) to ensure
 * thread safety with all Swing components.</p>
 */
import gui.Frame;
import javax.swing.*;

public class Main {

    /**
     * Application entry point.
     *
     * <p>Uses {@link SwingUtilities#invokeLater} to schedule the creation and
     * display of the main {@link Frame} window on the EDT.</p>
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }
}