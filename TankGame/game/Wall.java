package TankGame.game;

import TankGame.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Wall {
    private float x;
    private float y;
    private BufferedImage img;
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
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

    }
}
