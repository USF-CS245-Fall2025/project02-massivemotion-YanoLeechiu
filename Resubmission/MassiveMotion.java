import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * swingAnimationWithConfigDrivenList
 * Spawns white stars from edges and an orange sun at center.
 * List implementation is chosen via MassiveMotion.txt (key: list).
 */
public class MassiveMotion extends JPanel implements ActionListener {

    protected Timer tm;

    // windowAndAnimationDefaults (overridden by MassiveMotion.txt)
    private int windowWidth = 1024;
    private int windowHeight = 768;
    private final Random rng = new Random();

    // bodiesContainerConstructedAfterReadingListChoice
    private List<CelestialBody> bodies;

    // animationKnobsWithDefaults (overridden by MassiveMotion.txt)
    private int timerDelay = 75;
    private double genX = 0.06;     // spawnChanceTopBottom
    private double genY = 0.06;     // spawnChanceLeftRight
    private int bodySize = 10;      // whiteStarRadius
    private int bodyVelocity = 3;   // maxAbsVelocityForSpawnedStars
    private int starSize = 30;      // centerSunRadius
    private double starVX = 0;      // sunVelocityX
    private double starVY = 0;      // sunVelocityY
    private int starX;              // sunCenterX
    private int starY;              // sunCenterY

    /** loadConfigChooseListSetupPanelAndCreateSun */
    public MassiveMotion() {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream("MassiveMotion.txt")) {
            p.load(fis);
        } catch (IOException ignored) { }

        // chooseListImplementationFromFile
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

        // loadNumericAndPositionConfigOrUseDefaults
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

        // addCentralSun
        bodies.add(new CelestialBody(starX, starY, starVX, starVY, starSize, Color.ORANGE, true));
    }

    /** paintAllBodiesAndEnsureTimerRuns */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).draw(g2);
        }

        tm.start();
    }

    /** tickMoveCullSpawnAndRepaint */
    @Override
    public void actionPerformed(ActionEvent e) {
        // moveBodies
        for (int i = 0; i < bodies.size(); i++) bodies.get(i).step();

        // removeOffscreenBackwardToKeepIndicesValid
        for (int i = bodies.size() - 1; i >= 0; i--) {
            if (bodies.get(i).isOffscreen(windowWidth, windowHeight)) bodies.remove(i);
        }

        // maybeSpawnNewStars
        maybeSpawnXEdge();
        maybeSpawnYEdge();

        repaint();
    }

    /** maybeSpawnFromTopOrBottomAndPushInwardVertically */
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

    /** maybeSpawnFromLeftOrRightAndPushInwardHorizontally */
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

    /** randomNonZeroVelocityWithinBodyVelocity */
    private int[] nonZeroVelocity() {
        int vx = 0, vy = 0;
        while (vx == 0) vx = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        while (vy == 0) vy = rng.nextInt(bodyVelocity * 2 + 1) - bodyVelocity;
        return new int[]{vx, vy};
    }

    /** parseIntOrDefault */
    private static int getInt(Properties p, String key, int def){
        try { return Integer.parseInt(p.getProperty(key, String.valueOf(def)).trim()); }
        catch (Exception e){ return def; }
    }

    /** parseDoubleOrDefault */
    private static double getDouble(Properties p, String key, double def){
        try { return Double.parseDouble(p.getProperty(key, String.valueOf(def)).trim()); }
        catch (Exception e){ return def; }
    }

    /** createWindowAndStartProgram */
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
