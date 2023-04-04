import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create an orbit with a central point mass and an orbiting point mass

//            RenderablePointMass centralPointMass = new RenderablePointMass(400, 300, 5.972e24, 0, 0, 20);
//            RenderablePointMass orbitingPointMass = new RenderablePointMass(600, 300, 7.342e22, 0, 1.0e4, 5);
            RenderablePointMass centralPointMass = new RenderablePointMass(400, 300, 5.972e24, 0, 0, 20);
            RenderablePointMass orbitingPointMass = new RenderablePointMass(600, 300, 7, 0, 1, 5);
            Orbit orbit = new Orbit(centralPointMass);
            orbit.addOrbitingPointMass(orbitingPointMass);

            // Create an OrbitSimulation instance to render and update the orbit
            OrbitSimulation orbitSimulation = new OrbitSimulation(orbit, true);

            // Create a JFrame to display the OrbitSimulation
            JFrame frame = new JFrame("Orbit Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setContentPane(orbitSimulation);
            frame.setVisible(true);
        });
    }
}
