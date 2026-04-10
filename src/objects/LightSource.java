package objects;

import raytracer.Ray;

/**
 * Represents a point light emitter that casts a fixed number of rays in all
 * directions.
 *
 *  A {@code LightSource} owns a {@link Circle} that defines its position and
 * visual appearance, and an array of {@link Ray} objects that fan out evenly
 * around a full 360° sweep. Call {@link #generateRays()} whenever the source
 * moves to reset the ray origins and directions. 
 */
public class LightSource {

    /** The circle that positions and visually represents this light source. */
    public final Circle circle;

    /**
     * The rays emitted by this light source. The array length equals
     * {@link #numRays} and is (re-)populated by {@link #generateRays()}.
     */
    public Ray[] rays;

    /** The total number of rays emitted by this light source. */
    public final int numRays;

    /**
     * Constructs a {@code LightSource} at the position described by
     * {@code circle} and immediately generates its initial set of rays.
     *
     * @param circle  the circle defining the source's position and colour;
     *                must not be {@code null}
     * @param numRays the number of rays to emit; must be greater than zero
     */
    public LightSource(Circle circle, int numRays) {
        this.circle = circle;
        this.numRays = numRays;
        this.rays = new Ray[numRays];
        generateRays();
    }

    /**
     * Regenerates all rays so that they radiate evenly from the centre of
     * {@link #circle} across a full 360° sweep.
     *
     *  Each ray is spaced {@code 2π / numRays} radians apart. All rays start
     * at the circle's centre with {@code settled = false}, ready to be
     * processed by {@link raytracer.RayTracer#solveSteps}. 
     */
    public void generateRays() {
        double baseAngle = (2 * Math.PI) / numRays;
        for (int i = 0; i < numRays; i++) {
            double angle = baseAngle * i;
            double x_direction = Math.cos(angle);
            double y_direction = Math.sin(angle);
            rays[i] = new Ray(circle.x_center, circle.y_center, x_direction, y_direction);
            rays[i].settled = false;
        }
    }
}