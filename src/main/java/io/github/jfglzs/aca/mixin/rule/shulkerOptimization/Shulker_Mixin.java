package io.github.jfglzs.aca.mixin.rule.shulkerOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.EntityUtils;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Shulker.class)
public abstract class Shulker_Mixin {
    @WrapOperation(
            method = "move",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/golem/AbstractGolem;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V")
    )
    private static void move(Shulker instance, MoverType moverType, Vec3 vec3, Operation<Void> original) {
        if (!AcaSetting.shulkerOptimization) {
            original.call(instance, moverType, vec3);
        }
    }

//    @WrapOperation(
//            method = "tick",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Shulker;canStayAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z")
//    )

    //TODO 实体校验
    @Inject(
            method = "findAttachableSurface",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void canStayAt(BlockPos target, CallbackInfoReturnable<Direction> cir) {
        if (AcaSetting.shulkerOptimization) {
            Direction directions = EntityUtils.attachables.get(EntityUtils.pack(target.getX(), target.getY(), target.getZ()));
            if (directions != null) {
                cir.setReturnValue(directions);
            }
        }
    }

    @Inject(
            method = "findAttachableSurface",
            at = @At("RETURN")
    )
    private void findAttachableSurface(BlockPos target, CallbackInfoReturnable<Direction> cir) {
        if (AcaSetting.shulkerOptimization) {
            Direction direction = cir.getReturnValue();
            if (direction != null) {
                EntityUtils.attachables.put(EntityUtils.pack(target.getX(), target.getY(), target.getZ()), direction);
            }
        }
    }
}
