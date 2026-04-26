package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;
import java.util.List;

@Mixin(NearestLivingEntitySensor.class)
public class NearestLivingEntitySensor_Mixin<T extends LivingEntity> {
    @WrapOperation(
            method = "doTick",
            at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V")
    )
    protected void sort_Wrap(List instance, Comparator<? super Entity> comparator, Operation<Void> original, @Local(argsOnly = true) T entity) {
        if (
                AcaSetting.villagerOptimization && entity instanceof Villager villager && ((VillagerAccessor) villager).aca$canDisableAI()
        ) {
            return;
        }

        original.call(instance, comparator);
    }
}
