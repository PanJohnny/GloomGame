package com.panjohnny.game.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
                    // this is done to avoid class cast exceptions IDK why, but it works
                    if (methodHandle.toString().contains(this.getClass().getSimpleName()))
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
