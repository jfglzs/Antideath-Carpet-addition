package io.github.jfglzs.aca.mixin.rule.itemPickUpRange;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public class PlayerEntity_Mixin {
    @Unique
    Predicate<Entity> rule = entity -> !entity.isSpectator() && entity instanceof ItemEntity;
    @Unique
    Predicate<Entity> origin = entity -> !entity.isSpectator();


    @Inject(
            method = "tickMovement",
            at = @At(value = "TAIL")
    )
    public void tickMovement_Inject(CallbackInfo ci) {
        if (AcaSetting.itemPickUpRange > 0) {
            PlayerEntity player = ((PlayerEntity) (Object) this);
            Box box = player.getBoundingBox().expand(AcaSetting.itemPickUpRange);
            for (ItemEntity entity : player.getWorld().getEntitiesByType(EntityType.ITEM, box, entity -> !entity.isRemoved())) {
                entity.onPlayerCollision(player);
            }
        }
    }

    @Redirect(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;")
    )
    public List<Entity> getOtherEntities_Redirect(World world, Entity entity, Box box) {
        Predicate<Entity> predicate = AcaSetting.itemPickUpRange > 0 ? rule : origin;
        return world.getOtherEntities(entity, box, predicate);
    }
}
