import java.awt.*;
import java.awt.geom.Point2D;
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
    private final List<OrbitalTrack> orbitalTracks;
    private static final int NUM_PREDICTION_POINTS = 2000;

    /**
     * Creates a new Orbit with the given central point mass.
     *
     * @param centralPointMass The central point mass of the orbit, not null.
     */
    public Orbit(RenderablePointMass centralPointMass) {
        this.centralPointMass = centralPointMass;
        this.orbitingPointMasses = new ArrayList<>();
        this.orbitalTracks = new ArrayList<>();

        for (RenderablePointMass orbitingPointMass : orbitingPointMasses) {
            orbitalTracks.add(new OrbitalTrack(Color.GREEN));
        }
    }

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
    //public Rectangle2D.Double getBoundingBox() {
    //    return boundingBox;
    //}

    /**
     * Updates the positions and velocities of all orbiting point masses relative to the central point mass
     * according to the rules of Newtonian mechanics.
     *
     * @param deltaTime The time interval in seconds for which the positions and velocities should be updated.
     */

    public void update(double deltaTime) {
        for (int i = 0; i < orbitingPointMasses.size(); i++) {
            PointMass pointMass1 = orbitingPointMasses.get(i);

            // Interaction between orbiting point masses and the central point mass
            updateInteraction(pointMass1, centralPointMass, deltaTime);

            // Interaction between orbiting point masses themselves
            for (int j = i + 1; j < orbitingPointMasses.size(); j++) {
                PointMass pointMass2 = orbitingPointMasses.get(j);
                updateInteraction(pointMass1, pointMass2, deltaTime);
            }
        }
        updateOrbitalTracks(deltaTime);
    }

    private void updateOrbitalTracks(double deltaTime) {
        int numSteps = 2000;
        double timeMultiplier = 1.0;

        for (int i = 0; i < orbitingPointMasses.size(); i++) {
            RenderablePointMass originalOrbitingPointMass = orbitingPointMasses.get(i);
            RenderablePointMass orbitingPointMassCopy = new RenderablePointMass(originalOrbitingPointMass);
            RenderablePointMass centralPointMassCopy = new RenderablePointMass(centralPointMass);

            OrbitalTrack track = orbitalTracks.get(i);
            track.getPoints().clear();

            for (int step = 0; step < numSteps; step++) {
                updateInteraction(orbitingPointMassCopy, centralPointMassCopy, deltaTime * timeMultiplier);
                track.addPoint(new Point2D.Double(orbitingPointMassCopy.getX(), orbitingPointMassCopy.getY()));
                timeMultiplier += 1.0;
            }
        }
    }

    private void updateInteraction(PointMass pointMass1, PointMass pointMass2, double deltaTime) {
        double dx = pointMass2.getX() - pointMass1.getX();
        double dy = pointMass2.getY() - pointMass1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double force = gravitationalConstant * pointMass1.getMass() * pointMass2.getMass() / (distance * distance);

        double acceleration1 = force / pointMass1.getMass();
        double accelerationX1 = acceleration1 * dx / distance;
        double accelerationY1 = acceleration1 * dy / distance;

        pointMass1.setVelocityX(pointMass1.getVelocityX() + accelerationX1 * deltaTime);
        pointMass1.setVelocityY(pointMass1.getVelocityY() + accelerationY1 * deltaTime);

        pointMass1.setX(pointMass1.getX() + pointMass1.getVelocityX() * deltaTime);
        pointMass1.setY(pointMass1.getY() + pointMass1.getVelocityY() * deltaTime);

        double acceleration2 = force / pointMass2.getMass();
        double accelerationX2 = -acceleration1 * dx / distance;
        double accelerationY2 = -acceleration1 * dy / distance;

        pointMass2.setVelocityX(pointMass2.getVelocityX() + accelerationX2 * deltaTime);
        pointMass2.setVelocityY(pointMass2.getVelocityY() + accelerationY2 * deltaTime);

        pointMass2.setX(pointMass2.getX() + pointMass2.getVelocityX() * deltaTime);
        pointMass2.setY(pointMass2.getY() + pointMass2.getVelocityY() * deltaTime);
    }

    private void updatePositionForPrediction(PointMass pointMass, List<PointMass> allPointMasses, double deltaTime) {
        //System.out.println(pointMass.getX() +"," +pointMass.getY());
        for (PointMass otherPointMass : allPointMasses) {
            if (pointMass != otherPointMass) {
                double dx = otherPointMass.getX() - pointMass.getX();
                double dy = otherPointMass.getY() - pointMass.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance>0) {  // avoid throwing NaN if two points ever touch
                    double force = gravitationalConstant * pointMass.getMass() * otherPointMass.getMass() / (distance * distance);

                    double acceleration = force / pointMass.getMass();
                    double accelerationX = acceleration * dx / distance;
                    double accelerationY = acceleration * dy / distance;

                    pointMass.setVelocityX(pointMass.getVelocityX() + accelerationX * deltaTime);
                    pointMass.setVelocityY(pointMass.getVelocityY() + accelerationY * deltaTime);
                }
            }
        }
        //System.out.println(pointMass.getX() +"," +pointMass.getY());
        pointMass.setX(pointMass.getX() + pointMass.getVelocityX() * deltaTime);
        pointMass.setY(pointMass.getY() + pointMass.getVelocityY() * deltaTime);
    }

    public List<Point2D.Double> predictOrbit(PointMass orbitingPointMass, double deltaTime, int steps) {
        List<Point2D.Double> predictedPositions = new ArrayList<>();
        PointMass tempPointMass = new PointMass(orbitingPointMass.getX(), orbitingPointMass.getY(), orbitingPointMass.getMass(), orbitingPointMass.getVelocityX(), orbitingPointMass.getVelocityY());
        List<PointMass> allPointMasses = new ArrayList<>(orbitingPointMasses);
        allPointMasses.add(centralPointMass);

        for (int i = 0; i < steps; i++) {
            updatePositionForPrediction(tempPointMass, allPointMasses, deltaTime);
            predictedPositions.add(new Point2D.Double(tempPointMass.getX(), tempPointMass.getY()));
        }

        return predictedPositions;
    }

    public List<Point2D.Double> oldpredictOrbit(PointMass orbitingPointMass, double deltaTime, int steps) {
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
        //boundingBox.setRect(minX, minY, maxX - minX, maxY - minY);
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