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

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private BufferedImage background;
    private Tank t1;
    private Tank t2;
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
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory. When running a jar, class loaders will read from within the jar.
             */
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

        t1 = new Tank(300, 300, 0, 0, (short) 0, t1img);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_Q);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(980, 300, 0, 0, (short) 180, t2img);
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
    }
}
