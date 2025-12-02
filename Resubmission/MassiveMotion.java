import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * Swing animation that spawns white stars from edges and an orange sun at center.
 * List implementation is chosen via MassiveMotion.txt (key: list).
 */
public class MassiveMotion extends JPanel implements ActionListener {

    protected Timer tm;

    // Window + animation defaults (overridden by MassiveMotion.txt)
    private int windowWidth = 640;
    private int windowHeight = 480;
    private final Random rng = new Random();

    // Will be constructed after reading 'list' from file
    private List<CelestialBody> bodies;

    // Animation knobs with defaults (overridden by MassiveMotion.txt)
    private int timerDelay = 75;
    private double genX = 0.06;     // spawn chance per tick on top/bottom
    private double genY = 0.06;     // spawn chance per tick on left/right
    private int bodySize = 10;      // radius of spawned white stars
    private int bodyVelocity = 3;   // max |vx| and |vy| for spawned stars
    private int starSize = 30;      // radius of the center orange sun
    private double starVX = 0;      // sun vx
    private double starVY = 0;      // sun vy
    private int starX;              // sun x (from file or centered)
    private int starY;              // sun y (from file or centered)

    /** Loads config, chooses list impl, sets up panel/timer, creates the sun. */
    public MassiveMotion() {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream("MassiveMotion.txt")) {
            p.load(fis);
        } catch (IOException ignored) { }

        // Choose list implementation from file
        String listChoice = p.getProperty("list", "arraylist").trim().toLowerCase();
        switch (listChoice) {
            case "arraylist":
                bodies = new ArrayList<>();
                break;
            case "linkedlist":
                bodies = new LinkedList<>();
                break;
            case "dummyheadlinkedlist":
                bodies = new DummyHeadLinkedList<>();
                break;
            case "doublylinkedlist":
                bodies = new DoublyLinkedList<>();
                break;
            default:
                System.out.println("Unknown list '" + listChoice + "'. Defaulting to ArrayListImpl.");
                bodies = new ArrayList<>();
        }

        // Load remaining config (or keep defaults)
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
        setBackground(Color.BLACK);

        tm = new Timer(timerDelay, this);

        // Add central sun
        bodies.add(new CelestialBody(starX, starY, starVX, starVY, starSize, Color.ORANGE, true));
    }

    /** Paints all bodies and ensures the timer runs. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).draw(g2);
        }

        tm.start();
    }

    /** One tick: move, cull offscreen, maybe spawn, repaint. */
    @Override
    public void actionPerformed(ActionEvent e) {
        // move bodies
        for (int i = 0; i < bodies.size(); i++) bodies.get(i).step();

        // remove offscreen (backward to keep indices valid)
        for (int i = bodies.size() - 1; i >= 0; i--) {
            if (bodies.get(i).isOffscreen(windowWidth, windowHeight)) bodies.remove(i);
        }

        // spawn new stars
        maybeSpawnXEdge();
        maybeSpawnYEdge();

        repaint();
    }

    /** Possibly spawn on top/bottom edges, push inward vertically. */
    private void maybeSpawnXEdge() {
        if (rng.nextDouble() < genX) {
            boolean top = rng.nextBoolean();
            int y = top ? 0 : windowHeight;
            int x = rng.nextInt(windowWidth);
            int[] v = nonZeroVelocity();
            int vy = top ? Math.max(1, v[1]) : Math.min(-1, v[1]);
            bodies.add(new CelestialBody(x, y, v[0], vy, bodySize, Color.WHITE, false));
        }
    }

    /** Possibly spawn on left/right edges, push inward horizontally. */
    private void maybeSpawnYEdge() {
        if (rng.nextDouble() < genY) {
            boolean left = rng.nextBoolean();
            int x = left ? 0 : windowWidth;
            int y = rng.nextInt(windowHeight);
            int[] v = nonZeroVelocity();
            int vx = left ? Math.max(1, v[0]) : Math.min(-1, v[0]);
            bodies.add(new CelestialBody(x, y, vx, v[1], bodySize, Color.WHITE, false));
        }
    }

    /** Random non-zero (vx, vy) with |v| ≤ bodyVelocity */
    private int[] nonZeroVelocity() {
        int vx = 0, vy = 0;
        while (vx == 0) vx = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        while (vy == 0) vy = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        return new int[]{vx, vy};
    }

    /** Parse int property or return default. */
    private static int getInt(Properties p, String key, int def){
        try { return Integer.parseInt(p.getProperty(key, String.valueOf(def)).trim()); }
        catch (Exception e){ return def; }
    }

    /** Parse double property or return default. */
    private static double getDouble(Properties p, String key, double def){
        try { return Double.parseDouble(p.getProperty(key, String.valueOf(def)).trim()); }
        catch (Exception e){ return def; }
    }

    /** Entry point: create and show the window. */
    public static void main(String[] args) {
        JFrame jf = new JFrame("Massive Motion");
        MassiveMotion mm = new MassiveMotion();
        jf.setSize(mm.windowWidth, mm.windowHeight);
        jf.add(mm);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("Massive Motion starting...");
    }
}
