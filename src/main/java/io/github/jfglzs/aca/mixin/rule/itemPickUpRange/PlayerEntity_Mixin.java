package io.github.jfglzs.aca.mixin.rule.itemPickUpRange;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntity_Mixin {
    @Inject(
            method = "tickMovement",
            at = @At(value = "TAIL")
    )
    public void tickMovement_Inject(CallbackInfo ci) {
        if (AcaSetting.itemPickUpRange > 0) {
            PlayerEntity player = ((PlayerEntity) (Object) this);
            Box box = player.getBoundingBox().expand(AcaSetting.itemPickUpRange);
            for (ItemEntity entity : player.getWorld().getEntitiesByType(EntityType.ITEM, box, itemEntity -> !itemEntity.isRemoved())) {
                entity.onPlayerCollision(player);
            }
        }
    }

    @WrapOperation(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;")
    )
    public List<Entity> getOtherEntities_Wrap(World world, Entity entity, Box box, Operation<List<Entity>> original) {
        if (AcaSetting.itemPickUpRange > 0) {
            return world.getOtherEntities(entity, box, entity1 -> !entity1.isSpectator() && !(entity1 instanceof ItemEntity));
        }
        return original.call(world, entity, box);
    }
}
