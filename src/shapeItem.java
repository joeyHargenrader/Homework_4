import java.awt.*;
import java.awt.geom.Rectangle2D;

public class shapeItem {
    private Shape shape;
    private Color color;
    int startY;
    int vel = -1;

    shapeItem(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    shapeItem(Shape shape, int start){
        this.shape = shape;
        this.color = Color.white;
        this.startY = start;
    }

    shapeItem(Shape shape){
        this.shape = shape;
        this.color = Color.white;
        this.startY = (int) this.shape.getBounds().getY();
    }

    public Shape getShape() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }

    public double getX() {
        return this.shape.getBounds().getX();
    }

    public double getY() {
        return this.shape.getBounds().getY();
    }

    public double getH() {
        return this.shape.getBounds().getHeight();
    }

    public int getW() {
        return (int) this.shape.getBounds().getWidth();
    }

    public double getDiff() {
        return Math.abs(this.shape.getBounds().getY() - this.startY);
    }

    public void setRec(double x, double y, double s) {
        this.shape = new Rectangle2D.Double(x, y, s, s);
    }
}
