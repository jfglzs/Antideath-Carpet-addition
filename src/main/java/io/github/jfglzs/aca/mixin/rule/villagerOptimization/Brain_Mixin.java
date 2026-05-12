package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import io.github.jfglzs.aca.utils.EntityUtils;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.sensing.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(Brain.class)
public class Brain_Mixin<E extends LivingEntity> {
    @Unique
    private static final ReferenceOpenHashSet<Class<? extends Sensor>> SENSORS = new  ReferenceOpenHashSet<>(Set.of(
            SecondaryPoiSensor.class,
            VillagerBabiesSensor.class,
            PlayerSensor.class,
            NearestItemSensor.class,
            HurtBySensor.class
    ));

    @Unique
    private static final ReferenceOpenHashSet<Class<? extends BehaviorControl>> BEHAVIOR_CONTROLS = new ReferenceOpenHashSet<>(Set.of(
            Swim.class,
            GiveGiftToHero.class,
            LookAndFollowTradingPlayerSink.class,
            TradeWithVillager.class,
            ShowTradesToPlayer.class,
            GateBehavior.class
    ));

    @WrapWithCondition(
            method = "tickSensors",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/Sensor;tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    )
    private boolean tickSensors_WarpOperation(Sensor<?> sensor, ServerLevel world, E entity) {
        if (
                AcaSetting.villagerOptimization && EntityUtils.canDisableAI(this)
        ) {
            return !SENSORS.contains(sensor.getClass());
        }

        return true;
    }

    @WrapOperation(
            method = "tickEachRunningBehavior",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;tickOrStop(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)V"
            )
    )
    private void startTasks_Tick(BehaviorControl<?> bc, ServerLevel level, E entity, long l, Operation<Void> original) {
        if (
                AcaSetting.villagerOptimization && EntityUtils.canDisableAI(this) && BEHAVIOR_CONTROLS.contains(bc.getClass())
        ) {
            return;
        }

        original.call(bc, level, entity, l);
    }
}
