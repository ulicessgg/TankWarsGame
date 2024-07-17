package TankGame.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class LargeExplosion {
    private float x;
    private float y;
    private float angle;
    private List<BufferedImage> img;
    private boolean inert = false;
    private int frame = 0;

    LargeExplosion (float x, float y, float angle)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = new ArrayList<>();

        try
        {
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_lg/explosion_lg_0001.png"), "Could not find explosion_lg")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_lg/explosion_lg_0002.png"), "Could not find explosion_lg")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_lg/explosion_lg_0003.png"), "Could not find explosion_lg")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_lg/explosion_lg_0004.png"), "Could not find explosion_lg")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_lg/explosion_lg_0005.png"), "Could not find explosion_lg")));
            img.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("TankGame/resources/explosion_lg/explosion_lg_0006.png"), "Could not find explosion_lg")));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        Timer timer = new Timer(120, e ->
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

    boolean isInert()
    {
        return inert;
    }

    void drawImage(Graphics g)
    {
        if(!isInert())
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
