package TankGame.game;

import TankGame.GameConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author ulicessgg
 * using demo code provided by anthony-pc as base code
 */

public class PickUp
{

    private BufferedImage img;
    private float x;
    private float y;
    private Audio poweredUp;

    private boolean isPickedUp;
    private String power;

    PickUp(float x, float y, boolean isPickedUp, String power)
    {
        this.x = x;
        this.y = y;
        this.isPickedUp = isPickedUp;
        this.power = power;

        switch(power)
        {
            case "damage":
                try
                {
                    img = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/DoubleDamage.gif"), "Could not find DoubleDamage"));
                }
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case "health":
                try
                {
                    img = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/FullHealth.gif"), "Could not find FullHealth"));
                }
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case "life":
                try
                {
                    img = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/ExtraLife.gif"), "Could not find ExtraLife"));
                }
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case "speed":
                try
                {
                    img = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/SpeedBoost.gif"), "Could not find SpeedBoost"));
                }
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;
        }
    }

    void pickedUp()
    {
        this.isPickedUp = true;

        poweredUp = new Audio("power");
        poweredUp.playAudio();

        Timer timer = new Timer(6000, e ->
        {
            this.isPickedUp = false;
        });

        timer.setRepeats(true);
        timer.start();
    }

    boolean isPickedUp()
    {
        return isPickedUp;
    }

    String getPower()
    {
        return power;
    }

    Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    void drawImage(Graphics g)
    {
        if(!isPickedUp())
        {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
        }
    }
}
