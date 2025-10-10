package io.jfglzs.ad_carpet_addition.mixin.rule;

import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.jfglzs.ad_carpet_addition.AcaSetting.endermanNeverGetAngryByPlayer;

@Mixin(EndermanEntity.class)
public class EndermanEntity_Mixin
{
    @Inject(method = "isPlayerStaring",at = @At("HEAD"), cancellable = true)
    void setAngerTimeInject(PlayerEntity player, CallbackInfoReturnable<Boolean> cir)
    {
        if (endermanNeverGetAngryByPlayer) cir.setReturnValue(false);
    }
}
