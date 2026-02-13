package xyz.antideath.aca.mixin.rule;

import xyz.antideath.aca.AcaSetting;
import net.minecraft.block.BlockState;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
//#if MC == 12001
//$$ import net.minecraft.enchantment.EnchantmentHelper;
//$$ import net.minecraft.entity.attribute.EntityAttributes;
//$$ import net.minecraft.item.ItemStack;
//#endif
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntity_Mixin {
    @Shadow @Final PlayerInventory inventory;

    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getBlockBreakingSpeedInject(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (AcaSetting.noMiningSlowDown) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            float mineSpeed = this.inventory.getSelectedStack().getMiningSpeedMultiplier(block);
            if (mineSpeed > 1.0F) {
                mineSpeed += (float) player.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
            }
            if (StatusEffectUtil.hasHaste(player)) {
                mineSpeed *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(player) + 1) * 0.2F;
            }
            cir.setReturnValue(mineSpeed);
        }
    }
}