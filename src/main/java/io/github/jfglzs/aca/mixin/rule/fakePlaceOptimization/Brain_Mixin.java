package io.github.jfglzs.aca.mixin.rule.fakePlaceOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class Brain_Mixin<E extends LivingEntity> {
    /**
     * 灵感来源：<a href="https://github.com/Melationin/ROF-Carpet-Addition/blob/master/src/main/java/com/carpet/rof/mixin/rules/piglinRules/BrainMixin2.java">...</a>
     *
     * @Author AntiDeath
     * @Rule fakePeaceOptimization
     */

    @Inject(
            method = "tickSensors",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickSensors_Inject(ServerWorld world, E entity, CallbackInfo ci) {
        if (AcaSetting.fakePeaceOptimization && entity instanceof WardenEntity warden) {
            if (((EntityAccessor) warden).aca$getCount() > 70) {
                ci.cancel();
            }
        }
    }
}
