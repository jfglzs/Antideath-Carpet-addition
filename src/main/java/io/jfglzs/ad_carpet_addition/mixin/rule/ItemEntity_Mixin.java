package io.jfglzs.ad_carpet_addition.mixin.rule;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntity.class)
public abstract class ItemEntity_Mixin {

    @Shadow public abstract void setDespawnImmediately();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tick(CallbackInfo ci) {
        if(AcaSetting.itemDespawnImmediately) {
            setDespawnImmediately();
        }
    }

    @WrapWithCondition(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ItemEntity;discard()V",
                    ordinal = 1
            )
    )
    public boolean tick_discard(ItemEntity instance) {
        return !AcaSetting.itemNeverDespawn;
    }
}
