package Legacy;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private static final int TRAJECTORY_POINTS = 100;
    private static final double TRAJECTORY_TIME_STEP = 0.1;

    private PhysicalObject sprite;
    private World world;
    private Camera camera;
    private Viewport viewport;

    public Renderer(Camera camera, PhysicalObject sprite, World world) {
        this.camera = camera;
        this.sprite = sprite;
        this.world = world;
        this.viewport = new Viewport(camera);
        setPreferredSize(new Dimension(1000, 800));
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Update the camera's position to center on the sprite
        camera.setX(sprite.getX() - getWidth() / 2.0 + sprite.getWidth() / 2.0);
        camera.setY(sprite.getY() - getHeight() / 2.0 + sprite.getHeight() / 2.0);

        viewport.apply(g2d);
        world.draw(g, camera); // Draw the world
        //drawPredictedTrajectory(g2d, sprite, world);
        sprite.draw(g2d, camera); // Draw the sprite
        viewport.unapply(g2d);
    }

    /*
    private void drawPredictedTrajectory(Graphics2D g, PhysicalObject object, World world) {
        Vector2D currentPosition = object.getPosition();
        Vector2D currentVelocity = object.getVelocity();
        int mass = object.getMass();
        g.setColor(Color.YELLOW);

        // this doesn't work because the calculateGravitationalForce method expects a physicalobject, not a position
        // could solve by creating a ghost ship and passing that

        for (int i = 0; i < TRAJECTORY_POINTS; i++) {
            // Calculate gravitational force
            Vector2D force = PhysicsEngine.calculateGravitationalForce(currentPosition, mass, world);

            // Apply force and update position and velocity
            Vector2D acceleration = force.scalarMultiply(1.0 / mass);
            Vector2D deltaVelocity = acceleration.scalarMultiply(TRAJECTORY_TIME_STEP);
            Vector2D newVelocity = currentVelocity.add(deltaVelocity);
            Vector2D deltaPosition = newVelocity.scalarMultiply(TRAJECTORY_TIME_STEP);
            Vector2D newPosition = currentPosition.add(deltaPosition);

            // Draw line segment
            Point screenPosition1 = camera.worldToScreen(currentPosition.getX(), currentPosition.getY());
            Point screenPosition2 = camera.worldToScreen(newPosition.getX(), newPosition.getY());
            g.drawLine(screenPosition1.x, screenPosition1.y, screenPosition2.x, screenPosition2.y);

            // Update current position and velocity for next iteration
            currentPosition = newPosition;
            currentVelocity = newVelocity;
        }
    }

     */



}
