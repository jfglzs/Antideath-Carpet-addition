package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import io.github.jfglzs.aca.utils.EntityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
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
        if (
                AcaSetting.villagerOptimization
                && this instanceof VillagerAccessor villager
                && villager.aca$canDisableAI()
        ) {
            ci.cancel();
        }
    }
}
