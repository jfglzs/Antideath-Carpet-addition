package io.github.jfglzs.aca.mixin.rule.softAnvil;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntity_Mixin {
    @Shadow
    private boolean cancelDrop;

    @Inject(
            method = "causeFallDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"),
            cancellable = true
    )
    public void handleFallDamage_Inject(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.softAnvil) {
            this.cancelDrop = true;
            cir.setReturnValue(false);
        }
    }
}
