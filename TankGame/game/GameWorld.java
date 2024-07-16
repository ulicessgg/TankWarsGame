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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ulicessgg
 * using demo code provided by anthony-pc as base code
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private BufferedImage background;
    private Tank t1;
    private Tank t2;
    private Map<Point, Wall> barrierWalls = new HashMap<>();
    private Map<Point, Wall> obstacleWalls = new HashMap<>();
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
                tankCollision();
                rocketCollision();
                checkHealth();
                this.repaint();   // redraw game

                if(gameOver())
                {
                    String winner = getWinner();
                    System.out.println(winner);
                    SwingUtilities.invokeLater(() -> lf.setFrame("end"));
                    resetGame();
                    break;
                }

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
        InitializeGame();
    }

    public void resetPositions()
    {
        this.tick = 0;
        this.t1.setX(32);
        this.t1.setY(32);
        this.t2.setX(1232);
        this.t2.setY(912);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */

    public void loadWorld()
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
    }

    public void loadTanks()
    {
        // sets the images for both player tanks
        // player one
        BufferedImage t1img = null;
        try
        {
            t1img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/tank1.png"),
                            "Could not find tank1.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        // player two
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

        BufferedImage rimg = null;
        try
        {
            rimg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/Rocket.gif"),
                            "Could not find Rocket.gif")
            );
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // creates both player tanks
        t1 = new Tank(3, 100.00, 32, 32, 0, 0, (short) 0, t1img, obstacleWalls);
        TankControl tc1 = new TankControl(t1, rimg, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_E);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(3, 100.00, 1232, 912, 0, 0, (short) 180, t2img, obstacleWalls);
        TankControl tc2 = new TankControl(t2, rimg, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_U);
        this.lf.getJf().addKeyListener(tc2);
    }

    public void loadUnbreakableWalls()
    {
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

        // creates the barrier walls
        for(int i = 0; i < 41; i++)
        {
            barrierWalls.put(new Point(i, 0), new Wall(i * wall1Img.getWidth(), 0, wall1Img, false, false));
            barrierWalls.put(new Point(i, 960), new Wall(i * wall1Img.getWidth(), 960, wall1Img, false, false));

        }
        for(int i = 1; i < 30; i++)
        {
            barrierWalls.put(new Point(0, i), new Wall(0, i * wall1Img.getWidth(), wall1Img, false, false));
            barrierWalls.put(new Point(1280, i), new Wall(1280, i * wall1Img.getWidth(), wall1Img, false, false));
        }
    }

    public void loadBreakableWalls()
    {
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

        // creates the obstacle walls
        for(int i = 7; i < 34; i++)
        {
            if(i < 15 || i > 25)
            {
                obstacleWalls.put(new Point(i, 384), new Wall(i * wall2Img.getWidth(), 384, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 576), new Wall(i * wall2Img.getWidth(), 576, wall2Img, true, false));
            }
        }

        for(int i = 5; i < 26; i++)
        {
            if(i < 12 || i > 18)
            {
                obstacleWalls.put(new Point(448, i), new Wall(448, i * wall2Img.getWidth(), wall2Img, true, false));
                obstacleWalls.put(new Point(832, i), new Wall(832, i * wall2Img.getWidth(), wall2Img, true, false));
            }
        }
    }

    public void InitializeGame()
    {
        loadWorld();
        loadTanks();
        loadUnbreakableWalls();
        loadBreakableWalls();
    }

    public void tankCollision()
    {
        if(t1.getBounds().intersects(t2.getBounds()) && t2.getBounds().intersects(t1.getBounds()))
        {
            if(t1.getLives() > 0 && t2.getLives() > 0)
            {
                t1.loseLife();
                t2.loseLife();
                resetPositions();
            }
        }
    }

    public void rocketCollision()
    {
        Iterator<Rocket> iterator = t1.getRockets().iterator();
        while (iterator.hasNext())
        {
            Rocket rocket = iterator.next();
            if (rocket.getBounds().intersects(t2.getBounds()))
            {
                t2.loseHealth();
                iterator.remove();
                System.out.println("tank2 damaged");
            }

            for(Wall wall : obstacleWalls.values())
            {
                if(wall.isBreakable() && !wall.isDestroyed() && rocket.getBounds().intersects((wall.getBounds())))
                {
                    wall.destroy(true);
                    iterator.remove();
                    break;
                }
            }
        }

        iterator = t2.getRockets().iterator();
        while (iterator.hasNext())
        {
            Rocket rocket = iterator.next();
            if (rocket.getBounds().intersects(t1.getBounds()))
            {
                t1.loseHealth();
                iterator.remove();
                System.out.println("tank1 damaged");
            }

            for(Wall wall : obstacleWalls.values())
            {
                if(wall.isBreakable() && !wall.isDestroyed() && rocket.getBounds().intersects((wall.getBounds())))
                {
                    wall.destroy(true);
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public void checkHealth()
    {
        if(t1.getHealth() <= 0)
        {
            t1.loseLife();
            t1.setHealth();
            System.out.println("tank1 lost a life");
        }
        if(t2.getHealth() <= 0)
        {
            t2.loseLife();
            t2.setHealth();
            System.out.println("tank2 lost a life");
        }
    }

    public boolean gameOver()
    {
        return t1.getLives() == 0 || t2.getLives() == 0;
    }

    public String getWinner()
    {
        String winner = null;

        if(t1.getLives() > 0 && t2.getLives() == 0)
        {
            winner = "Player1";
        }
        if(t1.getLives() == 0 && t2.getLives() > 0)
        {
            winner = "Player2";
        }
        if(t1.getLives() == 0 && t2.getLives() == 0)
        {
            winner = "Draw";
        }

        return winner;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();

        buffer.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        for(Wall wall : barrierWalls.values())
        {
            wall.drawImage(buffer);
        }

        for(Wall wall : obstacleWalls.values())
        {
            wall.drawImage(buffer);
        }

        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);

        g2.drawImage(world, 0, 0, null);
    }
}
