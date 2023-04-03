import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private Sprite sprite;
    private World world;
    private Camera camera;
    private Viewport viewport;

    public Renderer(Camera camera, Sprite sprite, World world) {
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
        sprite.draw(g, camera); // Draw the sprite
        viewport.unapply(g2d);
    }




}
