import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class RenderablePointMass extends PointMass {
    private final int radius;
    private final Color color;
    private final LinkedList<TrailPoint> trail;
    private int trailLength = 4000;

    public RenderablePointMass(double x, double y, double mass, double velocityX, double velocityY, int radius, Color color) {
        super(x, y, mass, velocityX, velocityY);
        this.radius = radius;
        this.color = color;
        this.trail = new LinkedList<>();
    }

    public int getTrailLength() {
        return trailLength;
    }

    public void setTrailLength(int length) {
        trailLength = length;
    }

    public int getRenderRadius() {
        return radius;
    }

    public LinkedList<TrailPoint> getTrail(){
        return trail;
    };

    public TrailPoint getOldestTrailPoint() {
        if (trail.isEmpty()) {
            return null;
        }
        return trail.get(0);
    }


    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillOval((int) (getX() - radius / 2.0), (int) (getY() - radius / 2.0), radius, radius);
    }

    public void drawAtPosition(Graphics g, double x, double y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
    }

    public void drawTrail(Graphics graphics, Color trailColor) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(trailColor);
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        TrailPoint prevTrailPoint = null;

        for (TrailPoint trailPoint : trail) {
            if (prevTrailPoint != null) {
                g2d.drawLine((int) prevTrailPoint.getX(), (int) prevTrailPoint.getY(), (int) trailPoint.getX(), (int) trailPoint.getY());
            }
            prevTrailPoint = trailPoint;
        }
    }


    public void addPositionToTrail() {
        if (trail.size() >= trailLength) {
            trail.removeFirst();
        }
        trail.addLast(new TrailPoint(getX(), getY(), getVelocityX(), getVelocityY()));
    }

}
