package TankGame.game;

import TankGame.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;

/**
 * @author ulicessgg
 * using demo code provided by anthony-pc as base code
 */
public class Rocket
{

    private Map<Point, Wall> wallIntel;
    private float x;
    private float y;
    private float angle;
    private float R = 6;
    private BufferedImage img;
    private boolean hot;

    Rocket(float x, float y, float angle, boolean hot, BufferedImage img)
    {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        this.hot = true;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    float getX(){ return x; }

    float getY() { return y;}

    void setHot(boolean hot)
    {
        this.hot = hot;
    }

    boolean getHot()
    {
        return hot;
    }

    void update() {
        this.x += R * Math.cos(Math.toRadians(angle));
        this.y += R * Math.sin(Math.toRadians(angle));

        if (x < 0 || x > GameConstants.GAME_SCREEN_WIDTH || y < 0 || y > GameConstants.GAME_SCREEN_HEIGHT)
        {
            this.hot = false;
        }

        //System.out.println(angle); // this is for debugging the collision
    }


    private void checkBorder()
    {
        if (x < 32) {
            x = 32;
        }
        if (x >= GameConstants.GAME_SCREEN_WIDTH - 96) {
            x = GameConstants.GAME_SCREEN_WIDTH - 96;
        }
        if (y < 32) {
            y = 32;
        }
        if (y >= GameConstants.GAME_SCREEN_HEIGHT - 120) {
            y = GameConstants.GAME_SCREEN_HEIGHT - 120;
        }
    }

    public boolean collidesWith(Tank tank) {
        return tank.getBounds().intersects(this.getBounds());
    }

    public boolean collidesWith(Wall wall) {
        return wall.getBounds().intersects(this.getBounds());
    }

    Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    void drawImage(Graphics g)
    {
        g.drawImage(img, (int) x, (int) y, null);
    }
}
