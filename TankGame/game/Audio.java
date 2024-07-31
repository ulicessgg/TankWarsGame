package TankGame.game;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Audio
{
    private Clip sound;

    Audio(String wav)
    {
        InputStream audioSrc;
        try
        {
            switch(wav)
            {
                case "music":
                    audioSrc = getClass().getClassLoader().getResourceAsStream("TankGame/resources/Music.wav");
                    break;
                case "muzzle":
                    audioSrc = getClass().getClassLoader().getResourceAsStream("TankGame/resources/Explosion_small.wav");
                    break;
                case "small":
                    audioSrc = getClass().getClassLoader().getResourceAsStream("TankGame/resources/Explosion_small.wav");
                    break;
                case "large":
                    audioSrc = getClass().getClassLoader().getResourceAsStream("TankGame/resources/Explosion_large.wav");
                    break;
                case "power":
                    audioSrc = getClass().getClassLoader().getResourceAsStream("TankGame/resources/PoweredUp.wav");
                    break;
                default:
                    audioSrc = null;
                    break;
            }

            if (audioSrc == null) {
                throw new IOException("Audio file not found: " + wav);
            }

            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream file = AudioSystem.getAudioInputStream(bufferedIn);
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
