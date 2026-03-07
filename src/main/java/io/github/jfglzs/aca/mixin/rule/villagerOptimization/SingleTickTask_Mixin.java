package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.SingleTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleTickTask.class)
public class SingleTickTask_Mixin<E extends LivingEntity> {
    @Inject(
            method = "tryStarting",
            at = @At("HEAD"),
            cancellable = true
    )
    public final void tryStarting_Inject(ServerWorld world, E entity, long time, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof VillagerEntity villager && AcaSetting.villagerOptimization && ((VillagerAccessor) villager).aca$canDisableAI()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
