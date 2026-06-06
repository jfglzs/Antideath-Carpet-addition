package io.github.jfglzs.aca.accessors;

import net.minecraft.world.entity.Entity;

public interface GoalAccessor {
    void aca$setAccessible(boolean accessible);
    boolean aca$getAccessiblie();
    void aca$setEntity(Entity entity);
    Entity aca$getEntity();
}
