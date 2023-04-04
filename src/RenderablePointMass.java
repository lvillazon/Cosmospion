import java.awt.*;
import java.util.LinkedList;

public class RenderablePointMass extends PointMass implements Renderable {
    private final int pointSize;
    private final LinkedList<Point> previousPositions;
    private final int maxTrailLength;

    public RenderablePointMass(double x, double y, double mass, double velocityX, double velocityY, int pointSize) {
        super(x, y, mass, velocityX, velocityY);
        this.pointSize = pointSize;
        this.previousPositions = new LinkedList<>();
        this.maxTrailLength = 500; // Adjust this value to change the trail length
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
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the trail
        g2d.setColor(Color.GRAY);
        Point previousPoint = new Point((int) getX(), (int) getY());
        for (Point point : previousPositions) {
            g2d.drawLine(previousPoint.x, previousPoint.y, point.x, point.y);
            previousPoint = point;
        }

        // draw the point mass
        graphics.setColor(Color.RED);
        int screenX = getScreenX();
        int screenY = getScreenY();
        int adjustedPointSize = Math.max(pointSize, 3); // Ensure a minimum size of 3
        graphics.fillOval(
                screenX - adjustedPointSize / 2,
                screenY - adjustedPointSize / 2,
                adjustedPointSize, adjustedPointSize);

        // Add the current position to the trail and remove the oldest one if necessary
        previousPositions.addFirst(new Point((int) getX(), (int) getY()));
        if (previousPositions.size() > maxTrailLength) {
            previousPositions.removeLast();
        }
    }
}
