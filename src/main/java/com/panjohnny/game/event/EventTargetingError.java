package com.panjohnny.game.event;


public class EventTargetingError extends Error{

    public EventTargetingError(String message){
        super(message);
    }

    public EventTargetingError(String message, Throwable cause){
        super(message, cause);
    }
}

