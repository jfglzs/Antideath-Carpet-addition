package io.github.jfglzs.aca.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ThreadUtils {
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public static <T> void parallel(List<T> list, int batchSize, Consumer<List<T>> processor, ExecutorService pool, boolean autoClose) {
        ExecutorService executor = pool;

        try {
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < list.size(); i += batchSize) {
                int from = i;
                int to = Math.min(i + batchSize, list.size());

                futures.add(executor.submit(() -> processor.accept(list.subList(from, to))));
            }

            for (Future<?> future : futures) {
                future.get();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        finally {
            if (autoClose) executor.shutdown();
        }
    }

    public static <T> void parallel(List<T> list, int batchSize, Consumer<List<T>> processor) {
        parallel(list, batchSize, processor, THREAD_POOL, false);
    }
}
