package io.github.jfglzs.aca.accessors;

import java.util.List;
import java.util.concurrent.Future;

public interface ServerLevelAccessor {
    List<Future<?>> getFeture();
}
