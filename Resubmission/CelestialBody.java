import java.awt.*;

/**
 * drawableBodyWithPositionVelocityRadiusAndColor
 */
public class CelestialBody {
    public double x, y;
    public double vx, vy;
    public int radius;
    public Color color;
    public boolean isStar;

    public CelestialBody(double x, double y, double vx, double vy, int radius, Color color, boolean isStar){
        this.x = x; this.y = y;
        this.vx = vx; this.vy = vy;
        this.radius = radius;
        this.color = color;
        this.isStar = isStar;
    }

    /** advanceOneTickByAddingVelocityToPosition */
    public void step(){
        x += vx;
        y += vy;
    }

    /** returnsTrueIfBodyIsCompletelyOffscreen */
    public boolean isOffscreen(int width, int height){
        return (x + radius < 0) || (x - radius > width) || (y + radius < 0) || (y - radius > height);
    }

    /** drawAsFilledCircleCenteredAtXY */
    public void draw(Graphics2D g){
        g.setColor(color);
        int d = radius * 2;
        g.fillOval((int)Math.round(x - radius), (int)Math.round(y - radius), d, d);
    }
}
