package gui;

import objects.Circle;
import objects.Geometry;
import objects.LightSource;
import raytracer.Ray;
import raytracer.RayTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class DrawPanel extends JPanel {

    private static Point MOUSE_POINT_F = null;
    private static Point MOUSE_POINT_I = null;
    private static Geometry SELECTED_SHAPE = null;
    private static LightSource SELECTED_SOURCE = null;


    private final ArrayList<Geometry> targets;
    private final ArrayList<LightSource> lightSources;

    public DrawPanel() {
        super();
        this.targets = new ArrayList<>();
        this.lightSources = new ArrayList<>();

        setBackground(Color.BLACK);
        mouseMotionListener();
        mouseListener();
    }

    public void addSource(LightSource lightSource) {
        this.lightSources.add(lightSource);
        repaint();
    }

    public void addShapes(Geometry shape) {
        this.targets.add(shape);
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));


        for (LightSource lc : lightSources) {
            Shape sourceShape = new Ellipse2D.Double(lc.circle.x_center - lc.circle.radius, lc.circle.y_center - lc.circle.radius, 2 * lc.circle.radius, 2 * lc.circle.radius);
            g2d.setColor(lc.circle.color);
            g2d.fill(sourceShape);
            for (Ray ray: lc.rays) {
                g2d.drawLine((int) ray.start_x, (int) ray.start_y,(int) ray.end_x,(int) ray.end_y);
            }
            g2d.draw(sourceShape);
        }

        for  (Geometry shape : targets) {
            if (shape instanceof Circle circle) {
                Shape c = new Ellipse2D.Double(circle.x_center - circle.radius, circle.y_center - circle.radius, 2 * circle.radius, 2 * circle.radius);
                g2d.setColor(circle.color);
                g2d.fill(c);
                g2d.draw(c);
            }
        }
    }

    public void mouseListener() {
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                MOUSE_POINT_I = mouseEvent.getPoint();
                for (LightSource ls : lightSources) {
                    if (ls.circle.withinBounds(MOUSE_POINT_I.x, MOUSE_POINT_I.y)){
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

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    public void mouseMotionListener() {
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                MOUSE_POINT_F = mouseEvent.getPoint();
                // displacement of the mouse from
                double dispX = MOUSE_POINT_F.x - MOUSE_POINT_I.x;
                double dispY = MOUSE_POINT_F.y - MOUSE_POINT_I.y;

                if (SELECTED_SHAPE != null) {
                    if (checkBounds(SELECTED_SHAPE)) {
                        if (SELECTED_SHAPE instanceof Circle ) {
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

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }


    private boolean checkBounds(Geometry shape) {
        if (shape instanceof Circle circle) {
            if (circle.x_center + circle.radius >= getWidth() + 200) {
                return false;
            }
            if (circle.y_center + circle.radius >= getHeight() + 200) {
                return false;
            }
            if (circle.x_center - circle.radius <= 0 - 200) {
                return false;
            }
            if (circle.y_center - circle.radius <= 0 - 200) {
                return false;
            }
            return true;
        }
        return false;
    }

    // solve steps won't work if we have multiple targets
    public void traceLoop() {
        adjustRays();
        for (LightSource ls : lightSources) {
            RayTracer.solveSteps(ls.rays, targets);
        }
    }

    private void adjustRays () {
        for (LightSource ls : lightSources) {
            ls.generateRays();
        }
    }


}
