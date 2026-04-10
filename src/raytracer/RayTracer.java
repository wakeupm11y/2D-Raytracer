package raytracer;

import objects.Circle;

import gui.Frame;
import objects.Geometry;

import java.util.ArrayList;

public class RayTracer {
    private static final int MAX_X = Frame.WIDTH;
    private static final int MAX_Y = Frame.HEIGHT;

    public static void solveSteps(Ray[] rays, ArrayList<Geometry> circles) {
        for (Ray ray : rays) {
            // if already adjusted return
            if (ray.settled) continue;

            double t_min = Double.MAX_VALUE;

            for (Geometry targetCircle : circles) {
                // intermediate step
                double[] sub = subtract(ray.start_x, ray.start_y, ((Circle) targetCircle).x_center, ((Circle) targetCircle).y_center);
                // coefficients
                double a = dotProd(ray.x_direction, ray.y_direction, ray.x_direction, ray.y_direction);
                double b = 2 * dotProd(ray.x_direction, ray.y_direction, sub[0], sub[1]);
                double c = dotProd(sub[0], sub[1], sub[0], sub[1]) - ((Circle) targetCircle).radius * ((Circle) targetCircle).radius;

                double disc = solveDiscriminant(a, b, c);


                if (disc < 0) { // miss
                    solveTillBoundary(ray);
                } else if (disc >= 0) {
                    double sqrtDisc = Math.sqrt(disc);

                    double intersect1 = ((-b) - sqrtDisc)/(2 * a);
                    double intersect2 = ((-b) + sqrtDisc)/(2 * a);

                    // 1. Check t1: If t1 is non-negative, it is a valid hit.
                    if (intersect1 >= 0.0) {
                        t_min = intersect1;
                    }

                    // 2. Check t2: If t2 is non-negative AND is closer than t_min (this happens
                    //               if t1 was negative, i.e., ray origin is inside circle)
                    if (intersect2 >= 0.0 && intersect2 < t_min) {
                        t_min = intersect2;
                    }

                }
            }
            if (t_min == Double.MAX_VALUE) {
                // Ray missed or circle is completely behind the ray origin
                solveTillBoundary(ray);
            } else {
                // Intersection
                ray.step = t_min;
                ray.settled = true;
            }
            ray.solveEndPoints();
        }
    }



    private static double solveDiscriminant(double a, double b, double c) {
        // solve for the discriminant
        return (b * b) - (4 * a * c);
    }

    private static void solveTillBoundary(Ray ray) {
        double t_x = 0;
        double t_y = 0;

        if (ray.x_direction > 0.0) {
            t_x = (MAX_X - ray.start_x)/ray.x_direction;
        } else if (ray.x_direction < 0.0) {
            t_x = -ray.start_x/ray.x_direction;
        } else {
            t_x = Integer.MAX_VALUE; // represents infinity and avoid 0 division
        }

        if (ray.y_direction > 0.0) {
            t_y = (MAX_Y - ray.start_y)/ray.y_direction;
        } else if (ray.y_direction < 0.0) {
            t_y = -ray.start_y/ray.y_direction;
        } else {
            t_y = Integer.MAX_VALUE;
        }
        ray.step = Math.min(t_x, t_y);
    }

    private static double dotProd(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    private static double[] subtract(double x1, double y1, double x2, double y2) {
        return new double[]{x1 - x2, y1 - y2};
    }
}
