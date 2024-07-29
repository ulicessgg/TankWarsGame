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

/**
 * @author ulicessgg
 * using demo code provided by anthony-pc as base code
 */

public class GameWorld extends JPanel implements Runnable
{
    private int windowWidth = GameConstants.SCREEN_WIDTH / 2;
    private int windowHeight = GameConstants.SCREEN_HEIGHT;
    private BufferedImage world;
    private BufferedImage background;
    private Tank t1;
    private Tank t2;
    private Map<Point, Wall> barrierWalls = new HashMap<>();
    private Map<Point, Wall> obstacleWalls = new HashMap<>();
    private Map<Point, PickUp> powerUps = new HashMap<>();
    private Audio music;
    private int miniMapWidth = GameConstants.GAME_SCREEN_WIDTH / 8;
    private int miniMapHeight = GameConstants.GAME_SCREEN_HEIGHT / 8;
    private final Launcher lf;
    public String winner;

    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                this.t1.update();
                this.t2.update(); // update tank
                pickUpCollision();
                tankCollision();
                rocketCollision();
                checkHealth();
                this.repaint();   // redraw game

                if(gameOver())
                {
                    getWinner();
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
        t1 = new Tank(3, 100.00, 48, 88, 0, 0, (short) 0, t1img, obstacleWalls);
        TankControl tc1 = new TankControl(t1, rimg, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Q, KeyEvent.VK_E);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(3, 100.00, 1792, 928, 0, 0, (short) 180, t2img, obstacleWalls);
        TankControl tc2 = new TankControl(t2, rimg, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_U, KeyEvent.VK_O);
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
        for(int i = 0; i < 59; i++)
        {
            barrierWalls.put(new Point(i, 0), new Wall(i * wall1Img.getWidth(), 0, wall1Img, false, false));
            barrierWalls.put(new Point(i, 992), new Wall(i * wall1Img.getWidth(), 992, wall1Img, false, false));

        }
        for(int i = 1; i < 31; i++)
        {
            barrierWalls.put(new Point(0, i), new Wall(0, i * wall1Img.getWidth(), wall1Img, false, false));
            barrierWalls.put(new Point(1856, i), new Wall(1856, i * wall1Img.getWidth(), wall1Img, false, false));
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
        for(int i = 1; i < 58; i++)
        {
            if(i > 3 && i < 14)
            {
                obstacleWalls.put(new Point(i, 208), new Wall(i * wall2Img.getWidth(), 208, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 400), new Wall(i * wall2Img.getWidth(), 400, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 592), new Wall(i * wall2Img.getWidth(), 592, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 784), new Wall(i * wall2Img.getWidth(), 784, wall2Img, true, false));
            }
            if(i > 19 && i < 27)
            {
                obstacleWalls.put(new Point(i, 208), new Wall(i * wall2Img.getWidth(), 208, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 400), new Wall(i * wall2Img.getWidth(), 400, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 592), new Wall(i * wall2Img.getWidth(), 592, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 784), new Wall(i * wall2Img.getWidth(), 784, wall2Img, true, false));
            }
            if(i > 31 && i < 39)
            {
                obstacleWalls.put(new Point(i, 208), new Wall(i * wall2Img.getWidth(), 208, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 400), new Wall(i * wall2Img.getWidth(), 400, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 592), new Wall(i * wall2Img.getWidth(), 592, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 784), new Wall(i * wall2Img.getWidth(), 784, wall2Img, true, false));
            }
            if(i > 44 && i < 55)
            {
                obstacleWalls.put(new Point(i, 208), new Wall(i * wall2Img.getWidth(), 208, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 400), new Wall(i * wall2Img.getWidth(), 400, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 592), new Wall(i * wall2Img.getWidth(), 592, wall2Img, true, false));
                obstacleWalls.put(new Point(i, 784), new Wall(i * wall2Img.getWidth(), 784, wall2Img, true, false));
            }
        }
        for(int i = 1; i < 31; i++)
        {
            if(i < 7)
            {
                obstacleWalls.put(new Point(608, i), new Wall(608, (i * wall2Img.getWidth()), wall2Img, true, false));
                obstacleWalls.put(new Point(1248, i), new Wall(1248, (i * wall2Img.getWidth()), wall2Img, true, false));
            }
            if(i > 6 && i <12)
            {
                obstacleWalls.put(new Point(160, i), new Wall(160, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(448, i), new Wall(448, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(1408, i), new Wall(1408, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(1696, i), new Wall(1696, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
            }

            if(i > 12 && i < 18)
            {
                obstacleWalls.put(new Point(608, i), new Wall(608, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(1248, i), new Wall(1248, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
            }

            if(i > 18 && i < 24)
            {
                obstacleWalls.put(new Point(160, i), new Wall(160, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(448, i), new Wall(448, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(1408, i), new Wall(1408, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
                obstacleWalls.put(new Point(1696, i), new Wall(1696, (i * wall2Img.getWidth()) + 16, wall2Img, true, false));
            }

            if(i > 24)
            {
                obstacleWalls.put(new Point(608, i), new Wall(608, (i * wall2Img.getWidth()), wall2Img, true, false));
                obstacleWalls.put(new Point(1248, i), new Wall(1248, (i * wall2Img.getWidth()), wall2Img, true, false));
            }
        }
    }

    public void loadPowerUps()
    {
        powerUps.put(new Point(288, 288), new PickUp(288, 288, false, "life"));
        powerUps.put(new Point(1552, 288), new PickUp(1552, 288, false, "health"));
        powerUps.put(new Point(288, 672), new PickUp(288, 672, false, "speed"));
        powerUps.put(new Point(1552, 672), new PickUp(1552, 672, false, "damage"));
    }

    public void InitializeGame()
    {
        loadWorld();
        loadTanks();
        loadUnbreakableWalls();
        loadBreakableWalls();
        loadPowerUps();
        music = new Audio("music");
        music.loopAudio();
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame()
    {
        music.stopAudio();
        InitializeGame();
    }

    public void resetPositions()
    {
        this.t1.setX(48);
        this.t1.setY(88);
        this.t2.setX(1792);
        this.t2.setY(928);
    }

    public void pickUpCollision()
    {
        for(PickUp pickUp : powerUps.values())
        {
            if(!pickUp.isPickedUp() && t1.getBounds().intersects(pickUp.getBounds()))
            {
                pickUp.pickedUp();

                String temp = pickUp.getPower();

                switch(temp)
                {
                    case "damage":
                        t2.increaseDamage();
                        t1.decreaseSpeed();
                        break;
                    case "health":
                        t1.setHealth();
                        break;
                    case "life":
                        t1.gainLife();
                        break;
                    case "speed":
                        t1.increaseSpeed();
                        break;
                    default:
                        break;
                }
            }
        }

        for(PickUp pickUp : powerUps.values())
        {
            if(!pickUp.isPickedUp() && t2.getBounds().intersects(pickUp.getBounds()))
            {
                pickUp.pickedUp();

                String temp = pickUp.getPower();

                switch(temp)
                {
                    case "damage":
                        t1.increaseDamage();
                        t2.decreaseSpeed();
                        break;
                    case "health":
                        t2.setHealth();
                        break;
                    case "life":
                        t2.gainLife();
                        break;
                    case "speed":
                        t2.increaseSpeed();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void tankCollision()
    {
        if(t1.getBounds().intersects(t2.getBounds()) && t2.getBounds().intersects(t1.getBounds()))
        {
            if(t1.getLives() > 0 && t2.getLives() > 0)
            {
                t1.loseLife();
                t2.loseLife();
                t1.destroy(true);
                t2.destroy(true);
                resetPositions();
            }
        }
    }

    public void rocketCollision()
    {
        List <Rocket> toBeDeleted = new ArrayList<>();

        for (Rocket rocket : t1.getRockets())
        {
            boolean removed = false;
            if (rocket.getBounds().intersects(t2.getBounds()))
            {
                rocket.destroy(true);
                t2.loseHealth();
                t2.impact(true);
                toBeDeleted.add(rocket);
                removed = true;
            }

            if(!removed)
            {
                for(Wall wall : obstacleWalls.values())
                {
                    if(wall.isBreakable() && !wall.isDestroyed() && rocket.getBounds().intersects((wall.getBounds())))
                    {
                        rocket.destroy(true);
                        wall.destroy(true);
                        toBeDeleted.add(rocket);
                        removed = true;
                        break;
                    }
                }
            }
        }

        t1.getRockets().removeAll(toBeDeleted);
        toBeDeleted.clear();

        for (Rocket rocket : t2.getRockets())
        {
            boolean removed = false;
            if (rocket.getBounds().intersects(t1.getBounds()))
            {
                rocket.destroy(true);
                t1.loseHealth();
                t1.impact(true);
                toBeDeleted.add(rocket);
                removed = true;
            }

            if(!removed)
            {
                for(Wall wall : obstacleWalls.values())
                {
                    if(wall.isBreakable() && !wall.isDestroyed() && rocket.getBounds().intersects((wall.getBounds())))
                    {
                        rocket.destroy(true);
                        wall.destroy(true);
                        toBeDeleted.add(rocket);
                        removed = true;
                        break;
                    }
                }
            }
        }

        t2.getRockets().removeAll(toBeDeleted);
        toBeDeleted.clear();
    }

    public void checkHealth()
    {
        if(t1.getHealth() <= 0)
        {
            t1.loseLife();
            t1.setHealth();
            t1.destroy(true);
            System.out.println("tank1 lost a life");
            resetPositions();
        }
        if(t2.getHealth() <= 0)
        {
            t2.loseLife();
            t2.setHealth();
            t2.destroy(true);
            System.out.println("tank2 lost a life");
            resetPositions();
        }
    }

    public boolean gameOver()
    {
        return t1.getLives() == 0 || t2.getLives() == 0;
    }

    public void setWinner(String winner)
    {
        this.winner = winner;
    }

    public void getWinner()
    {
        String winner = null;

        if(t1.getLives() > 0 && t2.getLives() == 0)
        {
            winner = "Player 1 Wins!";
        }
        if(t1.getLives() == 0 && t2.getLives() > 0)
        {
            winner = "Player 2 Wins!";
        }
        if(t1.getLives() == 0 && t2.getLives() == 0)
        {
            winner = "Draw";
        }

        setWinner(winner);
    }

    public String getResult()
    {
        return winner;
    }

    public int getCamX(int x)
    {
        int offset = windowWidth / 2;
        int tCamX = Math.max(0, Math.min(x - offset, GameConstants.GAME_SCREEN_WIDTH - windowWidth));

        return tCamX;
    }

    public int getCamY(int y)
    {
        int offset = windowHeight / 2;
        int tCamY = Math.max(0, Math.min(y - offset, GameConstants.GAME_SCREEN_HEIGHT - windowHeight));

        return tCamY;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();

        buffer.drawImage(background, 0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT, null);

        for(Wall wall : barrierWalls.values())
        {
            wall.drawImage(buffer);
        }

        for(Wall wall : obstacleWalls.values())
        {
            wall.drawImage(buffer);
        }

        for(PickUp pickUp : powerUps.values())
        {
            pickUp.drawImage(buffer);
        }

        t1.drawImage(buffer);
        t2.drawImage(buffer);

        g2.drawImage(world.getSubimage(getCamX((int) t1.getX()), getCamY((int) t1.getY()), windowWidth, windowHeight), 0, 0, windowWidth, windowHeight, null);
        g2.drawImage(world.getSubimage(getCamX((int) t2.getX()), getCamY((int) t2.getY()), windowWidth, windowHeight), windowWidth, 0, windowWidth, windowHeight, null);

        g2.setColor(Color.BLACK);
        g2.fillRect(windowWidth - 4, 0, 8, windowHeight);

        g2.setColor(Color.BLACK);
        g2.fillRect(517, 393, miniMapWidth + 8, miniMapHeight + 8);

        g2.drawImage(world, 521, 397, miniMapWidth, miniMapHeight, null);
    }
}
