import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanel {

    private RenderablePointMass renderablePointMass;
    private Planet planet;

    public TestPanel() {
        renderablePointMass = new RenderablePointMass(100, 100, 1, 0, 0, 5);
        planet = new Planet(200, 100, 1, 0, 0, 10, 1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderablePointMass.draw(g);
        planet.draw(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Renderable");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setContentPane(new TestPanel());
            frame.setVisible(true);
        });
    }
}
