package io.github.jfglzs.aca.mixin.carpet;

import carpet.logging.HUDController;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.event.onLogging;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HUDController.class)
public abstract class HUDController_Mixin {
    @Unique private static long aca$lastCall = 0;

    @WrapOperation(
            method = "update_hud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getTickCount()I"
            )
    )
    private static int getTickCount(MinecraftServer server, Operation<Integer> original) {
        long delta = System.currentTimeMillis() - aca$lastCall;
        aca$lastCall = System.currentTimeMillis();
        return AcaSetting.justInTimeLoggers && delta > AcaSetting.justInTimeLoggersLeastUpdateFreq ? 0 : original.call(server);
    }

    @Inject(
            method = "update_hud",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;keySet()Ljava/util/Set;"
            ),
            remap = false
    )
    private static void updateHUD_Inject(MinecraftServer server, List<ServerPlayer> force, CallbackInfo ci) {
        onLogging.event.update(server);
    }
}

