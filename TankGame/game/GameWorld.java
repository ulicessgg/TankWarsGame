package TankGame.game;


import TankGame.GameConstants;
import TankGame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private BufferedImage background;
    private Tank t1;
    private Tank t2;
    private Map<Point, Wall> walls = new HashMap<>();
    private final Launcher lf;
    private long tick = 0;

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true)
            {
                this.tick++;
                this.t1.update();
                this.t2.update(); // update tank
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        }
        catch (InterruptedException ignored)
        {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame()
    {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
        this.t2.setX(980);
        this.t2.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame()
    {
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                GameConstants.GAME_SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        // adds the background bitmap to the game
        try
        {
            background = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/background.bmp"),
                            "Could not find background.bmp")
            );
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // sets the images for both player tanks
        BufferedImage t1img = null;
        try
        {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory. When running a jar, class loaders will read from within the jar.
             */
            t1img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/tank1.png"),
                    "Could not find tank1.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        BufferedImage t2img = null;
        try
        {
            t2img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/tank2.png"),
                            "Could not find tank2.png")
            );
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // sets the image for the unbreakable barrier walls
        BufferedImage wall1Img = null;
        try
        {
            wall1Img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/wall1.png"),
                            "Could not find wall1.png")
            );
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // sets the image for the breakable barrier walls
        BufferedImage wall2Img = null;
        try
        {
            wall2Img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/wall2.png"),
                            "Could not find wall1.png")
            );
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // creates the barrier walls
        for(int i = 0; i < 41; i++)
        {
            walls.put(new Point(i, 0), new Wall(i * wall1Img.getWidth(), 0, wall1Img, false));
            walls.put(new Point(i, 960), new Wall(i * wall1Img.getWidth(), 960, wall1Img, false));

        }
        for(int i = 1; i < 30; i++)
        {
            walls.put(new Point(0, i), new Wall(0, i * wall1Img.getWidth(), wall1Img, false));
            walls.put(new Point(1280, i), new Wall(1280, i * wall1Img.getWidth(), wall1Img, false));
        }

        // creates the obstacle walls
        for(int i = 6; i < 35; i++)
        {
            if(i < 15 || i > 25)
            {
                walls.put(new Point(i, 384), new Wall(i * wall1Img.getWidth(), 384, wall2Img, false));
                walls.put(new Point(i, 576), new Wall(i * wall1Img.getWidth(), 576, wall2Img, false));
            }
        }

        for(int i = 4; i < 27; i++)
        {
            if(i < 12 || i > 18)
            {
                walls.put(new Point(448, i), new Wall(448, i * wall1Img.getWidth(), wall2Img, false));
                walls.put(new Point(832, i), new Wall(832, i * wall1Img.getWidth(), wall2Img, false));
            }
        }

        // creates both player tanks
        t1 = new Tank(3, 100, 32, 32, 0, 0, (short) 0, t1img);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_Q);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(3, 100, 1232, 912, 0, 0, (short) 180, t2img);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_U, KeyEvent.VK_O);
        this.lf.getJf().addKeyListener(tc2);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        this.t1.drawImage(g2);
        this.t2.drawImage(g2);
        for(Wall wall : walls.values())
        {
            wall.drawImage(g2);
        }
    }
}
