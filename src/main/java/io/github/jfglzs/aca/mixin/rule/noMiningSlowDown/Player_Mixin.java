package io.github.jfglzs.aca.mixin.rule.noMiningSlowDown;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class Player_Mixin {
    @Shadow
    @Final
    Inventory inventory;

    @Inject(
            method = "getDestroySpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getBlockBreakingSpeed_Inject(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (AcaSetting.noMiningSlowDown) {
            Player player = (Player) (Object) this;
            float mineSpeed;
            //? if > 1.21.4 {
             mineSpeed = inventory.getSelectedItem().getDestroySpeed(block);
            //?} else {
            /*mineSpeed = inventory.getMainHandItem().getDestroySpeed(block);
            *///?}
            if (MobEffectUtil.hasDigSpeed(player)) {
                mineSpeed *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
            }
            cir.setReturnValue(mineSpeed);
        }
    }
}