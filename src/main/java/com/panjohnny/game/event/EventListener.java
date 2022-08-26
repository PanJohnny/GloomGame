package com.panjohnny.game.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This is technical note.
 * I am aware that usage of reflections in personal project is not so good.
 * Lemme' explain it:
 * I want my project to be easily modifiable and extendable.
 * That means someone may code something that will say "Hello" when someone presses F11 button.
 * I am happy for that. Also I'm sure no-one will ever see this code. :(
 * But if you are reading this and this repo is really unpopular, send me some message please. All feedback is valuable.
 * +-----------------------------------------------------+
 * Oh this should be used for the class description.
 * +-----------------------------------------------------+
 * This class is an interface. It is used by the classes that wants to listen to methods. When the class is registered we store all the events listened to int the hashmap.
 * When the event is fired we check if the event is registered in the hashmap. If it is, we call the method.
 * We also do some witchcraft to prevent ClassCastException. :D
 *
 * @author PanJohnny
 */
public interface EventListener {
    HashMap<Class<? extends Event<?>>, List<MethodHandle>> methodHandles = new HashMap<>();

    default void dispatchEvent(Event<?> event) {
        if (!methodHandles.containsKey(event.getClass())) {
            return;
        }
        List<MethodHandle> hand = this.methodHandles.get(event.getClass());
        if (hand != null) {
            for (MethodHandle methodHandle : hand) {
                try {
                    /* This thing outsmarts system. (and also me :P, but I figured it out by myself debugging it)
                     * You see the methodHandle.type().toString() is "(EventListenerName,SomethingEvent)void"
                     * We check if this class is contained followed by ",".
                     * That is because we don't want to confuse Level and LevelSomething for example.
                     */
                    if (methodHandle.type().toString().contains(getClass().getSimpleName() + ","))
                        methodHandle.invoke(this, event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

    }

    default void cache() {
        for (Method m : Arrays.stream(this.getClass().getMethods()).filter(m -> m.isAnnotationPresent(EventTarget.class)).toList()) {
            try {
                Class<? extends Event<?>> target = m.getAnnotation(EventTarget.class).value();
                MethodHandle mh = MethodHandles.lookup().unreflect(m);
                if (!methodHandles.containsKey(target)) {
                    methodHandles.put(m.getAnnotation(EventTarget.class).value(), new ArrayList<>());
                }
                methodHandles.get(target).add(mh);
            } catch (IllegalAccessException e) {
                System.err.println("Failed to cache method: " + m.getName() + " for " + this.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

    default void dropCache() {
        methodHandles.clear();
    }

    default String createCacheDump() {
        StringBuilder sb = new StringBuilder();
        for (Class<? extends Event<?>> event : methodHandles.keySet()) {
            sb.append(event.getSimpleName()).append("\n");
            for (MethodHandle mh : methodHandles.get(event)) {
                sb.append("\t").append(mh.type().toMethodDescriptorString()).append("\n");
            }
        }
        return sb.toString();
    }
}
