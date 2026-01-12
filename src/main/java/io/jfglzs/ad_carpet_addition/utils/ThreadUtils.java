package io.jfglzs.ad_carpet_addition.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static final Object lock = new Object();

    public static ExecutorService threadPool = Executors.newCachedThreadPool();
}
