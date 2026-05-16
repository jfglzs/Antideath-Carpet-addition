package io.github.jfglzs.aca.utils.wrap;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.npc.villager.Villager;
//? if >= 26.1 {
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
//?}

public class BehaviorWrapper<E extends LivingEntity> implements BehaviorControl<E> {
    private final BehaviorControl<E> behaviorControl;

    private BehaviorWrapper(BehaviorControl<E> behavior) {
        this.behaviorControl = behavior;
    }

    public static <E extends LivingEntity> BehaviorWrapper<E> Wrap(BehaviorControl<E> behavior) {
        return new BehaviorWrapper<>(behavior);
    }

    @Override
    public Behavior.Status getStatus() {
        return behaviorControl.getStatus();
    }

    //? if >= 26.1 {
    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return behaviorControl.getRequiredMemories();
    }
    //?}

    @Override
    public boolean tryStart(ServerLevel serverLevel, E livingEntity, long l) {
        if (
                AcaSetting.villagerOptimization
                && livingEntity instanceof VillagerAccessor villager
                && villager.aca$canDisableAI()
        ) {
            return false;
        }
        return behaviorControl.tryStart(serverLevel, livingEntity, l);
    }

    @Override
    public void tickOrStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (
                AcaSetting.villagerOptimization && livingEntity instanceof VillagerAccessor villager && villager.aca$canDisableAI()
        ) {
            return;
        }
        behaviorControl.tickOrStop(serverLevel, livingEntity, l);
    }

    @Override
    public void doStop(ServerLevel serverLevel, E livingEntity, long l) {
        if (
                 AcaSetting.villagerOptimization && livingEntity instanceof VillagerAccessor villager && villager.aca$canDisableAI()
        ) {
            return;
        }
        behaviorControl.doStop(serverLevel, livingEntity, l);
    }

    @Override
    public String debugString() {
        return "Wrapper[]";
    }
}
