import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface Physical {
    Vector2D getPosition();
    Vector2D getVelocity();
    int getMass();
    void setPosition(double x, double y);
    void applyForce(Vector2D force, double deltaTime);
    void updatePhysics(double deltaTime);
}
