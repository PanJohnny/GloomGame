package com.panjohnny.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public abstract class Event<D> {
    private Class<?> eventCaller;
    private EventHandler eventHandler;
    private D data;

    public Event<?> handle(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    public Event(Class<?> eventCaller, D data) {
        this.eventCaller = eventCaller;
        this.data = data;
    }
}
