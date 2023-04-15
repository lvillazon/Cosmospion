package Legacy;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class PhysicsEngine {
    private static final double GRAVITATIONAL_CONSTANT = 0;//9.81;

    public static void updateForces(PhysicalObject physicalObject, double  xCenter, double yCenter) {
        update(physicalObject, xCenter, yCenter);
    }

    public static Vector2D calculateGravitationalForce(PhysicalObject p, double worldCenterX, double worldCenterY) {
        double dx = worldCenterX - p.getX();
        double dy = worldCenterY - p.getY();

        double distanceSquared = dx * dx + dy * dy;
        double forceMagnitude = GRAVITATIONAL_CONSTANT * p.getMass() / distanceSquared;

        double normalizedDx = dx / Math.sqrt(distanceSquared);
        double normalizedDy = dy / Math.sqrt(distanceSquared);

        double fx = forceMagnitude * normalizedDx;
        double fy = forceMagnitude * normalizedDy;

        return new Vector2D(fx, fy);
    }

    public static void update(PhysicalObject p, double worldCenterX, double worldCenterY) {
        Vector2D gravitationalForce = calculateGravitationalForce(p, worldCenterX, worldCenterY);
        p.setVx(p.getVx() + gravitationalForce.getX());
        p.setVy(p.getVy() + gravitationalForce.getY());

        p.setX(p.getX() + p.getVx());
        p.setY(p.getY() + p.getVy());
    }
}
