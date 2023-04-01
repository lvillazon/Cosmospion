import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;

public class GamePanel extends JPanel implements ActionListener {
    private BufferedImage sprite;
    private double rotation;
    private final int rotationSpeed = 5;

    public GamePanel() {
        try {
            sprite = ImageIO.read(new FileInputStream(new File("assets\\art\\sprites\\ship1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        addKeyListener(new KeyInput());
        Timer timer = new Timer(1000 / 60, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int spriteWidth = sprite.getWidth();
        int spriteHeight = sprite.getHeight();

        g2d.rotate(Math.toRadians(rotation), centerX, centerY);
        g2d.drawImage(sprite, centerX - spriteWidth / 2, centerY - spriteHeight / 2, null);
        g2d.rotate(-Math.toRadians(rotation), centerX, centerY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_A) {
                rotation -= rotationSpeed;
            } else if (key == KeyEvent.VK_D) {
                rotation += rotationSpeed;
            }
        }
    }
}
