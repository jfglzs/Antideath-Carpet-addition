package io.jfglzs.ad_carpet_addition.mixin.rule;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.jfglzs.ad_carpet_addition.AcaSetting.itemDespawnImmediately;
import static io.jfglzs.ad_carpet_addition.AcaSetting.itemNeverDespawn;

@Mixin(ItemEntity.class)
public abstract class ItemEntity_Mixin
{

    @Shadow
    public abstract void setDespawnImmediately();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tick(CallbackInfo ci)
    {
        if(itemDespawnImmediately)
        {
            setDespawnImmediately();
        }
    }

    @Inject(method = "tick" , at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V" , ordinal = 1), cancellable = true)
    public void tick_discard(CallbackInfo ci)
    {
        if (itemNeverDespawn)
        {
            ci.cancel();
        }
    }
}
