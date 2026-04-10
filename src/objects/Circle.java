package objects;

import java.awt.*;

public class Circle extends Geometry{
    public double radius;
    public double x_center;
    public double y_center;


    public Circle(double x_center, double y_center, double radius, Color color, boolean source) {
        super(color, source);
        this.x_center = x_center;
        this.y_center = y_center;
        this.radius = radius;
    }

    public Circle(double x_center, double y_center, double radius, Color color) {
        super(color);
        this.x_center = x_center;
        this.y_center = y_center;
        this.radius = radius;
    }


    public boolean withinBounds(double x_pos, double y_pos){
        return Math.sqrt(Math.pow(x_pos - x_center, 2) + Math.pow(y_pos - y_center, 2)) <= radius;
    }
}
