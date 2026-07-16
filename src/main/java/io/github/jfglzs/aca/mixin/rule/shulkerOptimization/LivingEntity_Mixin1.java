package io.github.jfglzs.aca.mixin.rule.shulkerOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.LivingEntityAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntity_Mixin1 {
    @WrapOperation(
            method = "pushEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    public boolean pushEntities(
            LivingEntity instance,
            ServerLevel level,
            DamageSource damageSource,
            float amount,
            Operation<Boolean> original,
            @Local List<Entity> pushableEntities
    ) {
        if (AcaSetting.shulkerOptimization) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity instanceof Shulker) {
                for (Entity entity : pushableEntities) {
                    if (entity instanceof LivingEntityAccessor accessor) {
                        accessor.aca$setPushed(true);
                        entity.hurtServer(level, damageSource, amount);
                    }
                }
            }
            return true;
        }
        return original.call(instance, level, damageSource, amount);
    }
}
