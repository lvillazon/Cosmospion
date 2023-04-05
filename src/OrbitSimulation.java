import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class OrbitSimulation extends JPanel implements ActionListener {
    private final Orbit orbit;
    private Timer timer;
    private boolean singleStepMode;
    private double timeDelta;
    private int predictionSteps = 2000;  // how many time steps to look ahead for orbits.
    // Add a padding constant to ensure both bodies are visible on the screen
    private static final double PADDING_FACTOR = 1.1;

    public OrbitSimulation(Orbit orbit, boolean singleStepMode) {
        this.orbit = orbit;
        timeDelta = 1.0;  // default time step of 1s per frame
        this.timer = new Timer(1000 / 60, this); // 60 frames per second
        this.singleStepMode = singleStepMode;

        if (!singleStepMode) {
            this.timer.start();
        }

        // Add a KeyListener to handle simulation key events:
        //  ESC - toggle single step mode
        // Space - next step
        // [ and ] slow and speed simulation
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

    // render the orbiting objects
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Get the bounding box of the orbit and calculate the scaling factor (so that they all fit on screen)
        Rectangle2D.Double boundingBox = calculateBoundingBox();
        //System.out.print("Bounding box:"+ boundingBox);

        double scaleX = getWidth() / (boundingBox.getWidth() * 1.2);
        double scaleY = getHeight() / (boundingBox.getHeight() * 1.2);
        // Adjust the scale calculation to include the padding factor
        double scale = Math.min(scaleX, scaleY) * PADDING_FACTOR*.5;
        //System.out.print("  scale:"+scale);

        // Set up the graphics context to scale and translate the drawing
        Graphics2D g2d = (Graphics2D) graphics;
        AffineTransform oldTransform = g2d.getTransform();
        g2d.scale(scale, scale);
        // Calculate the translation required to center the central point mass
        double translateX = (getWidth() / (2 * scale)) - orbit.getCentralPointMass().getX();
        double translateY = (getHeight() / (2 * scale)) - orbit.getCentralPointMass().getY();
        g2d.translate(translateX, translateY);
        //System.out.println("   translate:"+translateX+","+translateY);

        // Draw the central point mass
        orbit.getCentralPointMass().draw(graphics);

        // Draw the orbiting point masses
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (RenderablePointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
            // Draw the predicted path
            java.util.List<Point2D.Double> predictedPositions = orbit.predictOrbit(orbitingPointMass, timeDelta, predictionSteps); // Predict the path for the next 10 seconds (600 steps)
            g2d.setColor(Color.GREEN);
            Point2D.Double previousPoint = new Point2D.Double(orbitingPointMass.getX(), orbitingPointMass.getY());
            for (Point2D.Double point : predictedPositions) {
                g2d.drawLine((int)previousPoint.x, (int)previousPoint.y, (int)point.x, (int)point.y);
                previousPoint = point;
            }
            //draw the point mass itself
            orbitingPointMass.draw(graphics);
        }

        // Restore the original graphics context
        g2d.setTransform(oldTransform);
    }

    public void increaseSpeed() {
        timeDelta *=2;
        System.out.println("time step = "+timeDelta);
    }

    public void decreaseSpeed() {
        timeDelta /=2;
        System.out.println("time step = "+timeDelta);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        orbit.update(timeDelta); // Update the orbit with the current time step
        repaint(); // Request a repaint to update the display
    }

    private Rectangle2D.Double calculateBoundingBox() {
        double minX = orbit.getCentralPointMass().getX();
        double minY = orbit.getCentralPointMass().getY();
        double maxX = minX;
        double maxY = minY;

        for (PointMass orbitingPointMass : orbit.getOrbitingPointMasses()) {
            List<Point2D.Double> predictedPositions = orbit.predictOrbit(orbitingPointMass, timeDelta, predictionSteps);
            //System.out.println("Predicted positions size: " + predictedPositions.size());
            for (Point2D.Double position : predictedPositions) {
                double x = position.getX();
                //System.out.println(position);
                double y = position.getY();
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }
        }
        Rectangle2D.Double r = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        //System.out.println(minX+","+maxX);
        return r;
    }

}



