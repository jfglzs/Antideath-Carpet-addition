package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.IVillagerAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class Entity_Mixin {
    //? if >= 1.21.1 {
    @Inject(
            method = "applyEffectsFromBlocks()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void applyEffectsFromBlocks_Inject(CallbackInfo ci) {
        if (!AcaSetting.villagerOptimization) {
            return;
        }

        if (this instanceof IVillagerAccessor villager && villager.aca$canDisableAI()) {
            ci.cancel();
        }
    }
    //?}
}
