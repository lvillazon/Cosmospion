import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an orbit with a central point mass and one or more orbiting point masses.
 * The positions and velocities of orbiting point masses are calculated relative to the central point mass
 * according to the rules of Newtonian mechanics.
 */
public class Orbit {
    private final RenderablePointMass centralPointMass;
    private final List<RenderablePointMass> orbitingPointMasses;
    private final double gravitationalConstant = 6.67430e1;// was 6.67430e-11;
    private Rectangle2D.Double boundingBox;

    /**
     * Creates a new Orbit with the given central point mass.
     *
     * @param centralPointMass The central point mass of the orbit, not null.
     */
    public Orbit(RenderablePointMass centralPointMass) {
        this.centralPointMass = centralPointMass;
        this.orbitingPointMasses = new ArrayList<>();
        this.boundingBox = new Rectangle2D.Double();    }

    /**
     * Adds an orbiting point mass to the orbit.
     *
     * @param orbitingPointMass The point mass to be added to the orbit, not null.
     */
    public void addOrbitingPointMass(RenderablePointMass orbitingPointMass) {
        orbitingPointMasses.add(orbitingPointMass);
    }

    /**
     * Returns the central point mass of the orbit.
     *
     * @return The central point mass of the orbit, not null.
     */
    public RenderablePointMass getCentralPointMass() {
        return centralPointMass;
    }

    /**
     * Returns an unmodifiable list of orbiting point masses.
     *
     * @return An unmodifiable list of orbiting point masses, not null.
     */
    public List<RenderablePointMass> getOrbitingPointMasses() {
        return Collections.unmodifiableList(orbitingPointMasses);
    }

    // calculate the rectangle needed to enclose all objects in the orbit
    public Rectangle2D.Double getBoundingBox() {
        return boundingBox;
    }

    /**
     * Updates the positions and velocities of all orbiting point masses relative to the central point mass
     * according to the rules of Newtonian mechanics.
     *
     * @param deltaTime The time interval in seconds for which the positions and velocities should be updated.
     */
    public void update(double deltaTime) {
        for (PointMass orbitingPointMass : orbitingPointMasses) {
            // Calculate gravitational force
            double dx = centralPointMass.getX() - orbitingPointMass.getX();
            double dy = centralPointMass.getY() - orbitingPointMass.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            double force = gravitationalConstant * centralPointMass.getMass() * orbitingPointMass.getMass() / (distance * distance);

            // Calculate acceleration due to gravity
            double acceleration = force / orbitingPointMass.getMass();
            double accelerationX = acceleration * dx / distance;
            double accelerationY = acceleration * dy / distance;

            // Update velocity
            orbitingPointMass.setVelocityX(orbitingPointMass.getVelocityX() + accelerationX * deltaTime);
            orbitingPointMass.setVelocityY(orbitingPointMass.getVelocityY() + accelerationY * deltaTime);

            // Update position
            orbitingPointMass.setX(orbitingPointMass.getX() + orbitingPointMass.getVelocityX() * deltaTime);
            orbitingPointMass.setY(orbitingPointMass.getY() + orbitingPointMass.getVelocityY() * deltaTime);
        }
    }

    public List<Point2D.Double> predictOrbit(PointMass orbitingPointMass, double deltaTime, int steps) {
        List<Point2D.Double> predictedPositions = new ArrayList<>();
        PointMass tempPointMass = new PointMass(orbitingPointMass.getX(), orbitingPointMass.getY(), orbitingPointMass.getMass(), orbitingPointMass.getVelocityX(), orbitingPointMass.getVelocityY());

        double minX = centralPointMass.getX();
        double minY = centralPointMass.getY();
        double maxX = minX;
        double maxY = minY;

        for (int i = 0; i < steps; i++) {
            updatePosition(tempPointMass, deltaTime);
            Point2D.Double predictedPosition = new Point2D.Double(tempPointMass.getX(), tempPointMass.getY());
            predictedPositions.add(predictedPosition);

            minX = Math.min(minX, predictedPosition.getX());
            minY = Math.min(minY, predictedPosition.getY());
            maxX = Math.max(maxX, predictedPosition.getX());
            maxY = Math.max(maxY, predictedPosition.getY());
        }

        for (PointMass orbitingPointMass2 : orbitingPointMasses) {
            double x = orbitingPointMass2.getX();
            double y = orbitingPointMass2.getY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        // update the bounding box so that the future orbital path fits within it
        boundingBox.setRect(minX, minY, maxX - minX, maxY - minY);
        return predictedPositions;
    }

    private void updatePosition(PointMass pointMass, double deltaTime) {
        double dx = centralPointMass.getX() - pointMass.getX();
        double dy = centralPointMass.getY() - pointMass.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double force = gravitationalConstant * centralPointMass.getMass() * pointMass.getMass() / (distance * distance);

        double acceleration = force / pointMass.getMass();
        double accelerationX = acceleration * dx / distance;
        double accelerationY = acceleration * dy / distance;

        pointMass.setVelocityX(pointMass.getVelocityX() + accelerationX * deltaTime);
        pointMass.setVelocityY(pointMass.getVelocityY() + accelerationY * deltaTime);

        pointMass.setX(pointMass.getX() + pointMass.getVelocityX() * deltaTime);
        pointMass.setY(pointMass.getY() + pointMass.getVelocityY() * deltaTime);
    }

}