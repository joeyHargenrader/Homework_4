import java.awt.*;

public class shapeItem {
    private Shape shape;
    private Color color;

    shapeItem(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    shapeItem(Shape shape){
        this.shape = shape;
        this.color = Color.white;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }
}
