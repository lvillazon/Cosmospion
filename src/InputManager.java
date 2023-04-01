import java.awt.event.*;

public class InputManager implements KeyListener, MouseWheelListener {
    private final CameraController cameraController;

    public InputManager(CameraController cameraController) {
        this.cameraController = cameraController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle key events here, if needed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handle key events here, if needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key events here, if needed
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoomFactor = e.getWheelRotation() < 0 ? 1.1 : 0.9;
        cameraController.zoom(zoomFactor);
    }
}
