package io.github.jfglzs.aca.mixin.rule.multiThreadWorldSave;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.Future;

@Mixin(ServerLevel.class)
public class ServerLevel_Mixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @WrapOperation(
            method = "save",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;saveAll()V"
            )
    )
    public void save(PersistentEntitySectionManager instance, Operation<Void> original) {
        if (! AcaSetting.multiThreadWorldSave) {
            original.call(instance);
            return;
        }

        if (this.server instanceof MinecraftServerAccessor accessor) {
            Future<Void> future = accessor.asa$getExecutor().submit(() -> original.call(instance));
            accessor.asa$getFutures().add(future);
        }
    }
}
