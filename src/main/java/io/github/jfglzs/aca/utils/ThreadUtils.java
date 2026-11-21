package io.github.jfglzs.aca.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ThreadUtils {
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    public static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "ACA-TASK");
        t.setDaemon(true);
        return t;
    });

    public static void schedule(Runnable task, long delayMs) {
        SCHEDULER.schedule(task, delayMs, TimeUnit.MILLISECONDS);
    }

    public static <T> void parallel(List<T> list, int batchSize, Consumer<List<T>> processor, ExecutorService pool, boolean autoShutDown) {
        try {
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < list.size(); i += batchSize) {
                int from = i;
                int to = Math.min(i + batchSize, list.size());

                futures.add(pool.submit(() -> processor.accept(list.subList(from, to))));
            }

            for (Future<?> future : futures) {
                future.get();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        finally {
            if (autoShutDown) pool.shutdown();
        }
    }

    public static <T> void parallel(List<T> list, int batchSize, Consumer<List<T>> processor) {
        parallel(list, batchSize, processor, THREAD_POOL, false);
    }
}
