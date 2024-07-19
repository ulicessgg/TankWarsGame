package TankGame.game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio
{
    private Clip sound;
    private AudioInputStream file;
    private String path;

    Audio(String wav)
    {
        switch(wav)
        {
            case "music":
                path = "TankGame/resources/Music.wav";
                break;
            case "muzzle":
                path = "TankGame/resources/Explosion_small.wav";
                break;
            case "small":
                path = "TankGame/resources/Explosion_small.wav";
                break;
            case "large":
                path = "TankGame/resources/Explosion_large.wav";
                break;
            case "power":
                path = "TankGame/resources/PoweredUp.wav";
                break;
            default:
                break;
        }

        try
        {
            file = AudioSystem.getAudioInputStream(new File(path));

            AudioFormat type = file.getFormat();
            DataLine.Info ext = new DataLine.Info(Clip.class, type);

            sound = (Clip) AudioSystem.getLine(ext);
            sound.open(file);
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    void playAudio()
    {
        if(sound != null)
        {
            sound.setFramePosition(0);
            sound.start();
        }
    }

    void loopAudio()
    {
        if(sound != null)
        {
            sound.setFramePosition(0);
            sound.loop(sound.LOOP_CONTINUOUSLY);
        }
    }

    void stopAudio()
    {
        if(sound != null)
        {
            sound.stop();
        }
    }
}
