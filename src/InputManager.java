import java.awt.event.*;

public class InputManager implements KeyListener, MouseWheelListener {
    private final CameraController cameraController;
    private boolean[] keys;

    public InputManager(CameraController cameraController) {
        this.cameraController = cameraController;
        keys = new boolean[256];
    }

    public boolean isKeyPressed(int keyCode) {
        return keys[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keys.length) {
            keys[keyCode] = false;
        }
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
