import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class OrbitalTrack {
    private final List<Point2D.Double> points;
    private final Color color;

    public OrbitalTrack(Color color) {
        this.points = new ArrayList<>();
        this.color = color;
    }

    public void addPoint(Point2D.Double point) {
        points.add(point);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 1; i < points.size(); i++) {
            Point2D.Double p1 = points.get(i - 1);
            Point2D.Double p2 = points.get(i);
            g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }
    }

    public List<Point2D.Double> getPoints() {
        return points;
    }
}
