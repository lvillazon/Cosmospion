import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite implements Renderable {
    private BufferedImage image;
    private int x;
    private int y;
    private int width;
    private int height;
    private double rotation;
    private int rotationStepCounter;
    private int totalRotationSteps;
    private double targetRotation;
    private double vx;
    private double vy;

    public Sprite(String imagePath, int x, int y) {
        this.x = x;
        this.y = y;
        rotation = 0.0;
        rotationStepCounter = 0;
        totalRotationSteps = 0;
        targetRotation = rotation;
        vx = 0.0;
        vy = 0.0;

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

    public void setX(double x) {
        this.x = (int)x;
    }

    public void setY(double y) {
        this.y = (int)y;
    }

    public void startRotating180Degrees(int steps) {
        rotationStepCounter = 0;
        totalRotationSteps = steps;
        targetRotation = rotation + 180;
    }

    public void updateRotation() {
        if (rotationStepCounter < totalRotationSteps) {
            rotation += (targetRotation - rotation) / (totalRotationSteps - rotationStepCounter);
            rotationStepCounter++;
            if (rotationStepCounter==totalRotationSteps) {
                rotation = targetRotation;  // guarantee we finish at the required rotation, rather than slightly off
            }
        }
    }

    public void rotate(double angle) {
        rotation += angle;
    }

    public double getRotation() {
        return rotation;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void move() {
        x += vx;
        y += vy;
    }

    public void draw(Graphics2D g, Camera camera) {
        int drawX = (int) Math.round(x - camera.getX());
        int drawY = (int) Math.round(y - camera.getY());

        g.translate(drawX + getWidth() / 2, drawY + getHeight() / 2);
        g.rotate(Math.toRadians(getRotation()));
        g.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);
    }





}
