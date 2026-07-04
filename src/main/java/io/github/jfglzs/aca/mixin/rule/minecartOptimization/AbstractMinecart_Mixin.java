package io.github.jfglzs.aca.mixin.rule.minecartOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.AbstractMinecartAccessor;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecart_Mixin implements AbstractMinecartAccessor {
    @Unique
    private boolean aca$canDisable = false;

    @Inject(
            method = "applyEffectsFromBlocks",
            at = @At("HEAD"),
            cancellable = true
    )
    private void applyEffectsFromBlocks(CallbackInfo ci) {
        if (AcaSetting.minecartOptimization && this.aca$canDisable) {
            ci.cancel();
        }
    }

    @Override
    public boolean aca$canDisable() {
        return aca$canDisable;
    }

    @Override
    public void aca$setCanDisable(boolean canDisable) {
        this.aca$canDisable = canDisable;
    }
}
