package io.jfglzs.ad_carpet_addition.mixin.rule;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class Slot_Mixin 
{
    @Inject(method = "canTakeItems",at = @At("HEAD"))
    public void canTakeItems(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) 
    {
      throw new RuntimeException();
    }
    
}
