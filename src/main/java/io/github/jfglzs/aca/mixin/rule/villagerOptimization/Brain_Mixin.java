package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.IVillagerAccessor;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.sensing.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(Brain.class)
public class Brain_Mixin<E extends LivingEntity> {
    @Unique
    private static final ReferenceOpenHashSet<Class<? extends Sensor<?>>> SENSORS = new ReferenceOpenHashSet<>(Set.of(
            SecondaryPoiSensor.class,
            VillagerBabiesSensor.class,
            PlayerSensor.class,
            NearestItemSensor.class,
            HurtBySensor.class
    ));


    @WrapWithCondition(
            method = "tickSensors",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/Sensor;tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V")
    )
    private boolean tickSensors_Wrap(Sensor<?> sensor, ServerLevel world, E entity) {
        if (
                AcaSetting.villagerOptimization
                        && entity instanceof IVillagerAccessor villager
                        && villager.aca$canDisableAI()
        ) {
            return !SENSORS.contains(sensor.getClass());
        }

        return true;
    }
}
