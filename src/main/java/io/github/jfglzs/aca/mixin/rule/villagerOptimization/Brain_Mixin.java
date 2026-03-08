package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.sensor.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(Brain.class)
public class Brain_Mixin<E extends LivingEntity> {
    @Unique
    private static final Set<Class<? extends Sensor>> SENSORS = Set.of(
            SecondaryPointsOfInterestSensor.class,
            VillagerBabiesSensor.class,
            NearestPlayersSensor.class,
            NearestItemsSensor.class,
            HurtBySensor.class
    );

    @Unique
    private static final Set<Class<? extends Task>> TASKS = Set.of(
            GiveGiftsToHeroTask.class,
            StayAboveWaterTask.class,
            FollowCustomerTask.class,
            GatherItemsVillagerTask.class
    );

    @WrapWithCondition(
            method = "tickSensors",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/sensor/Sensor;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V")
    )
    private boolean tickSensors_WarpOperation(Sensor<?> sensor, ServerWorld world, E entity) {
        if (
                entity instanceof VillagerEntity villager &&
                AcaSetting.villagerOptimization &&
                ((VillagerAccessor) villager).aca$canDisableAI()
        ) {
            return !SENSORS.contains(sensor.getClass());
        }

        return true;
    }

    @WrapOperation(
            method = "updateTasks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/brain/task/Task;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)V"
            )
    )
    private void startTasks_Tick(Task task, ServerWorld serverWorld, E e, long l, Operation<Void> original) {

        if (
                e instanceof VillagerEntity villager &&
                AcaSetting.villagerOptimization &&
                ((VillagerAccessor) villager).aca$canDisableAI() &&
                TASKS.contains(task.getClass())
        ) {
            return;
        }

        original.call(task, serverWorld, e, l);
    }

}
