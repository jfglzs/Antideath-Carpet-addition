package io.github.jfglzs.aca.utils.wrap;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
//? if >= 26.1 {
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
//?}

public class LimitedBehaviorWrapper<E extends LivingEntity> implements BehaviorControl<E> {
    private final BehaviorControl<E> behaviorControl;
    private boolean shouldSkip = false;

    private LimitedBehaviorWrapper(BehaviorControl<E> behavior) {
        this.behaviorControl = behavior;
    }

    public static <E extends LivingEntity> LimitedBehaviorWrapper<E> wrap(BehaviorControl<E> behavior) {
        return new LimitedBehaviorWrapper<>(behavior);
    }

    @Override
    public Behavior.Status getStatus() {
        return this.shouldSkip ? Behavior.Status.RUNNING : this.behaviorControl.getStatus();
    }

    //? if >= 26.1 {
    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return behaviorControl.getRequiredMemories();
    }
    //?}

    @Override
    public boolean tryStart(ServerLevel serverLevel, E livingEntity, long l) {
        if ((((livingEntity.tickCount ^ livingEntity.getId())) & 255) == 0) this.shouldSkip = EntityUtils.shouldSkip(livingEntity);
        this.shouldSkip = this.shouldSkip && AcaSetting.villagerOptimization;
        return this.shouldSkip && this.tryStart(serverLevel, livingEntity, l);
    }

    @Override
    public void doStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (!this.shouldSkip) this.behaviorControl.doStop(serverLevel, livingEntity, l);
    }

    @Override
    public String debugString() {
        return "";
    }

    @Override
    public void tickOrStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (!this.shouldSkip) this.behaviorControl.tickOrStop(serverLevel, livingEntity, l);
    }
}
