package io.github.jfglzs.aca.mixin.carpet;

import carpet.logging.HUDController;
import io.github.jfglzs.aca.event.onLogging;
import net.minecraft.ReportedException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HUDController.class)
public abstract class HUDController_Mixin {
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

