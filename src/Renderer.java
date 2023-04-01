import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private Sprite sprite;
    private Camera camera;
    private Viewport viewport;
    private InputManager inputManager;

    public Renderer() {
        sprite = new Sprite("assets\\art\\sprites\\ship1.png");
        camera = new Camera(sprite.getWidth()/2,sprite.getHeight()/2,1.0);
        viewport = new Viewport(camera);
        CameraController cameraController = new CameraController(camera);
        inputManager = new InputManager(cameraController);
        addKeyListener(inputManager);
        addMouseWheelListener(inputManager);
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        viewport.apply(g2d);
        sprite.draw(g, getWidth() / 2, getHeight() / 2);
        viewport.unapply(g2d);
    }

}
