package gui;

import java.awt.*;
import javax.swing.*;
import objects.Circle;
import objects.LightSource;

public class Frame extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    public static  int NUM_RAYS = 300;
    private static final DrawPanel drawPanel = new DrawPanel();

    
    public Frame() {
        super("Ray Tracing");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(drawPanel);

        drawPanel.addSource(new LightSource(new Circle(100, 300, 20, Color.YELLOW, true), NUM_RAYS));
        drawPanel.addShapes(new Circle(600, 300, 50, Color.WHITE));
        // drawPanel.addShapes(new Circle(600, 150, 50, Color.WHITE));

        drawPanel.traceLoop();

    }
}
