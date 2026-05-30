package io.github.jfglzs.aca.mixin.rule.endermanNeverGetAngryByPlayer;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EnderMan_Mixin {
    @Inject(
            //? if > 1.21.1 {
            method = "isBeingStaredBy",
            //?} else {
            /*method = "isLookingAtMe",
            *///?}
            at = @At("HEAD"),
            cancellable = true
    )
    private void setAngerTime_Inject(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.endermanNeverGetAngryByPlayer) {
            cir.setReturnValue(false);
        }
    }
}
