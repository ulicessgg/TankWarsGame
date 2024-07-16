package TankGame.game;

import TankGame.GameConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class SmallExplosion {
    private float x;
    private float y;
    private float angle;
    private List<BufferedImage> img;
    private boolean inert = false;
    private int frame = 0;

    SmallExplosion (float x, float y, float angle)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = new ArrayList<>();

        try
        {
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_sm/explosion_sm_0001.png"), "Could not find explosion_sm")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_sm/explosion_sm_0002.png"), "Could not find explosion_sm")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_sm/explosion_sm_0003.png"), "Could not find explosion_sm")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_sm/explosion_sm_0004.png"), "Could not find explosion_sm")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_sm/explosion_sm_0005.png"), "Could not find explosion_sm")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_sm/explosion_sm_0006.png"), "Could not find explosion_sm")));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        Timer timer = new Timer(150, e ->
        {
            frame++;
            if(frame >= img.size())
            {
                inert = true;
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    void setX(float x)
    {
        this.x = x;
    }

    void setY(float y)
    {
        this. y = y;
    }

    void inert(boolean inert)
    {
        this.inert = inert;
    }

    boolean isInert()
    {
        return inert;
    }

    @Override
    public String toString()
    {
        return "x=" + x + ", y=" + y + ", inert=" + inert;
    }


    void drawImage(Graphics g)
    {
        if(!inert)
        {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            Graphics2D g2d = (Graphics2D) g;
            for(int i = 0; i < img.size(); i++)
            {
                g2d.drawImage(this.img.get(frame), rotation, null);
            }
        }
    }
}
