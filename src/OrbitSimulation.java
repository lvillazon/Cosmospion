import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

// ... (imports)

public class OrbitSimulation extends JPanel implements ActionListener {
    public static final double GRAVITATIONAL_CONSTANT = 6.67430;//e-11;
    private final Orbit orbit;
    private Timer timer;
    private boolean singleStepMode;
    private double timeDelta;
    private static final double PADDING_FACTOR = 1.1;

    public OrbitSimulation(Orbit orbit, boolean singleStepMode) {
        this.orbit = orbit;
        timeDelta = 1.0;
        this.timer = new Timer(1000 / 60, this);
        this.singleStepMode = singleStepMode;
        initializeOrbit();

        if (!singleStepMode) {
            this.timer.start();
        }

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_SPACE) {
                    if (isSingleStepMode()) {
                        advanceFrame();
                    }
                } else if (keyCode == KeyEvent.VK_ESCAPE) {
                    toggleMode();
                } else if (keyCode == KeyEvent.VK_OPEN_BRACKET) {
                    decreaseSpeed();
                } else if (keyCode == KeyEvent.VK_CLOSE_BRACKET) {
                    increaseSpeed();
                }
            }
        });
    }

    private void advanceFrame() {
        orbit.update(1.0 / 60);
        repaint();
    }

    private boolean isSingleStepMode() {
        return singleStepMode;
    }

    private void toggleMode() {
        if (isSingleStepMode()) {
            timer.start();
            singleStepMode = false;
        } else {
            timer.stop();
            singleStepMode = true;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Rectangle2D.Double boundingBox = calculateBoundingBox();
        double scaleX = getWidth() / (boundingBox.getWidth() * 1.2);
        double scaleY = getHeight() / (boundingBox.getHeight() * 1.2);
        double scale = Math.min(scaleX, scaleY) * PADDING_FACTOR * 0.5;

        Graphics2D g2d = (Graphics2D) graphics;
        AffineTransform oldTransform = g2d.getTransform();
        g2d.scale(scale, scale);
        double translateX = (getWidth() / (2 * scale)) - orbit.getCentralPointMass().getX();
        double translateY = (getHeight() / (2 * scale)) - orbit.getCentralPointMass().getY();
        g2d.translate(translateX, translateY);

        orbit.getCentralPointMass().draw(graphics);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (RenderablePointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
            if (!orbitingPointMass.getTrail().isEmpty()) {
                TrailPoint oldestTrailPoint = orbitingPointMass.getTrail().get(0);
                // Draw the mass at the oldest position
                orbitingPointMass.drawAtPosition(graphics, oldestTrailPoint.getX(), oldestTrailPoint.getY());
            } else {
                // If the trail is empty, draw the mass at its current position
                orbitingPointMass.draw(graphics);
            }

            orbitingPointMass.drawTrail(graphics, Color.GRAY);
        }

        g2d.setTransform(oldTransform);
    }

    public void increaseSpeed() {
        timeDelta *= 2;
        System.out.println("time step = " + timeDelta);
    }

    public void decreaseSpeed() {
        timeDelta /= 2;
        System.out.println("time step = " + timeDelta);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        orbit.update(timeDelta);
        repaint();
    }

    private void initializeOrbit() {
        int maxTrailLength = 0;
        for (RenderablePointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
            maxTrailLength = Math.max(maxTrailLength, orbitingPointMass.getTrailLength());
        }

        boolean waitingForDeparture = true;
        for (int i = 0; i < maxTrailLength; i++) {
            orbit.update(timeDelta);

            boolean orbitClosed = false;

            for (RenderablePointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
                if (waitingForDeparture) {
                    if (!isCloseToStartingPosition(orbitingPointMass) && !orbit.isCollision(orbitingPointMass)) {
                        waitingForDeparture = false;
                    }
                } else {
                    if (isCloseToStartingPosition(orbitingPointMass) && !orbit.isCollision(orbitingPointMass)) {
                        orbitClosed = true;
                        orbitingPointMass.setTrailLength(i);
                        break;
                    }
                }
            }
            if (orbitClosed) {
                //break;
            }
        }
    }


    private boolean isCloseToStartingPosition(RenderablePointMass orbitingPointMass) {
        TrailPoint oldestTrailPoint = orbitingPointMass.getOldestTrailPoint();
        double distance = orbitingPointMass.getDistance(oldestTrailPoint.getX(), oldestTrailPoint.getY());
        double tolerance = orbitingPointMass.getRenderRadius() * 2;
        return distance <= tolerance;
    }


    private Rectangle2D.Double calculateBoundingBox() {
        double minX = orbit.getCentralPointMass().getX();
        double minY = orbit.getCentralPointMass().getY();
        double maxX = minX;
        double maxY = minY;

        for (RenderablePointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
            double x = orbitingPointMass.getX();
            double y = orbitingPointMass.getY();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);

            for (TrailPoint p : orbitingPointMass.getTrail()) {
                x = p.getX();
                y = p.getY();
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
}

