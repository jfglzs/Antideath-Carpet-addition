package io.github.jfglzs.aca.mixin.rule.beeOptimization;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.LivingEntity;
//~ if >= 1.21.11 '.animal.Bee' -> '.animal.bee.Bee' {
import net.minecraft.world.entity.animal.bee.Bee;
//~}
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
    private void travel_Inject(Vec3 vec3, CallbackInfo ci) {
        if (AcaSetting.beeOptimization && ((Object) this) instanceof Bee) {
            ci.cancel();
        }
    }
}
