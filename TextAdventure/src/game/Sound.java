/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author Lee17017
 */
class MusicPlayer {

}

public class Sound {

    private static int bgmAnz = 2;
    private static Clip[] bgmClip;
    private static int bgmCur = -1;

    private static int sfxAnz = 1;
    private static Clip[] sfxClip;
    private static int sfxCur = -1;

    public static void init() {
        try {

            bgmClip = new Clip[bgmAnz];
            for (int i = 0; i < bgmAnz; i++) {
                bgmClip[i] = AudioSystem.getClip();
                bgmClip[i].open(AudioSystem.getAudioInputStream(new File("src/ressources/sound/bgm" + i + ".wav")));
            }

            sfxClip = new Clip[sfxAnz];
            for (int i = 0; i < sfxAnz; i++) {
                sfxClip[i] = AudioSystem.getClip();
                sfxClip[i].open(AudioSystem.getAudioInputStream(new File("src/ressources/sound/sfx" + i + ".wav")));
            }

        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    

    public static void playBGM(int track) {

        if (bgmCur >=0 && bgmClip[bgmCur].isRunning()) {
            bgmClip[bgmCur].stop();
        }

        bgmCur = track;
        if(track < 0)
            return;
        bgmClip[track].setFramePosition(0);
        bgmClip[track].loop(Clip.LOOP_CONTINUOUSLY);
    }


    
    public static void setBGMVol(float amount)
    { if (bgmCur >=0 && bgmClip[bgmCur].isRunning()) {
            ((FloatControl) bgmClip[bgmCur].getControl(FloatControl.Type.MASTER_GAIN)).setValue(amount);
        }
    }
     
    
    public static void playSFX(int track) {
       if (sfxCur >=0 && sfxClip[sfxCur].isRunning()) {
            sfxClip[sfxCur].stop();
        }

        sfxCur = track;
        if(track < 0)
            return;
        sfxClip[track].setFramePosition(0);
        sfxClip[track].loop(Clip.LOOP_CONTINUOUSLY);
    }


    public static void setSFXVol(float amount) {
        if (sfxCur >=0 && sfxClip[sfxCur].isRunning()) {
            ((FloatControl) sfxClip[sfxCur].getControl(FloatControl.Type.MASTER_GAIN)).setValue(amount);
        }
    }

    public static void main(String[] args) throws Exception {

        Sound.init();
        Sound.playBGM(0);

        Thread.sleep(4000);

        Sound.playBGM(-1);

        Thread.sleep(2000);
        Sound.playSFX(0);
        Thread.sleep(3000);
        Sound.setSFXVol(-30);
        Thread.sleep(2000);
        Sound.playBGM(0);
        Thread.sleep(40000);
        System.out.println("end");

    }
}
