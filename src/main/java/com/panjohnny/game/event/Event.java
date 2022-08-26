package com.panjohnny.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * The event implementation. The event has its caller, handler and D data.
 */
@Data
@AllArgsConstructor
@Getter
public abstract class Event<D> {
    private Class<?> eventCaller;
    private EventHandler eventHandler;
    private D data;

    /**
     * @param eventCaller The class that is calling the event.
     * @param data        Data that is being carried by the event.
     */
    public Event(Class<?> eventCaller, D data) {
        this.eventCaller = eventCaller;
        this.data = data;
    }

    /**
     * Sets handle.
     *
     * @param eventHandler The handler that handlers the event.
     * @return It self.
     */
    public Event<?> handle(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }
}
