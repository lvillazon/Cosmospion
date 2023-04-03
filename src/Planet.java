import java.awt.Color;
import java.awt.Graphics;

public class Planet extends PointMass implements Renderable {
    private final double radius;
    private final double atmosphericDensity;

    /**
     * Creates a new Planet with the given position, mass, velocity, radius, and atmospheric density.
     *
     * @param x                 The x-coordinate of the position, any double value.
     * @param y                 The y-coordinate of the position, any double value.
     * @param mass              The mass of the planet, must be a positive value.
     * @param velocityX         The x-component of the velocity, any double value.
     * @param velocityY         The y-component of the velocity, any double value.
     * @param radius            The radius of the planet, must be a positive value.
     * @param atmosphericDensity The atmospheric density of the planet, any double value.
     * @throws IllegalArgumentException if mass or radius is negative.
     */
    public Planet(double x, double y, double mass, double velocityX, double velocityY, double radius, double atmosphericDensity) {
        super(x, y, mass, velocityX, velocityY);
        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.radius = radius;
        this.atmosphericDensity = atmosphericDensity;
    }

    /**
     * Returns the radius of the planet.
     *
     * @return The radius of the planet, a positive value.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Returns the atmospheric density of the planet.
     *
     * @return The atmospheric density of the planet, any double value.
     */
    public double getAtmosphericDensity() {
        return atmosphericDensity;
    }

    @Override
    public int getScreenX() {
        // Convert the x-coordinate of the position to screen coordinates.
        // This is a placeholder implementation. Replace it with your own conversion logic.
        return (int) getX();
    }

    @Override
    public int getScreenY() {
        // Convert the y-coordinate of the position to screen coordinates.
        // This is a placeholder implementation. Replace it with your own conversion logic.
        return (int) getY();
    }

    @Override
    public void draw(Graphics graphics) {
        // Set the desired color for the planet
        graphics.setColor(Color.BLUE);

        // Draw the planet as a filled circle centered on the PointMass
        int screenX = getScreenX();
        int screenY = getScreenY();
        int screenRadius = (int) radius; // Convert the radius to screen coordinates
        int adjustedScreenRadius = Math.max(screenRadius, 5); // Ensure a minimum size of 5
        graphics.fillOval(
                screenX - adjustedScreenRadius,
                screenY - adjustedScreenRadius,
                2 * adjustedScreenRadius,
                2 * adjustedScreenRadius);
    }
}
