import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * Swing animation with a config driven list.
 * Spawns white stars from the edges and an orange sun at the center.
 * The list implementation is chosen via MassiveMotion.txt using the key "list".
 */
public class MassiveMotion extends JPanel implements ActionListener {

    /** Swing timer that drives the animation ticks. */
    protected Timer tm;

    /** Window width set by config or default. */
    private int windowWidth = 1024;

    /** Window height set by config or default. */
    private int windowHeight = 768;

    /** Random number generator for spawning and velocities. */
    private final Random rng = new Random();

    /** Container of bodies constructed after reading the list choice. */
    private List<CelestialBody> bodies;

    /** Timer delay in milliseconds. */
    private int timerDelay = 75;

    /** Per tick spawn probability on top or bottom edges. */
    private double genX = 0.06;

    /** Per tick spawn probability on left or right edges. */
    private double genY = 0.06;

    /** Radius of newly spawned white stars. */
    private int bodySize = 10;

    /** Maximum absolute component speed for spawned stars. */
    private int bodyVelocity = 3;

    /** Radius of the center orange sun. */
    private int starSize = 30;

    /** Sun velocity along x. */
    private double starVX = 0;

    /** Sun velocity along y. */
    private double starVY = 0;

    /** Sun center x coordinate. */
    private int starX;

    /** Sun center y coordinate. */
    private int starY;

    /**
     * Load config, choose the list implementation, set up the panel and timer, and create the sun.
     */
    public MassiveMotion() {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream("MassiveMotion.txt")) {
            p.load(fis);
        } catch (IOException ignored) { }

        /** Choose list implementation from file. */
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
                System.out.println("Unknown list '" + listChoice + "'. Defaulting to ArrayList.");
                bodies = new ArrayList<>();
        }

        /** Load numeric and position config or keep defaults. */
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

        /** Initialize canvas and background color. */
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(Color.BLACK);

        /** Initialize and start the Swing timer with the configured delay. */
        tm = new Timer(timerDelay, this);

        /** Add the central sun to the bodies list. */
        bodies.add(new CelestialBody(starX, starY, starVX, starVY, starSize, Color.ORANGE, true));
    }

    /**
     * Paint all bodies and ensure the timer runs.
     * @param g the graphics context provided by Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).draw(g2);
        }

        tm.start();
    }

    /**
     * One animation tick: move bodies, cull offscreen, maybe spawn new ones, and repaint.
     * @param e the action event fired by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /** Move each body according to its velocity. */
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).step();
        }

        /** Remove offscreen bodies by iterating backward to keep indices valid. */
        for (int i = bodies.size() - 1; i >= 0; i--) {
            if (bodies.get(i).isOffscreen(windowWidth, windowHeight)) {
                bodies.remove(i);
            }
        }

        /** Possibly spawn new stars from the edges. */
        maybeSpawnXEdge();
        maybeSpawnYEdge();

        /** Request a repaint for the next frame. */
        repaint();
    }

    /**
     * Possibly spawn a star from the top or bottom edge and push inward vertically.
     */
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

    /**
     * Possibly spawn a star from the left or right edge and push inward horizontally.
     */
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

    /**
     * Generate a random non zero velocity vector where each component is in
     * the closed interval [-bodyVelocity, bodyVelocity].
     * @return a two element array containing vx and vy
     */
    private int[] nonZeroVelocity() {
        int vx = 0, vy = 0;
        while (vx == 0) vx = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        while (vy == 0) vy = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        return new int[]{vx, vy};
    }

    /**
     * Parse an integer property or return the provided default.
     * @param p    the loaded properties
     * @param key  the property key
     * @param def  the default value
     * @return the parsed integer or the default
     */
    private static int getInt(Properties p, String key, int def){
        try {
            return Integer.parseInt(p.getProperty(key, String.valueOf(def)).trim());
        } catch (Exception e){
            return def;
        }
    }

    /**
     * Parse a double property or return the provided default.
     * @param p    the loaded properties
     * @param key  the property key
     * @param def  the default value
     * @return the parsed double or the default
     */
    private static double getDouble(Properties p, String key, double def){
        try {
            return Double.parseDouble(p.getProperty(key, String.valueOf(def)).trim());
        } catch (Exception e){
            return def;
        }
    }

    /**
     * Create the window and start the program.
     * @param args command line arguments (unused)
     */
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
