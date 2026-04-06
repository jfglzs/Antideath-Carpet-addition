package io.github.jfglzs.aca.mixin.rule.noMiningSlowDown;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.block.BlockState;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntity_Mixin {
    @Shadow
    @Final
    PlayerInventory inventory;

    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getBlockBreakingSpeed_Inject(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (AcaSetting.noMiningSlowDown) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            float mineSpeed;
            //? if > 1.21.1 {
             mineSpeed = inventory.getSelectedStack().getMiningSpeedMultiplier(block);
            //?} else {
            /*mineSpeed = inventory.getMainHandStack().getMiningSpeedMultiplier(block);
            *///?}
            if (StatusEffectUtil.hasHaste(player)) {
                mineSpeed *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(player) + 1) * 0.2F;
            }
            cir.setReturnValue(mineSpeed);
        }
    }
}