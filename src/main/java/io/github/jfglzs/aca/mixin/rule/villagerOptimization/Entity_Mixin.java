package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public class Entity_Mixin {
    @Inject(
            method = "collideBoundingBox(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Ljava/util/List;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void adjustMovementForCollisions_Inject(@Nullable Entity entity, Vec3 movement, AABB entityBoundingBox, Level world, List<VoxelShape> collisions, CallbackInfoReturnable<Vec3> cir) {
        if (entity instanceof Villager villager && AcaSetting.villagerOptimization && ((VillagerAccessor) villager).aca$canDisableAI()) {
            cir.setReturnValue(Vec3.ZERO);
            cir.cancel();
        }
    }
}
