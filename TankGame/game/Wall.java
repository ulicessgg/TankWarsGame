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

    Wall (float x, float y, BufferedImage img, boolean breakable)
    {
        this.x = x;
        this.y = y;
        this.img = img;
        this.breakable = breakable;
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
