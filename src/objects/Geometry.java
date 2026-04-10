package objects;

import java.awt.*;

public abstract class Geometry {
    public Color color;
    public boolean source;
    public int id;

    public Geometry(Color color, boolean source) {
        this.color = color;
        this.source = source;
    }

    public Geometry(Color color) {
        this.color = color;
        this.source = false;
    }

    public abstract boolean withinBounds(double x_pos, double y_pos);
}
