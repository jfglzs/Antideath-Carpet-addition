package io.github.jfglzs.aca.event;

import io.github.jfglzs.aca.AcaExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Event<T> {
    private final Set<Consumer<T>> consumers = new HashSet<>();

    public void onUpdate(T arg) {
        try {
            this.consumers.forEach(consumer -> consumer.accept(arg));
        } catch (Exception e) {
            AcaExtension.LOGGER.error("Exception has thrown while processing event", e);
        }
    }

    public void onEvent(Consumer<T> consumer) {
        this.consumers.add(consumer);
    }
}
