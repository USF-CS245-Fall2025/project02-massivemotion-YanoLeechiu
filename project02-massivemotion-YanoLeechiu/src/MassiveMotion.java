import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class MassiveMotion extends JPanel implements ActionListener {

    protected Timer tm;


    // Window + animation
    private int windowWidth = 640;
    private int windowHeight = 480;
    private final Random rng = new Random();

    // Make sure ArrayListImpl<T> is in src/
    private final List<CelestialBody> bodies = new ArrayList<>();

    // constant values for animation
    private int timerDelay = 75;
    private double genX = 0.06;   // star spawn chance per tick on top/bottom
    private double genY = 0.06;   // star spawn chance per tick on left/right
    private int bodySize = 10;    // radius of white comets
    private int bodyVelocity = 3; // max |vx| and |vy| for spawned stars
    private int starSize = 30;    // radius of center sun (orange)
    private double starVX = 0;    // sun velocity (x)
    private double starVY = 0;    // sun velocity (y)
    private int starX;
    private int starY;

    // loads file, sets up panel/timer, and creates the center orange sun.
    public MassiveMotion() {
        // Reading:
        Properties p = new Properties();

        // hehe, Fish
        try (FileInputStream fish = new FileInputStream("MassiveMotion.txt")) {
            p.load(fish);
        } catch (IOException ignored) { }


        // reading and getting data from Massive Motion.txt
        timerDelay   = getInt(p, "timer_delay",   timerDelay);
        windowWidth  = getInt(p, "window_size_x", windowWidth);
        windowHeight = getInt(p, "window_size_y", windowHeight);

        genX         = getDouble(p, "gen_x", genX);
        genY         = getDouble(p, "gen_y", genY);
        bodySize     = getInt(p, "body_size", bodySize);
        bodyVelocity = getInt(p, "body_velocity", bodyVelocity);

        starSize     = getInt(p, "star_size", starSize);
        starVX       = getDouble(p, "star_velocity_x", starVX);
        starVY       = getDouble(p, "star_velocity_y", starVY);
        starX        = getInt(p, "star_position_x", windowWidth / 2);
        starY        = getInt(p, "star_position_y", windowHeight / 2);

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(Color.BLACK); // black background = space vacuum

        tm = new Timer(timerDelay, this);

        // The Sun
        bodies.add(new CelestialBody(starX, starY, starVX, starVY, starSize, Color.ORANGE, true));
    }

    // Paints the current frame: clears background and draws sun + stars.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw sun first, then comets
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).draw(g2);
        }

        // Start timer
        tm.start();
    }

    // advances simulation, spawns new stars
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Move every body
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).step();
        }
        // Remove off-screen bodies (iterate backwards)
        for (int i = bodies.size() - 1; i >= 0; i--) {
            if (bodies.get(i).isOffscreen(windowWidth, windowHeight)) { // use of isOffscreen
                bodies.remove(i);
            }
        }
        // Spawn new white stars/comets from edges
        maybeSpawnXEdge();
        maybeSpawnYEdge();

        // Keep this at the end of the function (no matter what you do above):
        repaint();
    }

    // Randomly spawns a star on the top/bottom edge and sends to screen
    private void maybeSpawnXEdge(){ // top or bottom edge
        if (rng.nextDouble() < genX){
            boolean top = rng.nextBoolean();
            int y = top ? 0 : windowHeight;
            int x = rng.nextInt(windowWidth);
            int[] v = nonZeroVelocity();
            int vy = top ? Math.max(1, v[1]) : Math.min(-1, v[1]); // push inward
            bodies.add(new CelestialBody(x, y, v[0], vy, bodySize, Color.WHITE, false)); // isStar=false
        }
    }

    // Randomly spawns a star on the left/right edge and sends to screen
    private void maybeSpawnYEdge(){ // left or right edge
        if (rng.nextDouble() < genY){
            boolean left = rng.nextBoolean();
            int x = left ? 0 : windowWidth;
            int y = rng.nextInt(windowHeight);
            int[] v = nonZeroVelocity();
            int vx = left ? Math.max(1, v[0]) : Math.min(-1, v[0]); // push inward
            bodies.add(new CelestialBody(x, y, vx, v[1], bodySize, Color.WHITE, false)); // isStar=false
        }
    }

    // Picks a random (vx, vy) in [-bodyVelocity, bodyVelocity] until neither is zero.
    private int[] nonZeroVelocity(){
        int vx = 0, vy = 0;
        while (vx == 0) vx = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        while (vy == 0) vy = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        return new int[]{vx, vy};
    }

    // parse an int property or return default.
    private static int getInt(Properties p, String key, int def){
        try { return Integer.parseInt(p.getProperty(key, String.valueOf(def)).trim()); }
        catch (Exception e){ return def; }
    }

    // parse a double property or return default.
    private static double getDouble(Properties p, String key, double def){
        try { return Double.parseDouble(p.getProperty(key, String.valueOf(def)).trim()); }
        catch (Exception e){ return def; }
    }

    public static void main(String[] args) {
        System.out.println("Massive Motion starting...");
        MassiveMotion mm = new MassiveMotion();

        JFrame jf = new JFrame();
        jf.setTitle("Massive Motion");
        jf.setSize(mm.windowWidth, mm.windowHeight);
        jf.add(mm);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
