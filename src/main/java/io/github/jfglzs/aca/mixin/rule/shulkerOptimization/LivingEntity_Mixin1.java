package io.github.jfglzs.aca.mixin.rule.shulkerOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.LivingEntityAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? > 1.21.1
import net.minecraft.server.level.ServerLevel;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntity_Mixin1 {
    @WrapOperation(
            method = "pushEntities",
            at = @At(
                    value = "INVOKE",
                    //? if > 1.21.1 {
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
                    //?} else {
                    /*target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
                    *///?}
            )
    )
    //? if > 1.21.1 {
    public boolean pushEntities(LivingEntity instance, ServerLevel level, DamageSource damageSource, float amount, Operation<Boolean> original, @Local List<Entity> pushableEntities) {
    //?} else {
    /*public boolean pushEntities(LivingEntity instance, DamageSource damageSource, float amount, Operation<Boolean> original, @Local List<Entity> pushableEntities) {
    *///?}
        if (AcaSetting.shulkerOptimization) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity instanceof Shulker) {
                for (Entity entity : pushableEntities) {
                    if (entity instanceof LivingEntityAccessor accessor) {
                        accessor.aca$setPushed(true);
                        //? if > 1.21.1 {
                        entity.hurtServer(level, damageSource, amount);
                        //?} else {
                        /*entity.hurt(damageSource, amount);
                        *///?}
                    }
                }
            }
            return true;
        }
        //? if > 1.21.1 {
        return original.call(instance, level, damageSource, amount);
        //?} else {
        /*return original.call(instance, damageSource, amount);
        *///?}
    }
}
