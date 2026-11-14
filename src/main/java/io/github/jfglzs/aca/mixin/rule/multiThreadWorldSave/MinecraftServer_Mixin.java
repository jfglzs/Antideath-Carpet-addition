package io.github.jfglzs.aca.mixin.rule.multiThreadWorldSave;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.ACAEntry;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
public class MinecraftServer_Mixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique private List<Future<?>> aca$threads;
    @Unique private ExecutorService aca$executor = Executors.newFixedThreadPool(10);

    @WrapOperation(
            method = "saveAllChunks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;save(Lnet/minecraft/util/ProgressListener;ZZ)V"
            )
    )
    public void saveAllChunks(ServerLevel level, ProgressListener progressListener, boolean flush, boolean noSave,
                              Operation<Void> original) {
        if (AcaSetting.multiThreadWorldSave) {
            this.ensureThreads();
            this.aca$threads.add(this.aca$executor.submit(() -> {
                LOGGER.info("Async Saving chunks for {}", level.getChunkSource().chunkMap.getStorageName());
                original.call(level, progressListener, flush, noSave);
            }));
            return;
        }
        original.call(level, progressListener, flush, noSave);
    }

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
                ACAEntry.LOGGER.error("Failed to save all chunks", e);
                throw new RuntimeException(e);
            }
        }

        this.aca$threads.clear();
    }

    @Unique
    private void ensureThreads() {
        if (this.aca$threads == null)
            this.aca$threads = new ArrayList<>();
    }
}
