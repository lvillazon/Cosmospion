/**
 * Represents a point mass in 2D space with position, mass, and velocity.
 * The position and velocity are mutable, but mass is immutable.
 */
public class PointMass {
    private double x;
    private double y;
    private final double mass;
    private double velocityX;
    private double velocityY;

    /**
     * Creates a new PointMass with the given position, mass, and velocity.
     *
     * @param x         The x-coordinate of the position, any double value.
     * @param y         The y-coordinate of the position, any double value.
     * @param mass      The mass of the point mass, must be a positive value.
     * @param velocityX The x-component of the velocity, any double value.
     * @param velocityY The y-component of the velocity, any double value.
     * @throws IllegalArgumentException if mass is negative.
     */
    public PointMass(double x, double y, double mass, double velocityX, double velocityY) {
        if (mass < 0) {
            throw new IllegalArgumentException("Mass must be positive");
        }
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public PointMass(PointMass other) {
        this.x = other.x;
        this.y = other.y;
        this.velocityX = other.velocityX;
        this.velocityY = other.velocityY;
        this.mass = other.mass;
    }


    /**
     * Returns the x-coordinate of the position.
     *
     * @return The x-coordinate of the position, any double value.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the position.
     *
     * @return The y-coordinate of the position, any double value.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the mass of the point mass.
     *
     * @return The mass of the point mass, a positive value.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Returns the x-component of the velocity.
     *
     * @return The x-component of the velocity, any double value.
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Returns the y-component of the velocity.
     *
     * @return The y-component of the velocity, any double value.
     */
    public double getVelocityY() {
        return velocityY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    // You may want to override equals(), hashCode(), and toString() as well.
}
