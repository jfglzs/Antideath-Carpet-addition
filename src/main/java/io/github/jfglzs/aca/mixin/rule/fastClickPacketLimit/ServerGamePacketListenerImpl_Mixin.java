package io.github.jfglzs.aca.mixin.rule.fastClickPacketLimit;

import com.google.common.util.concurrent.RateLimiter;
import io.github.jfglzs.aca.AcaSetting;
//? if > 1.21.11
//import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImpl_Mixin {
    @Unique
    private RateLimiter limiter  = RateLimiter.create(20F);

    @Inject(
            method = "handleUseItem",
            at = @At("HEAD"),
            cancellable = true
    )
    public void handleUseItem_Limit(ServerboundUseItemPacket packet, CallbackInfo ci) {
        if (!limiter.tryAcquire() && AcaSetting.fastClickPacketLimit) {
            ci.cancel();
        }
    }

}
