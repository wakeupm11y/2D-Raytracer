package raytracer;

/**
 * Represents a single ray used in the ray-tracing simulation.
 *
 *  A ray originates at {@code (start_x, start_y)}, travels in the direction
 * {@code (x_direction, y_direction)}, and terminates at the computed point
 * {@code (end_x, end_y)}. The scalar parameter {@code step} encodes the
 * distance along the ray at which it either intersects an object or reaches
 * the panel boundary. 
 *
 *  The {@code settled} flag is set to {@code true} once a final intersection
 * distance has been found, preventing redundant re-computation for rays that
 * have already been resolved. 
 */
public class Ray {

    /** X-coordinate of the ray's origin, in panel pixels. */
    public double start_x;

    /** Y-coordinate of the ray's origin, in panel pixels. */
    public double start_y;

    /**
     * X-component of the ray's unit direction vector.
     * Together with {@link #y_direction} this should be a normalised vector.
     */
    public double x_direction;

    /**
     * Y-component of the ray's unit direction vector.
     * Together with {@link #x_direction} this should be a normalised vector.
     */
    public double y_direction;

    /** X-coordinate of the ray's computed termination point. */
    public double end_x;

    /** Y-coordinate of the ray's computed termination point. */
    public double end_y;

    /**
     * The scalar distance parameter {@code t} such that the termination point
     * is {@code (start_x + t * x_direction, start_y + t * y_direction)}.
     */
    public double step;

    /**
     * {@code true} once a valid intersection (or boundary) distance has been
     * stored in {@link #step}; prevents the ray from being re-solved.
     */
    public boolean settled = false;

    /**
     * Constructs a {@code Ray} with an explicit initial step value.
     *
     * @param start_x     x-coordinate of the origin
     * @param start_y     y-coordinate of the origin
     * @param x_direction x-component of the direction vector
     * @param y_direction y-component of the direction vector
     * @param step        initial step parameter (may be {@code 0})
     */
    public Ray(double start_x, double start_y, double x_direction, double y_direction, double step) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.x_direction = x_direction;
        this.y_direction = y_direction;
        this.end_x = start_x;
        this.end_y = start_y;
        this.step = step;
    }

    /**
     * Constructs a {@code Ray} with an initial step of {@code 0}.
     * {@link #end_x} and {@link #end_y} are initialised to the origin.
     *
     * @param start_x     x-coordinate of the origin
     * @param start_y     y-coordinate of the origin
     * @param x_direction x-component of the direction vector
     * @param y_direction y-component of the direction vector
     */
    public Ray(double start_x, double start_y, double x_direction, double y_direction) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.x_direction = x_direction;
        this.y_direction = y_direction;
        this.end_x = start_x;
        this.end_y = start_y;
        this.step = 0;
    }

    /**
     * Computes and stores the ray's termination coordinates from the current
     * {@link #step} value.
     *
     *  Sets:
     *  
     *   end_x = start_x + step * x_direction
     *   end_y = start_y + step * y_direction
     *  
     * Must be called after {@link #step} has been assigned by
     * {@link RayTracer#solveSteps}. 
     */
    public void solveEndPoints() {
        this.end_x = start_x + step * x_direction;
        this.end_y = start_y + step * y_direction;
    }
}