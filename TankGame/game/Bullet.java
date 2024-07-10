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
public class Bullet{

    private int lives;
    private double health;

    private Map<Point, Wall> wallIntel;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private int buffer = -480;
    private float angle;

    private float R = 3;

    private BufferedImage img;
    private boolean ShootPressed;

    Bullet(int lives, double health, float x, float y, float vx, float vy, float angle, BufferedImage img, Map<Point, Wall> wallIntel)
    {
        this.lives = lives;
        this.health = health;
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        this.wallIntel = wallIntel;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    float getX(){ return x; }

    float getY() { return y;}

    void toggleShootPressed()
    {
        this.ShootPressed = true;
    }

    void unToggleShootPressed()
    {
        this.ShootPressed = false;
    }

    private void moveForwards()
    {
        float tempX = x;
        float tempY = y;

        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        if(!checkCollision(x + vx, y + vy))
        {
            x += vx;
            y += vy;
            checkBorder();
        }
        else
        {
            if(angle <= 90 && angle >= 270)
            {
                x = tempX - 1;
            }
            if(angle <= 180 && angle >= 0)
            {
                y = tempY - 1;
            }
            if(angle <= 90 && angle >= 0)
            {
                x = tempX - 1;
                y = tempY - 1;
            }
            if(angle <= 270 && angle >= 90)
            {
                x = tempX + 1;
            }
            if(angle <= 360 && angle >= 180)
            {
                y = tempY + 1;
            }
            if(angle <= 270 && angle >= 180)
            {
                x = tempX + 1;
                y = tempY + 1;
            }
        }
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

    private boolean checkCollision(float x, float y)
    {
        Rectangle temp = new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
        for(Wall wall : wallIntel.values())
        {
            if(wall.getBounds().intersects((getBounds())))
            {
                return true;
            }
        }
        return false;
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
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

    }
}
