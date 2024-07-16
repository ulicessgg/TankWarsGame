package TankGame.game;

import TankGame.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Wall {
    private float x;
    private float y;
    private BufferedImage img;
    private LargeExplosion c4;
    private boolean breakable;
    private boolean destroyed;

    Wall (float x, float y, BufferedImage img, boolean breakable, boolean destroyed)
    {
        this.x = x;
        this.y = y;
        this.img = img;
        this.breakable = breakable;
        this.destroyed = destroyed;
    }

    void setX(float x)
    {
        this.x = x;
    }

    void setY(float y)
    {
        this. y = y;
    }

    boolean isBreakable()
    {
        return breakable;
    }

    void destroy(boolean destroy)
    {
        this.destroyed = destroy;
        if(destroy)
        {
            c4 = new LargeExplosion(this.x, this.y, 0);
        }
    }

    boolean isDestroyed()
    {
        return destroyed;
    }

    Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    @Override
    public String toString()
    {
        return "x=" + x + ", y=" + y + ", breakable=" + breakable;
    }


    void drawImage(Graphics g)
    {
        if(!isDestroyed())
        {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
        }
        if(isDestroyed())
        {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            Graphics2D g2d = (Graphics2D) g;
            c4.drawImage(g2d);
        }
    }
}
