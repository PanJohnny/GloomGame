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
    private static Clip clip;

    public static synchronized void playSound(final String url) {
        playSound(url, 1);
    }

    public static synchronized void playSound(final String url, final int loop) {
        if (clip != null)
            stopSound();
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
                        Objects.requireNonNull(GloomGame.class.getResourceAsStream(url))));
                clip.open(inputStream);
                clip.loop(loop);
                clip.start();
                while (true) {
                    if (clip.isOpen()) break;
                    // wait
                }
                setVolume(Options.getFloat(Options.Option.VOLUME));
                Thread.currentThread().join();
            } catch (Exception e) {
                throw new TrackError(url, e);
            }
        }, "sound-thread-" + new Random().nextInt()).start();
    }

    public static void stopSound() {
        clip.stop();
    }

    public static float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public static void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);

        if (clip == null)
            return;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public static class TrackError extends Error {
        public TrackError(String url, Throwable cause) {
            super("Failed to play sound: %s".formatted(url), cause);
        }
    }
}
