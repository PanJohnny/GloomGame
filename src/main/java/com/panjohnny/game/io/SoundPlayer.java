package com.panjohnny.game.io;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.Options;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.util.Objects;
import java.util.Random;

/**
 * @apiNote Plays only internal files.
 */
public final class SoundPlayer {
    public static synchronized void playSound(final String url) {
        playSound(url,1);
    }

    public static synchronized void playSound(final String url, final int loop) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
                        Objects.requireNonNull(GloomGame.class.getResourceAsStream(url))));
                clip.open(inputStream);
                clip.loop(loop);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-(gainControl.getMaximum() * Options.volume));
                clip.start();
                Thread.currentThread().join();
            } catch (Exception e) {
                throw new TrackError(url, e);
            }
        }, "sound-thread-" + new Random().nextInt()).start();
    }

    public static class TrackError extends Error {
        public TrackError(String url, Throwable cause) {
            super("Failed to play sound: %s".formatted(url), cause);
        }
    }
}
