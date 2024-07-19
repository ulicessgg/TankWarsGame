package TankGame.game;

import TankGame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * @author ulicessgg
 * using demo code provided by anthony-pc as base code
 */

public class Tank
{

    private int lives;
    private double health;
    private double maxHealth;
    private double dps;
    private BufferedImage img;
    private Map<Point, Wall> wallIntel;
    private List<Rocket> rockets = new ArrayList<>();
    private List<MuzzleFlash> flashes = new ArrayList<>();
    private SmallExplosion thermite;
    private LargeExplosion c4;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R;
    private float ROTATIONSPEED = 1.50f;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean madeImpact;
    private boolean isDestroyed;

    Tank(int lives, double health, float x, float y, float vx, float vy, float angle, BufferedImage img, Map<Point, Wall> wallIntel)
    {
        this.lives = lives;
        this.health = health;
        this.maxHealth = health;
        this.dps = 16.67;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.R = 3;
        this.img = img;
        this.wallIntel = wallIntel;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    float getX(){ return x; }

    float getY() { return y;}

    Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    void gainLife()
    {
        this.lives = lives + 1;
    }

    void loseLife()
    {
        this.lives = lives - 1;
    }

    int getLives()
    {
        return this.lives;
    }

    void loseHealth()
    {
        this.health = health - dps;
    }

    void setHealth()
    {
        this.health = 100.0;
    }

    double getHealth()
    {
        return this.health;
    }

    void increaseDamage()
    {
        dps = dps * 2;
    }

    void increaseSpeed()
    {
        R = R * (float) 1.5;
    }

    void decreaseSpeed()
    {
        R = R / (float) 2;
    }

    public List<Rocket> getRockets()
    {
        return rockets;
    }

    void impact(boolean madeImpact)
    {
        this.madeImpact = madeImpact;
        if(madeImpact)
        {
            thermite = new SmallExplosion(this.x, this.y, this.angle);
        }
    }

    boolean madeImpact()
    {
        return madeImpact;
    }

    void destroy(boolean destroy)
    {
        this.isDestroyed = destroy;
        if(destroy)
        {
            c4 = new LargeExplosion(this.x, this.y, this.angle);
        }
    }

    boolean isDestroyed()
    {
        return isDestroyed;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update()
    {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        for (Rocket rocket : rockets)
        {
            rocket.update();
        }
    }

    private void rotateLeft()
    {
        this.angle -= this.ROTATIONSPEED;

        if(this.angle < 0)
        {
            this.angle = 360;
        }
    }

    private void rotateRight()
    {
        this.angle += this.ROTATIONSPEED;

        if(this.angle > 360)
        {
            this.angle = 0;
        }
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

    private void moveBackwards()
    {
        float tempX = x;
        float tempY = y;

        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        if(!checkCollision(x - vx, y - vy))
        {
            x -= vx;
            y -= vy;
            checkBorder();
        }
        else
        {
            if(angle <= 90 && angle >= 270)
            {
                x = tempX + 1;
            }
            if(angle <= 180 && angle >= 0)
            {
                y = tempY + 1;
            }
            if(angle <= 90 && angle >= 0)
            {
                x = tempX + 1;
                y = tempY + 1;
            }
            if(angle <= 270 && angle >= 90)
            {
                x = tempX - 1;
            }
            if(angle <= 360 && angle >= 180)
            {
                y = tempY - 1;
            }
            if(angle <= 270 && angle >= 180)
            {
                x = tempX - 1;
                y = tempY - 1;
            }
        }
    }

    public void fireRocket(BufferedImage rimg)
    {
        float offset = 16;
        float rocketStartX = x + (float) Math.cos(Math.toRadians(angle)) * (img.getWidth() / 2 + offset);
        float rocketStartY = y + (float) Math.sin(Math.toRadians(angle)) * (img.getHeight() / 2 + offset);
        Rocket rocket = new Rocket(rocketStartX, rocketStartY, angle, rimg, wallIntel);
        rockets.add(rocket);
        MuzzleFlash round = new MuzzleFlash(rocketStartX, rocketStartY, angle);
        flashes.add(round);
    }

    private boolean checkCollision(float x, float y)
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

    void drawImage(Graphics g)
    {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;

        if(!madeImpact() || !isDestroyed())
        {
            g2d.drawImage(this.img, rotation, null);

            for (int i = 0; i < lives; i++)
            {
                g2d.drawImage(img, (int) (x - 6) + ((i * 20) + 3), (int) y - 48, 16, 16, null);
            }

            g2d.setColor(Color.RED);
            g2d.fillRect((int) x - 6, (int) y - 24, 60, 6);

            int currentHealthWidth = (int) ((double) health / maxHealth * 60);
            g2d.setColor(Color.GREEN);
            g2d.fillRect((int) x - 6, (int) y - 24, currentHealthWidth, 6);

            for (MuzzleFlash flash : flashes)
            {
                flash.drawImage(g2d);
                for (Rocket rocket : rockets)
                {
                    rocket.drawImage(g2d);
                }
            }
        }

        if(madeImpact())
        {
            g2d.drawImage(this.img, rotation, null);

            for (int i = 0; i < lives; i++)
            {
                g2d.drawImage(img, (int) (x - 6) + ((i * 16) + 4), (int) y - 48, 16, 16, null);
            }

            g2d.setColor(Color.RED);
            g2d.fillRect((int) x - 6, (int) y - 24, 60, 6);

            int currentHealthWidth = (int) ((double) health / maxHealth * 60);
            g2d.setColor(Color.GREEN);
            g2d.fillRect((int) x - 6, (int) y - 24, currentHealthWidth, 6);

            for (MuzzleFlash flash : flashes) {
                flash.drawImage(g2d);
                for (Rocket rocket : rockets) {
                    rocket.drawImage(g2d);
                }
            }

            thermite.drawImage(g2d);
        }

        if(isDestroyed())
        {
            c4.drawImage(g2d);
        }
    }
}
