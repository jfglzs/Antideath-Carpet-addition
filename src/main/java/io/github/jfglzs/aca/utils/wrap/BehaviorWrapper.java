package io.github.jfglzs.aca.utils.wrap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
//? if >= 26.1 {
/*import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
*///?}

public abstract class BehaviorWrapper<E extends LivingEntity> implements BehaviorControl<E> {
    protected final BehaviorControl<E> behaviorControl;

    protected BehaviorWrapper(BehaviorControl<E> behavior) {
        this.behaviorControl = behavior;
    }

    @Override
    public Behavior.Status getStatus() {
        return behaviorControl.getStatus();
    }

    //? if >= 26.1 {
    /*@Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return this.behaviorControl.getRequiredMemories();
    }
    *///?}

    @Override
    public boolean tryStart(ServerLevel serverLevel, E livingEntity, long l) {
        return behaviorControl.tryStart(serverLevel, livingEntity, l);
    }

    @Override
    public void tickOrStop(ServerLevel serverLevel, E livingEntity, long l) {
        behaviorControl.tickOrStop(serverLevel, livingEntity, l);
    }

    @Override
    public void doStop(ServerLevel serverLevel, E livingEntity, long l) {
        behaviorControl.doStop(serverLevel, livingEntity, l);
    }

    @Override
    public String debugString() {
        return "Wrapper[]";
    }
}
