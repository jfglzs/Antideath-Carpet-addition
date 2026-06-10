package io.github.jfglzs.aca.utils.wrap;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
//? if >= 26.1 {
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jspecify.annotations.NonNull;

import java.util.Set;
//?}

public class FullSuppressBehaviorWrapper<E extends LivingEntity> implements BehaviorControl<E> {
    private final BehaviorControl<E> behaviorControl;
    private boolean shouldSkip = true;

    private FullSuppressBehaviorWrapper(BehaviorControl<E> behavior) {
        this.behaviorControl = behavior;
    }

    public static <E extends LivingEntity> BehaviorControl<E> wrap(BehaviorControl<E> behavior) {
        return new FullSuppressBehaviorWrapper<>(behavior);
    }

    @Override
    public Behavior.Status getStatus() {
        return this.shouldSkip ? null : this.behaviorControl.getStatus();
    }

    //? if >= 26.1 {
    @Override
    public @NonNull Set<MemoryModuleType<?>> getRequiredMemories() {
        return this.behaviorControl.getRequiredMemories();
    }
    //?}

    @Override
    public boolean tryStart(ServerLevel serverLevel, E livingEntity, long l) {
        this.shouldSkip = this.shouldSkip && AcaSetting.villagerOptimization;
        if ((livingEntity.tickCount ^ livingEntity.getId() & 511) == 0) this.shouldSkip = EntityUtils.shouldSkip(livingEntity);
        return this.shouldSkip || this.behaviorControl.tryStart(serverLevel, livingEntity, l);
    }

    @Override
    public void doStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (!this.shouldSkip) this.behaviorControl.doStop(serverLevel, livingEntity, l);
    }

    @Override
    public @NonNull String debugString() {
        return "FullBehaviorWrapper[%s]".formatted(behaviorControl.debugString());
    }

    @Override
    public void tickOrStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (!this.shouldSkip) this.behaviorControl.tickOrStop(serverLevel, livingEntity, l);
    }
}
