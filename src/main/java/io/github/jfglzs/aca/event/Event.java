package io.github.jfglzs.aca.event;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Event<T> {
    private final Set<Consumer<T>> consumers = new HashSet<>();

    public void onUpdate(T arg) {
        this.consumers.forEach(consumer -> consumer.accept(arg));
    }

    public void onEvent(Consumer<T> consumer) {
        this.consumers.add(consumer);
    }
}
