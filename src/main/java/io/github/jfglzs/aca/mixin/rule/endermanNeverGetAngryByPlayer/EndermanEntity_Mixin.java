package io.github.jfglzs.aca.mixin.rule.endermanNeverGetAngryByPlayer;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntity_Mixin {
    @Inject(
            method = "isPlayerStaring",
            at = @At("HEAD"),
            cancellable = true
    )
    void setAngerTimeInject(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.endermanNeverGetAngryByPlayer) cir.setReturnValue(false);
    }
}
