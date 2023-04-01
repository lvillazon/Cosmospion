import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {
    private BufferedImage image;
    private int width;
    private int height;

    public Sprite(String imagePath) {
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

    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x - width / 2, y - height / 2, null);
    }
}
