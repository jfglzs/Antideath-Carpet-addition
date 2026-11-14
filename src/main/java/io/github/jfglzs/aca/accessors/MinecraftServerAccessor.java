package io.github.jfglzs.aca.accessors;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface MinecraftServerAccessor {
    List<Future<?>> asa$getFutures();
    ExecutorService asa$getExecutor();
}
