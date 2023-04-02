import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameEngine extends JFrame implements ActionListener {
    private Renderer renderer;
    private Camera camera;
    private Sprite sprite;
    private InputManager inputManager;
    private static final double ROTATION_SPEED = 5.0;

    public GameEngine() {
        setTitle("Cosmospion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        camera = new Camera(0, 0, 1.0);
        sprite = new Sprite("assets\\art\\sprites\\ship1.png", 400, 300);
        renderer = new Renderer(camera, sprite);
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
        renderer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameEngine::new);
    }
}
