package Legacy;

public class CameraController {
    private Camera camera;

    public CameraController(Camera camera) {
        this.camera = camera;
    }

    public void move(int dx, int dy) {
        camera.setX(camera.getX() + dx);
        camera.setY(camera.getY() + dy);
    }

    public void zoom(double zoomFactor) {
        camera.setZoom(camera.getZoom() * zoomFactor);
    }
}
