package com.panjohnny.game.event;

import com.google.common.cache.LoadingCache;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public interface EventListener {
    HashMap<Class<? extends Event<?>>, List<MethodHandle>> methodHandles = new HashMap<>();

    default void dispatchEvent(Event<?> event) {
        List<MethodHandle> hand = this.methodHandles.get(event.getClass());
        if (hand != null) {
            for (MethodHandle methodHandle : hand) {
                try {
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
                Class<? extends Event<?>> target = m.getAnnotation(EventTarget.class).target();
                MethodHandle mh = MethodHandles.lookup().findVirtual(this.getClass(), m.getName(), MethodType.methodType(void.class, target));
                if (!methodHandles.containsKey(target)) {
                    methodHandles.put(m.getAnnotation(EventTarget.class).target(), new ArrayList<>());
                }
                methodHandles.get(target).add(mh);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                System.err.println("Failed to cache method: " + m.getName() + " for " + this.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

    default void dropCache() {
        methodHandles.clear();
    }

    default String createCacheDump(){
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
