package com.panjohnny.game.util;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class Timer<C> {
    private final long duration;
    private final Consumer<C> callback;
    private long startTime;
    private long endTime;
    private boolean done;

    public Timer(long duration, Consumer<C> consumer) {
        this(duration, consumer, false);
    }

    private Timer(long duration, Consumer<C> consumer, boolean done) {
        startTime = System.nanoTime();
        this.duration = millisToNanos(duration);
        endTime = startTime + duration;
        this.callback = consumer;
        this.done = done;
    }

    public static long millisToNanos(long millis) {
        return millis * 1000000;
    }

    public static <T> Timer<T> createDummyTimer(Consumer<T> consumer, long duration) {
        return new Timer<>(duration, consumer, true);
    }

    public void tick(C callbackContext) {
        if (!isDone() && System.nanoTime() >= endTime) {
            callback.accept(callbackContext);

            stop();
        }
    }

    public void stop() {
        startTime = 0;
        endTime = 0;

        done = true;
    }

    public void reset() {
        startTime = System.nanoTime();
        endTime = startTime + duration;
        done = false;
    }
}
