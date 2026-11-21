package io.github.jfglzs.aca.mixin.carpet;

import carpet.logging.HUDController;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.event.onLogging;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(HUDController.class)
public abstract class HUDController_Mixin {
    @Mutable
    @Shadow
    @Final
    public static Map<ServerPlayer, List<Component>> player_huds;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void clinit(CallbackInfo ci) {
        player_huds = new ConcurrentHashMap<>();
    }

    @WrapOperation(
            method = "update_hud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getTickCount()I"
            )
    )
    private static int getTickCount(MinecraftServer server, Operation<Integer> original) {
        return AcaSetting.asynchronizedLoggers ? 0 : original.call(server);
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

