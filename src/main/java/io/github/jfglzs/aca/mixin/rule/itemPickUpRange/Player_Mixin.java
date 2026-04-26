package io.github.jfglzs.aca.mixin.rule.itemPickUpRange;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Player.class)
public class Player_Mixin {
    @Unique
    Predicate<Entity> rule = entity -> !entity.isSpectator() && entity instanceof ItemEntity;
    @Unique
    Predicate<Entity> origin = entity -> !entity.isSpectator();

    @Inject(
            method = "aiStep",
            at = @At(value = "TAIL")
    )
    public void tickMovement_Inject(CallbackInfo ci) {
        if (AcaSetting.itemPickUpRange > 0) {
            Player player = ((Player) (Object) this);
            AABB box = player.getBoundingBox().inflate(AcaSetting.itemPickUpRange);
            for (ItemEntity entity : player.level().getEntities(EntityType.ITEM, box, entity -> !entity.isRemoved())) {
                entity.playerTouch(player);
            }
        }
    }

    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;")
    )
    public List<Entity> getOtherEntities_Redirect(Level world, Entity entity, AABB box) {
        Predicate<Entity> predicate = AcaSetting.itemPickUpRange > 0 ? rule : origin;
        return world.getEntities(entity, box, predicate);
    }
}
