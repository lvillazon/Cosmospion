public class Camera {
    private int x;
    private int y;
    private double zoom;

    public Camera(int x, int y, double zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
}
