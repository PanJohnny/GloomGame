package com.panjohnny.game.event;

import lombok.Getter;

import java.util.LinkedList;

public class EventHandler {
    @Getter
    private final LinkedList<EventListener> listeners = new LinkedList<>();

    public void register(EventListener listener) {
        if(listeners.contains(listener))
            return;
        listeners.add(listener);
        listener.cache();
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
        listener.dropCache();
    }

    public void fire(Event<?> event) {
        listeners.forEach(listener -> listener.dispatchEvent(event));
    }
}
