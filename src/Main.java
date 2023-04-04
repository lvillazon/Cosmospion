import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create an orbit with a central point mass and an orbiting point mass
            // Using simplified Earth-Moon system as an example

            // Earth mass (in arbitrary units): 5.972e24 kg -> 5.972
            // Moon mass (in arbitrary units): 7.342e22 kg -> 0.07342
            double centralPointMassMass = 5.972;
            double orbitingPointMassMass = 0.07342;

            // Earth-Moon average distance (in arbitrary units): 384400 km -> 384.4
            double earthMoonDistance = 384.4;

            // Moon's orbital velocity (in arbitrary units): 1.022 km/s -> 1.022
            double moonOrbitalVelocity = 1.022;

            // Screen dimensions
            int screenWidth = 800;
            int screenHeight = 600;

            RenderablePointMass centralPointMass = new RenderablePointMass(0, 0, centralPointMassMass, 0, 0, 50);
            RenderablePointMass orbitingPointMass = new RenderablePointMass(earthMoonDistance, 0, orbitingPointMassMass, 0, moonOrbitalVelocity, 50);
            Orbit orbit = new Orbit(centralPointMass);
            orbit.addOrbitingPointMass(orbitingPointMass);

            // Create an OrbitSimulation instance to render and update the orbit
            OrbitSimulation orbitSimulation = new OrbitSimulation(orbit, false); // Start in single-step mode

            // Create a JFrame to display the OrbitSimulation
            JFrame frame = new JFrame("Orbit Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setContentPane(orbitSimulation);
            frame.setVisible(true);
        });
    }
}
