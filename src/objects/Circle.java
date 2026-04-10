package objects;

import java.awt.*;

/**
 * A circular geometry defined by its centre coordinates and radius.
 *
 *  Used both as a passive ray-intersection target and, when wrapped in a
 * {@link LightSource}, as the visual representation of a light emitter. 
 */
public class Circle extends Geometry {

    /** The radius of the circle in pixels. */
    public double radius;

    /** The x-coordinate of the circle's centre in panel pixels. */
    public double x_center;

    /** The y-coordinate of the circle's centre in panel pixels. */
    public double y_center;

    /**
     * Constructs a {@code Circle} with an explicit source flag.
     *
     * @param x_center the x-coordinate of the centre
     * @param y_center the y-coordinate of the centre
     * @param radius   the radius in pixels; must be positive
     * @param color    the display colour; must not be {@code null}
     * @param source   {@code true} if this circle represents a light emitter
     */
    public Circle(double x_center, double y_center, double radius, Color color, boolean source) {
        super(color, source);
        this.x_center = x_center;
        this.y_center = y_center;
        this.radius = radius;
    }

    /**
     * Constructs a {@code Circle} that is  not  a light source.
     *
     * @param x_center the x-coordinate of the centre
     * @param y_center the y-coordinate of the centre
     * @param radius   the radius in pixels; must be positive
     * @param color    the display colour; must not be {@code null}
     */
    public Circle(double x_center, double y_center, double radius, Color color) {
        super(color);
        this.x_center = x_center;
        this.y_center = y_center;
        this.radius = radius;
    }

    /**
     * Returns {@code true} if the point {@code (x_pos, y_pos)} lies within or
     * on the circumference of this circle.
     *
     *  Uses the Euclidean distance from the test point to the centre:
     * {@code sqrt((x_pos - x_center)² + (y_pos - y_center)²) ≤ radius}. 
     *
     * @param x_pos the x-coordinate of the test point
     * @param y_pos the y-coordinate of the test point
     * @return {@code true} if the point is inside or on the circle
     */
    @Override
    public boolean withinBounds(double x_pos, double y_pos) {
        return Math.sqrt(Math.pow(x_pos - x_center, 2) + Math.pow(y_pos - y_center, 2)) <= radius;
    }
}