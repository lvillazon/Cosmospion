import java.awt.Color;
import java.awt.Graphics;

public class RenderablePointMass extends PointMass implements Renderable {
    private final int pointSize;

    public RenderablePointMass(double x, double y, double mass, double velocityX, double velocityY, int pointSize) {
        super(x, y, mass, velocityX, velocityY);
        this.pointSize = pointSize;
    }

    @Override
    public int getScreenX() {
        // Convert the x-coordinate of the position to screen coordinates.
        // Replace this with your own conversion logic.
        return (int) getX();
    }

    @Override
    public int getScreenY() {
        // Convert the y-coordinate of the position to screen coordinates.
        // Replace this with your own conversion logic.
        return (int) getY();
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.RED);
        int screenX = getScreenX();
        int screenY = getScreenY();
        int adjustedPointSize = Math.max(pointSize, 3); // Ensure a minimum size of 3
        graphics.fillOval(
                screenX - adjustedPointSize / 2,
                screenY - adjustedPointSize / 2,
                adjustedPointSize, adjustedPointSize);

    }
}
