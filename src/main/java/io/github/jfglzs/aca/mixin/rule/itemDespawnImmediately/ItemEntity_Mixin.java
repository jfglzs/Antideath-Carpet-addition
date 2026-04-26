package io.github.jfglzs.aca.mixin.rule.itemDespawnImmediately;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntity.class)
public abstract class ItemEntity_Mixin {

    @Shadow
    public abstract void makeFakeItem();

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tick(CallbackInfo ci) {
        if (AcaSetting.itemDespawnImmediately) {
            this.makeFakeItem();
        }
    }

    @WrapWithCondition(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V",
                    ordinal = 1
            )
    )
    public boolean discard_Wrap(ItemEntity instance) {
        return !AcaSetting.itemNeverDespawn;
    }
}
