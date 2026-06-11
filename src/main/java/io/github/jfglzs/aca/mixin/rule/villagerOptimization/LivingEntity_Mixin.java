package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.IVillagerAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 900)
public class LivingEntity_Mixin {
    @Inject(
            method = "travel",
            at = @At("HEAD"),
            cancellable = true
    )
    private void travel(Vec3 vec3, CallbackInfo ci) {
        if (
                AcaSetting.villagerOptimization
                && !AcaSetting.villagerOptimizationEjectSupport
                && this instanceof IVillagerAccessor villager
                && villager.aca$canDisableAI()
        ) {
            ci.cancel();
        }
    }

    @Inject(
            method = "pushEntities",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pushEntities(CallbackInfo ci) {
        if (
                AcaSetting.villagerOptimization && this instanceof IVillagerAccessor villager && villager.aca$canDisableAI()
        ) {
            ci.cancel();
        }
    }
}
