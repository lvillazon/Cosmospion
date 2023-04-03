import java.awt.*;

public class World {
    private int radius;
    private double atmosphericDensity;
    private double centerX;
    private double centerY;

    public World(int radius, double atmosphericDensity, double centerX, double centerY) {
        this.radius = radius;
        this.atmosphericDensity = atmosphericDensity;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    // Getter methods for centerX and centerY
    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public int getRadius() {
        return radius;
    }

    public double getAtmosphericDensity() {
        return atmosphericDensity;
    }

    public void draw(Graphics g, Camera camera) {
        Graphics2D g2d = (Graphics2D) g.create();

        int drawX = (int) Math.round(centerX - camera.getX());
        int drawY = (int) Math.round(centerY - camera.getY());

        g2d.setColor(Color.BLUE);
        g2d.fillOval(drawX - radius, drawY - radius, 2 * radius, 2 * radius);

        g2d.dispose();
    }


}
