import java.awt.*;

public class CelestialBody {
    public double x, y;
    public double vx, vy;
    public int radius;
    public Color color;
    public boolean isStar;


    public CelestialBody(double x, double y, double vx, double vy, int radius, Color color, boolean isStar){
        // position
        this.x = x; this.y = y;
        // movement
        this.vx = vx; this.vy = vy;
        this.radius = radius;
        this.color = color;
        this.isStar = isStar;
    }
    // progression of movement
    public void step(){
        x += vx;
        y += vy;
    }
    // check if a star is off-screen to be removed if true
    public boolean isOffscreen(int w, int h){
        return (x + radius < 0) || (x - radius > w) || (y + radius < 0) || (y - radius > h);
    }

    public void draw(Graphics2D g){
        g.setColor(color);
        int d = radius * 2;
        g.fillOval((int)Math.round(x - radius), (int)Math.round(y - radius), d, d);
    }
}
