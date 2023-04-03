import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class OrbitSimulation extends JPanel implements ActionListener {
    private final Orbit orbit;
    private Timer timer;

    public OrbitSimulation(Orbit orbit) {
        this.orbit = orbit;
        this.timer = new Timer(1000 / 60, this); // 60 frames per second
        this.timer.start();
    }

    // render the orbiting objects
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Get the bounding box of the orbit and calculate the scaling factor (so that they all fit on screen)
        Rectangle2D.Double boundingBox = orbit.getBoundingBox();
        double scaleX = getWidth() / boundingBox.getWidth();
        double scaleY = getHeight() / boundingBox.getHeight();
        double scale = Math.min(scaleX, scaleY);

        // Set up the graphics context to scale and translate the drawing
        Graphics2D g2d = (Graphics2D) graphics;
        AffineTransform oldTransform = g2d.getTransform();
        g2d.scale(scale, scale);
        g2d.translate(-boundingBox.getX() + (getWidth() / scale - boundingBox.getWidth()) / 2,
                -boundingBox.getY() + (getHeight() / scale - boundingBox.getHeight()) / 2);

        // Draw the central point mass
        RenderablePointMass centralPointMass = new RenderablePointMass(
                orbit.getCentralPointMass().getX(),
                orbit.getCentralPointMass().getY(),
                orbit.getCentralPointMass().getMass(),
                orbit.getCentralPointMass().getVelocityX(),
                orbit.getCentralPointMass().getVelocityY(),
                10);
        centralPointMass.draw(graphics);

        // Draw the orbiting point masses
        for (PointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
            RenderablePointMass renderableOrbitingPointMass = new RenderablePointMass(
                    orbitingPointMass.getX(),
                    orbitingPointMass.getY(),
                    orbitingPointMass.getMass(),
                    orbitingPointMass.getVelocityX(),
                    orbitingPointMass.getVelocityY(),
                    5);
            renderableOrbitingPointMass.draw(graphics);
        }

        // Restore the original graphics context
        g2d.setTransform(oldTransform);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        orbit.update(1.0 / 60); // Update the orbit with a time step of 1/60 seconds
        repaint(); // Request a repaint to update the display
    }
}