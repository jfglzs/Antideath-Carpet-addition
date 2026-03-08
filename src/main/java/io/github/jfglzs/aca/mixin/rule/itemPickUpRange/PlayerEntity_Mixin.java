package io.github.jfglzs.aca.mixin.rule.itemPickUpRange;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntity_Mixin {
    @Inject(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;")
    )
    public void tickMovement(CallbackInfo ci) {
        if (AcaSetting.itemPickUpRange >= 0) {
            PlayerEntity player = ((PlayerEntity) (Object) this);
            Box box = player.getBoundingBox().expand(AcaSetting.itemPickUpRange);
            for (ItemEntity entity : player.getWorld().getNonSpectatingEntities(ItemEntity.class, box)) {
                if (!entity.isRemoved()) {
                    entity.onPlayerCollision(player);
                }
            }
        }
    }
}
