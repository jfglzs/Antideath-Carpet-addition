package io.jfglzs.ad_carpet_addition.mixin.rule;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.jfglzs.ad_carpet_addition.AcaSetting.noMiningSlowDown;

@Mixin(PlayerEntity.class)
public abstract class playerEntity_Mixin extends LivingEntity {
    @Shadow
    @Final
    PlayerInventory inventory;

    protected playerEntity_Mixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "HEAD"), cancellable = true)

    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        float mineSpeed = this.inventory.getSelectedStack().getMiningSpeedMultiplier(block);
        if (noMiningSlowDown) {
            //#if MC > 12001
            if (mineSpeed > 1.0F) {
                mineSpeed += (float) this.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
            }
            if (StatusEffectUtil.hasHaste(this)) {
                mineSpeed *= 1.0F + (float) (StatusEffectUtil.getHasteAmplifier(this) + 1) * 0.2F;

                cir.setReturnValue(mineSpeed);
                //#else
                //$$            if (mineSpeed > 1.0F) {
                //$$                int i = EnchantmentHelper.getEfficiency(this);
                //$$                ItemStack itemStack = this.getMainHandStack();
                //$$              if (i > 0 && !itemStack.isEmpty()) {
                //$$                    mineSpeed += (float)(i * i + 1);
                //$$                }
                //$$            }
                //$$            if (StatusEffectUtil.hasHaste(this)) {
                //$$                mineSpeed *= 1.0F + (float)(StatusEffectUtil.getHasteAmplifier(this) + 1) * 0.2F;
                //$$            }
                //$$   cir.setReturnValue(mineSpeed);
                //#endif
            }
        }
    }
//#if MC > 12001
}
//#endif