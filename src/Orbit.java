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

    // deep copy constructor
    public Orbit(Orbit other) {
        this.centralPointMass = new RenderablePointMass(other.centralPointMass);
        this.orbitingPointMasses = new ArrayList<>();

        for (RenderablePointMass rpm : other.orbitingPointMasses) {
            this.orbitingPointMasses.add(new RenderablePointMass(rpm));
        }

        this.orbitalTracks = new ArrayList<>();
        for (OrbitalTrack track : other.orbitalTracks) {
            this.orbitalTracks.add(new OrbitalTrack(track));
        }
    }


    /**
     * Adds an orbiting point mass to the orbit.
     *
     * @param orbitingPointMass The point mass to be added to the orbit, not null.
     */
    public void addOrbitingPointMass(RenderablePointMass orbitingPointMass) {
        orbitingPointMasses.add(orbitingPointMass);
        this.orbitalTracks.add(new OrbitalTrack(Color.GREEN));
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

    /**
     * Updates the positions and velocities of all orbiting point masses relative to the central point mass
     * according to the rules of Newtonian mechanics.
     *
     * @param deltaTime The time interval in seconds for which the positions and velocities should be updated.
     */

    public void updateWithoutOrbitalTracks(double deltaTime) {
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
    }

    public void update(double deltaTime) {
        updateWithoutOrbitalTracks(deltaTime);
        updateOrbitalTracks(deltaTime, NUM_PREDICTION_POINTS);
    }

    private void updateSingleStep(double deltaTime) {
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
    }


    public void updateOrbitalTracks(double timeDelta, int predictionSteps) {
        for (int i = 0; i < orbitingPointMasses.size(); i++) {
            RenderablePointMass orbitingPointMass = orbitingPointMasses.get(i);
            OrbitalTrack track = orbitalTracks.get(i);

            // Reset the OrbitalTrack points
            track.clearPoints();

            // Create a deep copy of the current Orbit object
            Orbit tempOrbit = new Orbit(this);

            // Calculate the predicted positions for the current orbiting point mass
            for (int step = 1; step <= predictionSteps; step++) {
                tempOrbit.updateSingleStep(timeDelta); // Update with a fixed timeDelta for each step
                Point2D.Double predictedPosition = new Point2D.Double(
                        tempOrbit.getOrbitingPointMasses().get(i).getX(),
                        tempOrbit.getOrbitingPointMasses().get(i).getY()
                );
                track.addPoint(predictedPosition);
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

        double newX1 = pointMass1.getX() + (pointMass1.getVelocityX() + accelerationX1 * deltaTime) * deltaTime;
        double newY1 = pointMass1.getY() + (pointMass1.getVelocityY() + accelerationY1 * deltaTime) * deltaTime;

        double acceleration2 = force / pointMass2.getMass();
        double accelerationX2 = acceleration2 * (-dx) / distance;
        double accelerationY2 = acceleration2 * (-dy) / distance;

        double newX2 = pointMass2.getX() + (pointMass2.getVelocityX() + accelerationX2 * deltaTime) * deltaTime;
        double newY2 = pointMass2.getY() + (pointMass2.getVelocityY() + accelerationY2 * deltaTime) * deltaTime;

        pointMass1.setVelocityX(pointMass1.getVelocityX() + accelerationX1 * deltaTime);
        pointMass1.setVelocityY(pointMass1.getVelocityY() + accelerationY1 * deltaTime);
        pointMass1.setX(newX1);
        pointMass1.setY(newY1);

        pointMass2.setVelocityX(pointMass2.getVelocityX() + accelerationX2 * deltaTime);
        pointMass2.setVelocityY(pointMass2.getVelocityY() + accelerationY2 * deltaTime);
        pointMass2.setX(newX2);
        pointMass2.setY(newY2);
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

    public List<Point2D.Double> predictOrbit(RenderablePointMass orbitingPointMass, double timeDelta, int steps) {
        List<Point2D.Double> predictedPositions = new ArrayList<>();
        PointMass predictedPointMass = new PointMass(orbitingPointMass.getX(), orbitingPointMass.getY(), orbitingPointMass.getMass(), orbitingPointMass.getVelocityX(), orbitingPointMass.getVelocityY());

        for (int i = 0; i < steps; i++) {
            updateInteraction(centralPointMass, predictedPointMass, timeDelta);
            predictedPositions.add(new Point2D.Double(predictedPointMass.getX(), predictedPointMass.getY()));
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