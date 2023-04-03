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
    private final PointMass centralPointMass;
    private final List<PointMass> orbitingPointMasses;
    private final double gravitationalConstant = 6.67430e-11;

    /**
     * Creates a new Orbit with the given central point mass.
     *
     * @param centralPointMass The central point mass of the orbit, not null.
     */
    public Orbit(PointMass centralPointMass) {
        this.centralPointMass = centralPointMass;
        this.orbitingPointMasses = new ArrayList<>();
    }

    /**
     * Adds an orbiting point mass to the orbit.
     *
     * @param orbitingPointMass The point mass to be added to the orbit, not null.
     */
    public void addOrbitingPointMass(PointMass orbitingPointMass) {
        orbitingPointMasses.add(orbitingPointMass);
    }

    /**
     * Returns the central point mass of the orbit.
     *
     * @return The central point mass of the orbit, not null.
     */
    public PointMass getCentralPointMass() {
        return centralPointMass;
    }

    /**
     * Returns an unmodifiable list of orbiting point masses.
     *
     * @return An unmodifiable list of orbiting point masses, not null.
     */
    public List<PointMass> getOrbitingPointMasses() {
        return Collections.unmodifiableList(orbitingPointMasses);
    }

    // calculate the rectangle needed to enclose all objects in the orbit
    public Rectangle2D.Double getBoundingBox() {
        double minX = centralPointMass.getX();
        double minY = centralPointMass.getY();
        double maxX = minX;
        double maxY = minY;

        for (PointMass orbitingPointMass : orbitingPointMasses) {
            double x = orbitingPointMass.getX();
            double y = orbitingPointMass.getY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
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

    // testing update method
    public void testUpdate() {
        System.out.println("Initial position: " + centralPointMass.getX() + ", " + centralPointMass.getY());
        for (int i = 0; i < 10; i++) {
            update(1);
            System.out.println("Updated position: " + centralPointMass.getX() + ", " + centralPointMass.getY());
        }
    }

}