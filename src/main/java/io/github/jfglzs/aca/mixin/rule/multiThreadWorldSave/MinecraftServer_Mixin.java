package io.github.jfglzs.aca.mixin.rule.multiThreadWorldSave;

import io.github.jfglzs.aca.ACAEntry;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Mixin(MinecraftServer.class)
public class MinecraftServer_Mixin implements MinecraftServerAccessor {
    @Unique private List<Future<?>> aca$threads;
    @Unique private ExecutorService aca$executor = Executors.newFixedThreadPool(10);

    @Inject(
            method = "saveAllChunks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getSingleplayerProfile()Lcom/mojang/authlib/GameProfile;"
            )
    )
    public void saveAllChunks(CallbackInfoReturnable<Boolean> cir) {
        if (! AcaSetting.multiThreadWorldSave)
            return;

        for (Future<?> future : this.aca$threads) {
            try {
                future.get();
            }
            catch (Exception e) {
                ACAEntry.LOGGER.error("Failed to save world", e);
                throw new RuntimeException(e);
            }
        }

        this.aca$threads.clear();
    }

    @Override
    public List<Future<?>> asa$getFutures() {
        ensure();
        return this.aca$threads;
    }

    @Override
    public ExecutorService asa$getExecutor() {
        return this.aca$executor;
    }

    @Unique
    private void ensure() {
        if (this.aca$threads == null)
            this.aca$threads = new ArrayList<>();
    }
}
