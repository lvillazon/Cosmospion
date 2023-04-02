import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private Sprite sprite;
    private Camera camera;
    private Viewport viewport;

    public Renderer(Camera camera, Sprite sprite) {
        this.camera = camera;
        this.sprite = sprite;
        this.viewport = new Viewport(camera);
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Update the camera's position to center on the sprite
        camera.setX(sprite.getX() + sprite.getWidth() / 2 - g.getClipBounds().width / 2);
        camera.setY(sprite.getY() + sprite.getHeight() / 2 - g.getClipBounds().height / 2);

        viewport.apply(g2d);
        sprite.draw(g, camera);
        viewport.unapply(g2d);
    }

}
