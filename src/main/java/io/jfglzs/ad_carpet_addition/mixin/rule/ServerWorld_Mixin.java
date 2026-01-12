package io.jfglzs.ad_carpet_addition.mixin.rule;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorld_Mixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tickInject(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
    }
}
