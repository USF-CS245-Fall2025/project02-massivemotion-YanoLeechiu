import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A drawable body in the simulation with position, velocity, radius, and color.
 * Can advance over time, test if it has left the visible area, and render itself.
 */
public class CelestialBody {

    /** X-coordinate of the body's center (in pixels). */
    public double positionX;

    /** Y-coordinate of the body's center (in pixels). */
    public double positionY;

    /** Horizontal velocity in pixels per tick (Δx). */
    public double velocityX;

    /** Vertical velocity in pixels per tick (Δy). */
    public double velocityY;

    /** Radius of the body in pixels. */
    public int radius;

    /** Fill color used when drawing this body. */
    public Color color;

    /** True if this body is the central “sun”, false for spawned stars. */
    public boolean isStar;

    /**
     * Construct a {@code CelestialBody}.
     *
     * @param positionX  initial x-coordinate (pixels)
     * @param positionY  initial y-coordinate (pixels)
     * @param velocityX  initial horizontal velocity (pixels/tick)
     * @param velocityY  initial vertical velocity (pixels/tick)
     * @param radius     body radius (pixels)
     * @param color      fill color for rendering
     * @param isStar     whether this body represents the central sun
     */
    public CelestialBody(double positionX,
                         double positionY,
                         double velocityX,
                         double velocityY,
                         int radius,
                         Color color,
                         boolean isStar) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.radius = radius;
        this.color = color;
        this.isStar = isStar;
    }

    /**
     * Advance the body by one simulation tick by applying its velocity.
     * Updates {@link #positionX} and {@link #positionY}.
     */
    public void step() {
        positionX += velocityX;
        positionY += velocityY;
    }

    /**
     * Determine whether the body is completely outside the visible viewport.
     *
     * @param width   viewport width in pixels
     * @param height  viewport height in pixels
     * @return {@code true} if no part of the body is within the rectangle
     *         {@code [0,width] × [0,height]}, otherwise {@code false}
     */
    public boolean isOffscreen(int width, int height) {
        return (positionX + radius < 0)
                || (positionX - radius > width)
                || (positionY + radius < 0)
                || (positionY - radius > height);
    }

    /**
     * Render the body as a filled circle centered at its current position.
     *
     * @param g graphics context (already configured by caller)
     */
    public void draw(Graphics2D g) {
        g.setColor(color);
        int diameter = radius * 2;
        int drawX = (int) Math.round(positionX - radius);
        int drawY = (int) Math.round(positionY - radius);
        g.fillOval(drawX, drawY, diameter, diameter);
    }
}
