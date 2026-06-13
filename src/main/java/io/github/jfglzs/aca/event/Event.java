package io.github.jfglzs.aca.event;

import io.github.jfglzs.aca.ACAEntry;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Event<T> {
    private final Set<Consumer<T>> consumers = new ReferenceOpenHashSet<>();

    public void update(T arg) {
        try {
            this.consumers.forEach(consumer -> consumer.accept(arg));
        }
        catch (Exception e) {
            ACAEntry.LOGGER.error("A Exception has thrown while passing event", e);
        }
    }

    public void register(Consumer<T> consumer) {
        this.consumers.add(consumer);
    }
}
