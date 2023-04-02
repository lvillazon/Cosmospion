import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {
    private BufferedImage image;
    private int x;
    private int y;
    private int width;
    private int height;
    private double rotation;

    public Sprite(String imagePath, int x, int y) {
        this.x = x;
        this.y = y;
        rotation = 0.0;

        try {
            image = ImageIO.read(new FileInputStream(new File(imagePath)));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void rotate(double angle) {
        rotation += angle;
    }

    public void draw(Graphics g, Camera camera) {
        int drawX = (int) ((x - camera.getX()) * camera.getZoom()) + g.getClipBounds().width / 2;
        int drawY = (int) ((y - camera.getY()) * camera.getZoom()) + g.getClipBounds().height / 2;

        int drawWidth = (int) (width * camera.getZoom());
        int drawHeight = (int) (height * camera.getZoom());

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(rotation), drawX, drawY);
        g2d.drawImage(image, drawX - drawWidth / 2, drawY - drawHeight / 2, drawWidth, drawHeight, null);
        g2d.dispose();
    }
}
