import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Orbit {
    private final RenderablePointMass centralPointMass;
    private final List<RenderablePointMass> orbitingPointMasses;

    public Orbit(RenderablePointMass centralPointMass) {
        this.centralPointMass = centralPointMass;
        this.orbitingPointMasses = new ArrayList<>();
    }

    public void addOrbitingPointMass(RenderablePointMass pointMass) {
        orbitingPointMasses.add(pointMass);
    }

    public void update(double timeDelta) {
        for (RenderablePointMass pointMass : orbitingPointMasses) {
            updateOrbitingPointMass(pointMass, timeDelta);
            pointMass.addPositionToTrail();
        }
    }

    private void updateOrbitingPointMass(RenderablePointMass pointMass, double timeDelta) {
        double dx = pointMass.getX() - centralPointMass.getX();
        double dy = pointMass.getY() - centralPointMass.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        double forceMagnitude = (OrbitSimulation.GRAVITATIONAL_CONSTANT * pointMass.getMass() * centralPointMass.getMass()) / (distance * distance);
        double accelerationMagnitude = forceMagnitude / pointMass.getMass();

        double ax = -accelerationMagnitude * (dx / distance);
        double ay = -accelerationMagnitude * (dy / distance);

        pointMass.setVelocityX(pointMass.getVelocityX() + ax * timeDelta);
        pointMass.setVelocityY(pointMass.getVelocityY() + ay * timeDelta);

        pointMass.setX(pointMass.getX() + pointMass.getVelocityX() * timeDelta);
        pointMass.setY(pointMass.getY() + pointMass.getVelocityY() * timeDelta);
    }

    public RenderablePointMass getCentralPointMass() {
        return centralPointMass;
    }

    public List<RenderablePointMass> getOrbitingPointMasses() {
        return orbitingPointMasses;
    }


    public boolean isCollision(RenderablePointMass orbitingPointMass) {
        for (RenderablePointMass otherPointMass : orbitingPointMasses) {
            if (orbitingPointMass == otherPointMass) {
                continue;
            }

            double distance = orbitingPointMass.getDistance(otherPointMass);
            double combinedRadius = orbitingPointMass.getRenderRadius() + otherPointMass.getRenderRadius();

            if (distance <= combinedRadius) {
                return true;
            }
        }

        // Check collision with central point mass
        if (centralPointMass instanceof RenderablePointMass) {
            RenderablePointMass renderableCentralPointMass = (RenderablePointMass) centralPointMass;
            double distanceToCentral = orbitingPointMass.getDistance(renderableCentralPointMass);
            double combinedRadiusWithCentral = orbitingPointMass.getRenderRadius() + renderableCentralPointMass.getRenderRadius();

            return distanceToCentral <= combinedRadiusWithCentral;
        }

        return false;
    }


}
