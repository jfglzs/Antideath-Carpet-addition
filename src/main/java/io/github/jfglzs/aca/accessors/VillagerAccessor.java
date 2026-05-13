package io.github.jfglzs.aca.accessors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Path;

public interface VillagerAccessor {
    boolean aca$canDisableAI();
    int aca$getPOIRequests();
    void aca$addPOIRequest();
    Path aca$getPath();
    void aca$setPath(Path path);
}
