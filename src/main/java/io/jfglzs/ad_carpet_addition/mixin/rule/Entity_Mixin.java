package io.jfglzs.ad_carpet_addition.mixin.rule;

import com.google.common.util.concurrent.RateLimiter;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class Entity_Mixin
{
    @Inject(
            method = "getPose",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getPoseInject(CallbackInfoReturnable<EntityPose> cir)
    {
        if (AcaSetting.doubleClickShiftGetDown && (Object) this instanceof PlayerEntity p && AcaSetting.getDownPlayerList.contains(p) && p.isOnGround())
        {
            cir.setReturnValue(EntityPose.SWIMMING);
        }
    }

    @Inject(
            method = "setSneaking",
            at = @At("HEAD")
    )
    public void setSneakingInject(boolean sneaking, CallbackInfo ci)
    {
        if (AcaSetting.doubleClickShiftGetDown && sneaking && (Object) this instanceof PlayerEntity p && p.isOnGround())
        {
            if (!AcaSetting.getDownPlayerList.contains(p))
            {
                RateLimiter r = AcaSetting.sneakCooldownMap.get(p);
                if (!r.tryAcquire()) setToGetDown(p);
            }
            else
            {
                AcaSetting.getDownPlayerList.remove(p);
            }
        }
    }

    @Unique
    private void setToGetDown(PlayerEntity p)
    {
        AcaSetting.getDownPlayerList.add(p);
    }
}
