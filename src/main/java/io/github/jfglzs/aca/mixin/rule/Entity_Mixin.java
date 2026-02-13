package io.github.jfglzs.aca.mixin.rule;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class Entity_Mixin {
    @Inject(
            method = "getPose",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getPoseInject(CallbackInfoReturnable<EntityPose> cir) {
        if (AcaSetting.doubleClickShiftGetDown && (Object) this instanceof PlayerEntity p && AcaSetting.getDownPlayerSet.contains(p)) {
            if (!p.isOnGround()) {
                AcaSetting.getDownPlayerSet.remove(p);
            } else {
                cir.setReturnValue(EntityPose.SWIMMING);
            }
        }
    }

    @Inject(
            method = "setSneaking",
            at = @At("HEAD")
    )
    public void setSneakingInject(boolean sneaking, CallbackInfo ci) {
        if (!((Object) this instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity)(Object) this;
        RateLimiter rateLimiter = AcaSetting.sneakCooldownMap.get(player);
        if (rateLimiter == null) return;

        if (
                AcaSetting.doubleClickShiftGetDown &&
                sneaking &&
                player != null &&
                player.isOnGround()
        ) {
            if (!AcaSetting.getDownPlayerSet.contains(player)) {
                if (!rateLimiter.tryAcquire()) AcaSetting.getDownPlayerSet.add(player);
            }
            else AcaSetting.getDownPlayerSet.remove(player);
        }
    }
}
