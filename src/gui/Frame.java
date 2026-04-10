package gui;

import java.awt.*;
import javax.swing.*;
import objects.Circle;
import objects.LightSource;

/**
 * The main application window for the Ray Tracer.
 *
 * <p>Sets up a fixed-size {@link JFrame} containing a {@link DrawPanel}, then
 * populates the scene with a default {@link LightSource} and one or more
 * target {@link Circle} shapes before triggering an initial ray-trace pass.</p>
 */
public class Frame extends JFrame {

    /** The width of the application window in pixels. */
    public static final int WIDTH = 1000;

    /** The height of the application window in pixels. */
    public static final int HEIGHT = 600;

    /** The number of rays emitted by each {@link LightSource}. */
    public static int NUM_RAYS = 300;

    /** The single shared drawing surface for the scene. */
    private static final DrawPanel drawPanel = new DrawPanel();

    /**
     * Constructs and configures the main application window.
     *
     * <p>Initialises the frame properties, adds the {@link DrawPanel},
     * places a yellow {@link LightSource} at (100, 300) and a white target
     * {@link Circle} at (600, 300), then performs an initial
     * {@link DrawPanel#traceLoop()} to compute ray intersections.</p>
     */
    public Frame() {
        super("Ray Tracing");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(drawPanel);

        drawPanel.addSource(new LightSource(new Circle(100, 300, 20, Color.YELLOW, true), NUM_RAYS));
        drawPanel.addShapes(new Circle(600, 300, 50, Color.WHITE));

        drawPanel.traceLoop();
    }
}
