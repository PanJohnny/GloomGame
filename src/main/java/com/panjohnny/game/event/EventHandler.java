package com.panjohnny.game.event;

import lombok.Getter;

import java.util.LinkedList;

public class EventHandler {
    @Getter
    private final LinkedList<EventListener> listeners = new LinkedList<>();

    /**
     * Registers a listener.
     *
     * @param listener Listener to register.
     */
    public void register(EventListener listener) {
        if (listeners.contains(listener))
            return;
        listeners.add(listener);
        listener.cache();
    }

    /**
     * Unregisters listener and clears it cache.
     *
     * @param listener The listener to unregister.
     */
    public void unregister(EventListener listener) {
        listeners.remove(listener);
        listener.dropCache();
    }

    /**
     * Fires an event.
     *
     * @param event Event to dispatch to all the listeners.
     * @implNote If the listener wants that event, is handled on its side. Which may be a security flaw.
     */
    public void fire(Event<?> event) {
        // prevent concurrent modification exception
        LinkedList<EventListener> listeners = new LinkedList<>(this.listeners);
        for (EventListener listener : listeners) {
            listener.dispatchEvent(event.handle(this));
        }
    }
}
