import java.awt.*;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;


public class PhysicalObject extends Sprite implements Physical {
    private Vector2D position;
    private Vector2D velocity;
    private int mass;
    private World world;

    public PhysicalObject(String imagePath, int x, int y, int mass, World world) {
        super(imagePath, x, y);
        this.mass = mass;
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0, 0);
        this.world = world;
    }

    @Override
    public int getMass() {
        return mass;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Vector2D getVelocity() {
        return velocity;
    }

    @Override
    public void setPosition(double x, double y) {
        this.position = new Vector2D(x, y);
        setX((int) x);
        setY((int) y);
    }

    public void setPosition(Vector2D newPosition) {
        setPosition(newPosition.getX(), newPosition.getY());
    }

    @Override
    public void applyForce(Vector2D force, double deltaTime) {
        Vector2D acceleration = force.scalarMultiply(1.0 / mass);
        Vector2D deltaVelocity = acceleration.scalarMultiply(deltaTime);
        Vector2D newVelocity = velocity.add(deltaVelocity);
        velocity = newVelocity;

        //Vector2D deltaPosition = newVelocity.scalarMultiply(deltaTime);
        //Vector2D newPosition = position.add(deltaPosition);
        //setPosition(newPosition);
    }

    @Override
    public void updatePhysics(double deltaTime) {
        PhysicsEngine.updateForces(this, world.getCenterX(), world.getCenterY());
        //Vector2D newPosition = position.add(velocity.scalarMultiply(deltaTime));

        // Check for collision
        //setPosition(newPosition.getX(), newPosition.getY());
        //if (world.isCollidingWith(this)) {
        //    System.out.println("BANG!");
        //} else {
        //    setPosition(newPosition.getX(), newPosition.getY());
        //}
    }

    @Override
    public void move() {
        if (!world.isCollidingWith(this)) {
            setX(getX() + velocity.getX());
            setY(getY() + velocity.getY());
        } else {
            // If colliding, keep the object at the edge of the world
            double angle = Math.atan2(getY() - world.getCenterY(), getX() - world.getCenterX());
            setX(world.getCenterX() + (world.getRadius() + getWidth() / 2) * Math.cos(angle) - getWidth() / 2);
            setY(world.getCenterY() + (world.getRadius() + getHeight() / 2) * Math.sin(angle) - getHeight() / 2);
        }
    }

    @Override
    public void draw(Graphics2D g, Camera camera) {
        super.draw(g, camera);
    }
}
