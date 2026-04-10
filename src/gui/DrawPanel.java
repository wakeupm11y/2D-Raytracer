package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.*;
import objects.Circle;
import objects.Geometry;
import objects.LightSource;
import raytracer.Ray;
import raytracer.RayTracer;

/**
 * A {@link JPanel} that renders the ray-traced scene and handles interactive
 * dragging of light sources and target shapes.
 *
 *  The panel maintains two collections: {@code targets} (geometric objects
 * that rays can intersect) and {@code lightSources} (emitters that cast rays).
 * On each repaint the panel draws every light source, its associated rays, and
 * all target shapes. Mouse press-and-drag events allow the user to reposition
 * any object, triggering a fresh ray-trace pass after each movement step. 
 */
public class DrawPanel extends JPanel {

    /**
     * The current mouse position during a drag operation (updated continuously).
     * {@code null} when no drag is in progress.
     */
    private static Point MOUSE_POINT_F = null;

    /**
     * The mouse position at the start of the current drag operation.
     * {@code null} when no drag is in progress.
     */
    private static Point MOUSE_POINT_I = null;

    /**
     * The target {@link Geometry} shape currently being dragged, or
     * {@code null} if no shape is selected.
     */
    private static Geometry SELECTED_SHAPE = null;

    /**
     * The {@link LightSource} currently being dragged, or {@code null} if
     * none is selected.
     */
    private static LightSource SELECTED_SOURCE = null;

    /** The list of geometric target objects present in the scene. */
    private final ArrayList<Geometry> targets;

    /** The list of light sources present in the scene. */
    private final ArrayList<LightSource> lightSources;

    /**
     * Constructs a new {@code DrawPanel} with a black background and
     * registers mouse and mouse-motion listeners for interactive dragging.
     */
    public DrawPanel() {
        super();
        this.targets = new ArrayList<>();
        this.lightSources = new ArrayList<>();

        setBackground(Color.BLACK);
        mouseMotionListener();
        mouseListener();
    }

    /**
     * Adds a {@link LightSource} to the scene and schedules a repaint.
     *
     * @param lightSource the light source to add; must not be {@code null}
     */
    public void addSource(LightSource lightSource) {
        this.lightSources.add(lightSource);
        repaint();
    }

    /**
     * Adds a target {@link Geometry} shape to the scene and schedules a repaint.
     *
     * @param shape the geometric shape to add; must not be {@code null}
     */
    public void addShapes(Geometry shape) {
        this.targets.add(shape);
        repaint();
    }

