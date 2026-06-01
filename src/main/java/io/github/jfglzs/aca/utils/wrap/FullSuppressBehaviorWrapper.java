package io.github.jfglzs.aca.utils.wrap;

import io.github.jfglzs.aca.utils.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
//? if >= 26.1 {
/*import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
*///?}

public class FullSuppressBehaviorWrapper<E extends LivingEntity> extends BehaviorWrapper<E> {
    protected FullSuppressBehaviorWrapper(BehaviorControl<E> behavior) {
        super(behavior);
    }

    public static <E extends LivingEntity> BehaviorWrapper<E> wrap(BehaviorControl<E> behavior) {
        return new FullSuppressBehaviorWrapper<>(behavior);
    }

    //? if >= 26.1 {
    /*@Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return behaviorControl.getRequiredMemories();
    }
    *///?}


    @Override
    public boolean tryStart(ServerLevel serverLevel, E livingEntity, long l) {
        return EntityUtils.shouldSkip(livingEntity) || super.tryStart(serverLevel, livingEntity, l);
    }

    @Override
    public void doStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (!EntityUtils.shouldSkip(livingEntity)) {
            super.doStop(serverLevel, livingEntity, l);
        }
    }

    @Override
    public void tickOrStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (!EntityUtils.shouldSkip(livingEntity)) {
            super.tickOrStop(serverLevel, livingEntity, l);
        }
    }
}
