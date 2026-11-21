package io.github.jfglzs.aca.mixin.carpet;

import carpet.CarpetServer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(CarpetServer.class)
public class CarpetServer_Mixin {
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcarpet/logging/HUDController;update_hud(Lnet/minecraft/server/MinecraftServer;Ljava/util/List;)V"
            )
    )
    private static void update_hud(MinecraftServer server, List<ServerPlayer> force, Operation<Void> original) {
        if (! AcaSetting.asynchronizedLoggers)
            original.call(server, force);
    }
}
