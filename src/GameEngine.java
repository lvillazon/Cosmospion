import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEngine extends JFrame implements ActionListener {
    private Renderer renderer;

    public GameEngine() {
        setTitle("Sprite Rotation Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        renderer = new Renderer();
        add(renderer);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        Timer timer = new Timer(1000 / 60, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        renderer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameEngine::new);
    }
}
