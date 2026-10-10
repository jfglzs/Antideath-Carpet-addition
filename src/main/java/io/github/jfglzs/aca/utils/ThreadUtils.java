package io.github.jfglzs.aca.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
}
