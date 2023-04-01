import java.awt.*;

public class Viewport {
    private Camera camera;

    public Viewport(Camera camera) {
        this.camera = camera;
    }

    public void apply(Graphics2D g) {
        g.translate(-camera.getX() + g.getClipBounds().width / 2, -camera.getY() + g.getClipBounds().height / 2);
        g.scale(camera.getZoom(), camera.getZoom());
    }

    public void unapply(Graphics2D g) {
        g.scale(1 / camera.getZoom(), 1 / camera.getZoom());
        g.translate(camera.getX() - g.getClipBounds().width / 2, camera.getY() - g.getClipBounds().height / 2);
    }
}
