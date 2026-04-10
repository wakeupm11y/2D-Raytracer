package raytracer;

import gui.Frame;
import java.util.ArrayList;
import objects.Circle;
import objects.Geometry;

/**
 * Stateless utility class that performs analytic ray–circle intersection tests
 * for every ray in a light source.
 *
 * For each unsettled {@link Ray}, {@link #solveSteps} tests it against every
 * target {@link Circle} using the standard quadratic ray–sphere formula. The
 * smallest non-negative intersection parameter {@code t} is chosen; if no
 * intersection exists the ray is extended to the nearest panel boundary. 
 *
 *  All methods are {@code static}; this class is not intended to be
 * instantiated. 
 */
public class RayTracer {

    /** The right-hand panel boundary (x), sourced from {@link Frame#WIDTH}. */
    private static final int MAX_X = Frame.WIDTH;

    /** The bottom panel boundary (y), sourced from {@link Frame#HEIGHT}. */
    private static final int MAX_Y = Frame.HEIGHT;

    /**
     * Computes the termination parameter {@link Ray#step} for each ray in
     * {@code rays} by testing against all circles in {@code circles}.
     *
     *  Algorithm for each ray:
     *  
     *    Skip rays that are already {@link Ray#settled}. 
     *    For each target circle, build the quadratic
     *       {@code at² + bt + c = 0} where the coefficients encode the
     *       ray–origin-to-circle-centre offset. 
     *    Evaluate the discriminant: negative → miss; non-negative →
     *       compute both roots and keep the smallest non-negative one. 
     *    After all circles: if no valid {@code t} was found, extend the
     *       ray to the panel boundary via {@link #solveTillBoundary};
     *       otherwise store the hit distance and mark the ray settled. 
     *    Call {@link Ray#solveEndPoints()} to materialise
     *       {@link Ray#end_x}/{@link Ray#end_y}. 
     *  
     *  
     *
     * @param rays    the array of rays to process; rays with {@code settled ==
     *                true} are skipped
     * @param circles the list of target geometries; all elements are expected
     *                to be {@link Circle} instances
     */
    public static void solveSteps(Ray[] rays, ArrayList<Geometry> circles) {
        for (Ray ray : rays) {
            if (ray.settled) continue;

            double t_min = Double.MAX_VALUE;

            for (Geometry targetCircle : circles) {
                double[] sub = subtract(ray.start_x, ray.start_y,
                        ((Circle) targetCircle).x_center,
                        ((Circle) targetCircle).y_center);

                double a = dotProd(ray.x_direction, ray.y_direction,
                                ray.x_direction, ray.y_direction);
                double b = 2 * dotProd(ray.x_direction, ray.y_direction, sub[0], sub[1]);
                double c = dotProd(sub[0], sub[1], sub[0], sub[1])
                         - ((Circle) targetCircle).radius * ((Circle) targetCircle).radius;

                double disc = solveDiscriminant(a, b, c);

                if (disc < 0) {
                    solveTillBoundary(ray);
                } else {
                    double sqrtDisc = Math.sqrt(disc);
                    double intersect1 = ((-b) - sqrtDisc) / (2 * a);
                    double intersect2 = ((-b) + sqrtDisc) / (2 * a);

                    if (intersect1 >= 0.0) {
                        t_min = intersect1;
                    }
                    if (intersect2 >= 0.0 && intersect2 < t_min) {
                        t_min = intersect2;
                    }
                }
            }

            if (t_min == Double.MAX_VALUE) {
                solveTillBoundary(ray);
            } else {
                ray.step = t_min;
                ray.settled = true;
            }
            ray.solveEndPoints();
        }
    }

    /**
     * Computes the discriminant {@code b² - 4ac} of a quadratic equation.
     *
     *  Used to determine whether a ray intersects a circle:
     *  
     *    Negative → no real intersection (miss). 
     *    Zero     → one tangent intersection. 
     *    Positive → two distinct intersections. 
     *  
     *  
     *
     * @param a coefficient of the quadratic term
     * @param b coefficient of the linear term
     * @param c constant term
     * @return the value of {@code b² - 4ac}
     */
    private static double solveDiscriminant(double a, double b, double c) {
        return (b * b) - (4 * a * c);
    }

    /**
     * Extends {@code ray} to the nearest panel boundary when no circle
     * intersection exists.
     *
     *  Computes the {@code t} value at which the ray exits the rectangle
     * {@code [0, MAX_X] × [0, MAX_Y]} separately for each axis and takes the
     * minimum. A direction component of zero is treated as "never reaches that
     * boundary" (represented by {@link Integer#MAX_VALUE}). 
     *
     * @param ray the ray whose {@link Ray#step} is to be set; modified in-place
     */
    private static void solveTillBoundary(Ray ray) {
        double t_x;
        double t_y;

        if (ray.x_direction > 0.0) {
            t_x = (MAX_X - ray.start_x) / ray.x_direction;
        } else if (ray.x_direction < 0.0) {
            t_x = -ray.start_x / ray.x_direction;
        } else {
            t_x = Integer.MAX_VALUE;
        }

        if (ray.y_direction > 0.0) {
            t_y = (MAX_Y - ray.start_y) / ray.y_direction;
        } else if (ray.y_direction < 0.0) {
            t_y = -ray.start_y / ray.y_direction;
        } else {
            t_y = Integer.MAX_VALUE;
        }

        ray.step = Math.min(t_x, t_y);
    }

    /**
     * Computes the 2D dot product of vectors {@code (x1, y1)} and
     * {@code (x2, y2)}.
     *
     * @param x1 x-component of the first vector
     * @param y1 y-component of the first vector
     * @param x2 x-component of the second vector
     * @param y2 y-component of the second vector
     * @return {@code x1*x2 + y1*y2}
     */
    private static double dotProd(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    /**
     * Computes the component-wise subtraction of point {@code (x2, y2)} from
     * point {@code (x1, y1)}.
     *
     * @param x1 x-coordinate of the minuend point
     * @param y1 y-coordinate of the minuend point
     * @param x2 x-coordinate of the subtrahend point
     * @param y2 y-coordinate of the subtrahend point
     * @return a two-element array {@code {x1 - x2, y1 - y2}}
     */
    private static double[] subtract(double x1, double y1, double x2, double y2) {
        return new double[]{x1 - x2, y1 - y2};
    }
}