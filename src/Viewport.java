import java.awt.*;

public class Viewport {
    private Camera camera;

    public Viewport(Camera camera) {
        this.camera = camera;
    }

    public void apply(Graphics2D g) {
        int centerX = g.getClipBounds().width / 2;
        int centerY = g.getClipBounds().height / 2;

        g.translate(centerX, centerY);
        g.scale(camera.getZoom(), camera.getZoom());
        g.translate(-centerX, -centerY);
    }

    public void unapply(Graphics2D g) {
        g.scale(1 / camera.getZoom(), 1 / camera.getZoom());
        g.translate(camera.getX() - g.getClipBounds().width / 2, camera.getY() - g.getClipBounds().height / 2);
    }
}
