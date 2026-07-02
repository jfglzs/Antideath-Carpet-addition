package io.github.jfglzs.aca.mixin.rule.neverDropLeashBySpectator;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Leashable.class)
public interface Leashable_Mixin {
    //? if >= 1.21.10 {
    @WrapOperation(
            method = "tickLeash",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;canInteractWithLevel()Z",
                    ordinal = 1
            )
    )
     private static boolean dropLeash(Entity entity, Operation<Boolean> original) {
        if (AcaSetting.neverDropLeashBySpectator) {
            if (entity instanceof ServerPlayer player && player.isSpectator()) {
                return true;
            }
        }
        return original.call(entity);
    }
    //?}
}
