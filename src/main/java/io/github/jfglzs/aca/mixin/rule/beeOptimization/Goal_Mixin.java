package io.github.jfglzs.aca.mixin.rule.beeOptimization;

import io.github.jfglzs.aca.accessors.GoalAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Goal.class)
public class Goal_Mixin implements GoalAccessor {
    @Unique
    private boolean aca$accessible = false;
    @Unique
    private Entity aca$Entity = null;

    @Override
    public void aca$setAccessible(boolean accessible) {
        this.aca$accessible = accessible;
    }

    @Override
    public boolean aca$getAccessiblie() {
        return aca$accessible;
    }

    @Override
    public void aca$setEntity(Entity entity) {
        this.aca$Entity = entity;
    }

    @Override
    public Entity aca$getEntity() {
        return this.aca$Entity;
    }
}
