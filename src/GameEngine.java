import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameEngine extends JFrame implements ActionListener {
    private Renderer renderer;
    private Camera camera;
    private Sprite sprite;
    private World world;
    private InputManager inputManager;
    private int targetRotation;
    private boolean currentlyRotating;
    private static final double ROTATION_SPEED = 5.0;
    private static final double ACCELERATION = 0.1;

    public GameEngine() {
        setTitle("Cosmospion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        camera = new Camera(0, 0, 0.5);
        sprite = new Sprite("assets\\art\\sprites\\ship2.png", 0, 0);
        targetRotation = (int)sprite.getRotation();
        currentlyRotating = false;
        world = new World(1000, 1.0, 0, 0);
        positionSpriteOnWorld();
        renderer = new Renderer(camera, sprite, world);
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
            sprite.rotate(-ROTATION_SPEED);
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_D)) {
            sprite.rotate(ROTATION_SPEED);
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_W)) {
            sprite.setVx(sprite.getVx() + ACCELERATION * Math.sin(Math.toRadians(sprite.getRotation())));
            sprite.setVy(sprite.getVy() - ACCELERATION * Math.cos(Math.toRadians(sprite.getRotation())));
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_S)) {
            targetRotation += 180;
            targetRotation %= 360;
            sprite.startRotating180Degrees(60);
        }

        sprite.updateRotation();
        sprite.move();
        renderer.repaint();
    }

    private void positionSpriteOnWorld() {
        double angle = Math.toRadians(270); // Set angle according to desired position (90 degrees for top)
        double x = world.getRadius() * Math.cos(angle) + world.getCenterX() - sprite.getWidth() / 2;
        double y = world.getRadius() * Math.sin(angle) + world.getCenterY() - sprite.getHeight();

        sprite.setX(x);
        sprite.setY(y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameEngine::new);
    }
}
