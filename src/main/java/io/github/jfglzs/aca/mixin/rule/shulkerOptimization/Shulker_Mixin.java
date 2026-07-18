package io.github.jfglzs.aca.mixin.rule.shulkerOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.EntityUtils;
import io.github.jfglzs.aca.utils.wrap.Cache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Shulker.class)
public abstract class Shulker_Mixin extends AbstractGolem {
    protected Shulker_Mixin(EntityType<? extends AbstractGolem> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
            method = "move",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/golem/AbstractGolem;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V")
    )
    private static void move(Shulker instance, MoverType moverType, Vec3 vec3, Operation<Void> original) {
        if (!AcaSetting.shulkerOptimization) {
            original.call(instance, moverType, vec3);
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Shulker;canStayAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z")
    )
    private boolean canStayAt(Shulker instance, BlockPos pos, Direction facing, Operation<Boolean> original) {
        if (AcaSetting.shulkerOptimization) {
            if (this.random.nextInt(5) == 0) {
               return original.call(instance, pos, facing);
            }
            return true;
        }
        return original.call(instance, pos, facing);
    }

    //TODO 实体校验
    @Inject(
            method = "findAttachableSurface",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void findAttachableSurface(BlockPos target, CallbackInfoReturnable<Direction> cir) {
        if (AcaSetting.shulkerOptimization) {
            Cache<Direction> cache = EntityUtils.ATTACHABLES.get(EntityUtils.pack(target.getX(), target.getY(), target.getZ()));
            if (cache != null && !cache.isExpired()) {
                cir.setReturnValue(cache.getValue());
            }
        }
    }

    @Inject(
            method = "findAttachableSurface",
            at = @At("RETURN")
    )
    private void findAttachableSurface_1(BlockPos target, CallbackInfoReturnable<Direction> cir) {
        if (AcaSetting.shulkerOptimization) {
            Direction direction = cir.getReturnValue();
            if (direction != null) {
                EntityUtils.ATTACHABLES.put(
                        EntityUtils.pack(target.getX(), target.getY(), target.getZ()),
                        new Cache<>(direction, this.random.nextInt(5,10))
                );
            }
        }
    }
}
