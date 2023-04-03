import java.awt.Graphics;

/**
 * Represents an object that can be drawn on the screen.
 */
public interface Renderable {
    /**
     * Returns the x-coordinate of the screen position.
     *
     * @return The x-coordinate of the screen position, any integer value.
     */
    int getScreenX();

    /**
     * Returns the y-coordinate of the screen position.
     *
     * @return The y-coordinate of the screen position, any integer value.
     */
    int getScreenY();

    /**
     * Draws the object on the screen using the provided Graphics object.
     *
     * @param graphics The Graphics object used for drawing the object on the screen, not null.
     */
    void draw(Graphics graphics);
}
