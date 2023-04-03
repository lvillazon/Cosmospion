import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameEngine extends JFrame implements ActionListener {
    private Renderer renderer;
    private Camera camera;
    private PhysicalObject ship;
    private World world;
    private InputManager inputManager;
    private int targetRotation;
    private boolean currentlyRotating;
    private static final double ROTATION_SPEED = 5.0;
    private static final double ACCELERATION = 300;

    public GameEngine() {
        setTitle("Cosmospion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        camera = new Camera(0, 0, 0.5);
        world = new World(1000, 1.0, 0, 0);
        ship = new PhysicalObject("assets\\art\\sprites\\ship3.png", 0, 0, 10000, world);
        targetRotation = (int)ship.getRotation();
        currentlyRotating = false;
        positionSpriteOnWorld();
        renderer = new Renderer(camera, ship, world);
        CameraController cameraController = new CameraController(camera);
        inputManager = new InputManager(cameraController);
        renderer.addKeyListener(inputManager);
        renderer.addMouseWheelListener(inputManager);
        add(renderer);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        Timer timer = new Timer(1000 / 60, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inputManager.isKeyPressed(KeyEvent.VK_A)) {
            ship.rotate(-ROTATION_SPEED);
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_D)) {
            ship.rotate(ROTATION_SPEED);
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_W)) {
            double angle = Math.toRadians(ship.getRotation());
            Vector2D force = new Vector2D(ACCELERATION * Math.sin(angle), -ACCELERATION * Math.cos(angle));
            ship.applyForce(force, 1);
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_S)) {
            targetRotation += 180;
            targetRotation %= 360;
            ship.startRotating180Degrees(60);
        }

        ship.updateRotation();
        ship.updatePhysics(1);
        ship.move();
        renderer.repaint();
    }

    private void positionSpriteOnWorld() {
        double angle = Math.toRadians(270); // Set angle according to desired position (90 degrees for top)
        double x = 2*world.getRadius() * Math.cos(angle) + world.getCenterX() - ship.getWidth() / 2;
        double y = 2*world.getRadius() * Math.sin(angle) + world.getCenterY() - ship.getHeight();

        ship.setPosition(x,y);
//        ship.setX(x);
//        ship.setY(y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameEngine::new);
    }
}
