package raytracer;

public class Ray {

    public double start_x, start_y;
    public double x_direction, y_direction;
    public double end_x, end_y;
    public double step;
    public boolean settled = false;

    public Ray(double start_x, double start_y, double x_direction, double y_direction, double step) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.x_direction = x_direction;
        this.y_direction = y_direction;
        this.end_x = start_x;
        this.end_y = start_y;
        this.step = step;
    }

    public Ray(double start_x, double start_y, double x_direction, double y_direction) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.x_direction = x_direction;
        this.y_direction = y_direction;
        this.end_x = start_x;
        this.end_y = start_y;
        this.step = 0;
    }

    public void solveEndPoints() {
         this.end_x = start_x + step * x_direction;
         this.end_y = start_y + step * y_direction;
    }
}