    /**
     * Paints the scene by rendering all light sources (filled circles plus
     * their rays) followed by all target shapes.
     *
     *  Anti-aliasing is enabled and stroke width is set to 2 px. Each
     * {@link LightSource} is drawn in its own colour; rays are drawn as lines
     * from {@link Ray#start_x}/{@link Ray#start_y} to
     * {@link Ray#end_x}/{@link Ray#end_y}. Target {@link Circle} objects are
     * filled and outlined in their assigned colour. 
     *
     * @param g the {@link Graphics} context provided by Swing; cast internally
     *          to {@link Graphics2D}
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));

        for (LightSource lc : lightSources) {
            Shape sourceShape = new Ellipse2D.Double(
                    lc.circle.x_center - lc.circle.radius,
                    lc.circle.y_center - lc.circle.radius,
                    2 * lc.circle.radius,
                    2 * lc.circle.radius);
            g2d.setColor(lc.circle.color);
            g2d.fill(sourceShape);
            for (Ray ray : lc.rays) {
                g2d.drawLine((int) ray.start_x, (int) ray.start_y,
                             (int) ray.end_x,   (int) ray.end_y);
            }
            g2d.draw(sourceShape);
        }

        for (Geometry shape : targets) {
            if (shape instanceof Circle circle) {
                Shape c = new Ellipse2D.Double(
                        circle.x_center - circle.radius,
                        circle.y_center - circle.radius,
                        2 * circle.radius,
                        2 * circle.radius);
                g2d.setColor(circle.color);
                g2d.fill(c);
                g2d.draw(c);
            }
        }
    }

    /**
     * Registers a {@link MouseListener} that tracks press and release events.
     *
     *  On press: records the initial cursor position and selects whichever
     * light source or target shape lies under the cursor (light sources take
     * priority). On release: clears all selection and drag state. 
     */
    public void mouseListener() {
        addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent mouseEvent) {}

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                MOUSE_POINT_I = mouseEvent.getPoint();
                for (LightSource ls : lightSources) {
                    if (ls.circle.withinBounds(MOUSE_POINT_I.x, MOUSE_POINT_I.y)) {
                        SELECTED_SOURCE = ls;
                        break;
                    }
                }
                for (Geometry shape : targets) {
                    if (shape.withinBounds(MOUSE_POINT_I.x, MOUSE_POINT_I.y)) {
                        SELECTED_SHAPE = shape;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                MOUSE_POINT_I = null;
                MOUSE_POINT_F = null;
                SELECTED_SHAPE = null;
                SELECTED_SOURCE = null;
            }

            @Override public void mouseEntered(MouseEvent mouseEvent) {}
            @Override public void mouseExited(MouseEvent mouseEvent) {}
        });
    }

    /**
     * Registers a {@link MouseMotionListener} that moves the selected object
     * during drag operations.
     *
     *  On each drag event the displacement from the previous mouse position
     * is applied to the selected shape's or light source's centre coordinates.
     * A bounds check ({@link #checkBounds}) prevents objects from being moved
     * more than 200 px beyond any panel edge. After each move,
     * {@link #traceLoop()} is called to recompute all ray intersections and
     * {@link #repaint()} refreshes the display. 
     */
    public void mouseMotionListener() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                MOUSE_POINT_F = mouseEvent.getPoint();
                double dispX = MOUSE_POINT_F.x - MOUSE_POINT_I.x;
                double dispY = MOUSE_POINT_F.y - MOUSE_POINT_I.y;

                if (SELECTED_SHAPE != null) {
                    if (checkBounds(SELECTED_SHAPE)) {
                        if (SELECTED_SHAPE instanceof Circle) {
                            ((Circle) SELECTED_SHAPE).x_center += dispX;
                            ((Circle) SELECTED_SHAPE).y_center += dispY;
                        }
                        assert SELECTED_SHAPE instanceof Circle;
                        traceLoop();
                    }
                } else if (SELECTED_SOURCE != null) {
                    if (checkBounds(SELECTED_SOURCE.circle)) {
                        SELECTED_SOURCE.circle.x_center += dispX;
                        SELECTED_SOURCE.circle.y_center += dispY;
                        traceLoop();
                    }
                }
                MOUSE_POINT_I = MOUSE_POINT_F;
                repaint();
            }

            @Override public void mouseMoved(MouseEvent e) {}
        });
    }

    /**
     * Returns {@code true} if the given shape is within the permitted drag
     * area, i.e. its bounding circle does not exceed 200 px beyond any edge
     * of the panel.
     *
     * @param shape the {@link Geometry} to test; currently only {@link Circle}
     *              instances are supported
     * @return {@code true} if the shape may continue to be dragged;
     *         {@code false} otherwise
     */
    private boolean checkBounds(Geometry shape) {
        if (shape instanceof Circle circle) {
            if (circle.x_center + circle.radius >= getWidth()  + 200) return false;
            if (circle.y_center + circle.radius >= getHeight() + 200) return false;
            if (circle.x_center - circle.radius <= 0 - 200)           return false;
            if (circle.y_center - circle.radius <= 0 - 200)           return false;
            return true;
        }
        return false;
    }

    /**
     * Regenerates all rays from every light source and then recomputes ray
     * intersections against all target shapes.
     *
     *  This method should be called whenever any object in the scene moves.
     * Note: the current implementation assumes a single target object; results
     * may be incorrect when multiple targets are present. 
     */
    public void traceLoop() {
        adjustRays();
        for (LightSource ls : lightSources) {
            RayTracer.solveSteps(ls.rays, targets);
        }
    }

    /**
     * Regenerates the ray arrays for all light sources, resetting each ray to
     * originate from its source's current centre position.
     */
    private void adjustRays() {
        for (LightSource ls : lightSources) {
            ls.generateRays();
        }
    }
}