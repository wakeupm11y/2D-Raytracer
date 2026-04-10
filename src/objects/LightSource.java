package objects;

import raytracer.Ray;

import java.awt.*;

public class LightSource {
    public final Circle circle;
    public Ray[] rays;
    public final int numRays;

    public LightSource(Circle circle, int numRays) {
        this.circle = circle;
        this.numRays = numRays;
        this.rays = new Ray[numRays];
        generateRays();
    }

    public void generateRays() {
        double baseAngle = (2 * Math.PI) / numRays;
        for (int i = 0; i < numRays; i++) {
            // we want to generate rays from the center
            // start with getting the directions
            double angle = baseAngle * i;
            double x_direction = Math.cos(angle);
            double y_direction = Math.sin(angle);

            // get the rays around the circle
            rays[i] = new Ray(circle.x_center, circle.y_center, x_direction, y_direction);
            rays[i].settled = false;
        }
    }
}
