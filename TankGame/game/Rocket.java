package TankGame.game;

import TankGame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * @author ulicessgg
 * using demo code provided by anthony-pc as base code
 */

public class Rocket
{
    private BufferedImage img;
    private Map<Point, Wall> wallIntel;
    private SmallExplosion thermite;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 6;
    private boolean isDestroyed = false;

    Rocket(float x, float y, float angle, BufferedImage img, Map<Point, Wall> wallIntel)
    {
        this.x = x;
        this.y = y;
        this.vx = (float) Math.cos(Math.toRadians(angle)) * R;
        this.vy = (float) Math.sin(Math.toRadians(angle)) * R;
        this.angle = angle;
        this.img = img;
        this.wallIntel = wallIntel;
    }

    Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    void destroy(boolean destroy)
    {
        this.isDestroyed = destroy;
        if(destroy)
        {
            thermite = new SmallExplosion(this.x, this.y, 0);
        }
    }

    boolean isDestroyed()
    {
        return isDestroyed;
    }

    void update()
    {
        if (!isDestroyed())
        {
            moveForwards();
        }

        if (isDestroyed())
        {
        }
    }

    private void moveForwards()
    {
        float newX = x + vx;
        float newY = y + vy;

        if (!checkCollision())
        {
            x = newX;
            y = newY;
            checkBorder();
        }
        else
        {
            destroy(true);
        }
    }

    private boolean checkCollision()
    {
        for(Wall wall : wallIntel.values())
        {
            if(wall.isBreakable() && !wall.isDestroyed() && wall.getBounds().intersects((getBounds())))
            {
                return true;
            }
        }
        return false;
    }

    private void checkBorder()
    {
        if (x < 32) {
            x = 32;
            destroy(true);
        }
        if (x >= GameConstants.GAME_SCREEN_WIDTH - 64) {
            x = GameConstants.GAME_SCREEN_WIDTH - 64;
            destroy(true);
        }
        if (y < 32) {
            y = 32;
            destroy(true);
        }
        if (y >= GameConstants.GAME_SCREEN_HEIGHT - 88) {
            y = GameConstants.GAME_SCREEN_HEIGHT - 88;
            destroy(true);
        }
    }

    void drawImage(Graphics g)
    {
        if(!isDestroyed())
        {
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.img, rotation, null);
        }
        if(isDestroyed())
        {
            if(thermite != null)
            {
                Graphics2D g2d = (Graphics2D) g;
                thermite.drawImage(g2d);
            }
        }
    }
}
