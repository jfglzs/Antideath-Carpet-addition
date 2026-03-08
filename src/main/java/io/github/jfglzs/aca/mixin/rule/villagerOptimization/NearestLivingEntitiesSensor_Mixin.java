package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.NearestLivingEntitiesSensor;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;
import java.util.List;

@Mixin(NearestLivingEntitiesSensor.class)
public class NearestLivingEntitiesSensor_Mixin<T extends LivingEntity> {
    @WrapOperation(
            method = "sense",
            at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V")
    )
    protected void sort_Wrap(List instance, Comparator<? super Entity> comparator, Operation<Void> original, @Local(name = "entity") T entity) {
        if (
                AcaSetting.villagerOptimization && entity instanceof VillagerEntity villager && ((VillagerAccessor) villager).aca$canDisableAI()
        ) {
            return;
        }

        original.call(instance, comparator);
    }
}
