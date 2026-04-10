package objects;

import java.awt.*;

/**
 * Abstract base class for all geometric objects in the ray-tracer scene.
 *
 *  Every geometry carries a display {@link Color}, a flag indicating whether
 * it acts as a light-source emitter, and an integer identifier. Concrete
 * subclasses must implement the {@link #withinBounds} hit-test method used for
 * mouse picking. 
 */
public abstract class Geometry {

    /** The fill/stroke colour used when rendering this object. */
    public Color color;

    /**
     * {@code true} if this geometry represents a light source rather than a
     * passive target.
     */
    public boolean source;

    /** An optional integer identifier for this geometry (default {@code 0}). */
    public int id;

    /**
     * Constructs a {@code Geometry} with an explicit source flag.
     *
     * @param color  the display colour; must not be {@code null}
     * @param source {@code true} if this object is a light emitter
     */
    public Geometry(Color color, boolean source) {
        this.color = color;
        this.source = source;
    }

    /**
     * Constructs a {@code Geometry} that is  not  a light source
     * ({@code source} defaults to {@code false}).
     *
     * @param color the display colour; must not be {@code null}
     */
    public Geometry(Color color) {
        this.color = color;
        this.source = false;
    }

    /**
     * Tests whether the point {@code (x_pos, y_pos)} lies within or on the
     * boundary of this geometry.
     *
     *  Used for mouse-picking to determine whether the user has clicked on
     * this object. 
     *
     * @param x_pos the x-coordinate of the test point, in panel pixels
     * @param y_pos the y-coordinate of the test point, in panel pixels
     * @return {@code true} if the point is inside (or on) this geometry;
     *         {@code false} otherwise
     */
    public abstract boolean withinBounds(double x_pos, double y_pos);
}