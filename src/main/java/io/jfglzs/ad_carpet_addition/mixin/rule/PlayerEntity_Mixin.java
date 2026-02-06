package io.jfglzs.ad_carpet_addition.mixin.rule;

import io.jfglzs.ad_carpet_addition.AcaSetting;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
//#if MC == 12001
//$$ import net.minecraft.enchantment.EnchantmentHelper;
//$$ import net.minecraft.entity.attribute.EntityAttributes;
//$$ import net.minecraft.item.ItemStack;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntity_Mixin extends LivingEntity {
    @Shadow @Final PlayerInventory inventory;

    protected PlayerEntity_Mixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("HEAD"),
            cancellable = true
    )

    public void getBlockBreakingSpeedInject(BlockState block, CallbackInfoReturnable<Float> cir) {
        float mineSpeed = this.inventory.getSelectedStack().getMiningSpeedMultiplier(block);
        if (AcaSetting.noMiningSlowDown) {
            if (mineSpeed > 1.0F) {
                mineSpeed += (float) this.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
            }
            if (StatusEffectUtil.hasHaste(this))
            {
                mineSpeed *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(this) + 1) * 0.2F;
            }
            cir.setReturnValue(mineSpeed);
        }
    }
}