package io.github.jfglzs.aca.mixin.rule.fakePlaceOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public class Entity_Mixin {
    @Inject(
            method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void adjustMovementForCollisions_Inject(@Nullable Entity entity, Vec3d movement, Box entityBoundingBox, World world, List<VoxelShape> collisions, CallbackInfoReturnable<Vec3d> cir) {
        if (AcaSetting.fakePeaceOptimization) {
            if (
                    (entity instanceof WardenEntity warden && ((EntityAccessor) warden).aca$getCount() > 70) ||
                            (entity instanceof WitherEntity wither && ((EntityAccessor) wither).aca$getCount() > 70)
            ) {
                cir.setReturnValue(Vec3d.ZERO);
                cir.cancel();
            }
        }
    }
}
