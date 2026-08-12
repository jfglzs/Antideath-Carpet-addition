package io.github.jfglzs.aca.mixin.rule.mobSpawnOptimization;

import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.level.ServerChunkCache;


@Mixin(ServerChunkCache.class)
public class ServerChunkCache_Mixin {
    //? if > 1.21.4 {
    @Shadow
    @Final
    private ServerLevel level;

    @Inject(
            method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;J)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/level/ServerChunkCache;spawningChunks:Ljava/util/List;",
                    opcode = Opcodes.GETFIELD
            )
    )
    private void getFilteredSpawningCategories(ProfilerFiller profiler, long timeDiff, CallbackInfo ci, @Local List<MobCategory> result) {
        if (AcaSetting.mobSpawnOptimization) {
            if (result.isEmpty()) return;
            ResourceKey<Level> dimension = this.level.dimension();
            boolean bl = dimension == Level.OVERWORLD;
            if (!bl) {
                result.remove(MobCategory.AXOLOTLS);
                result.remove(MobCategory.WATER_AMBIENT);
                result.remove(MobCategory.UNDERGROUND_WATER_CREATURE);
                result.remove(MobCategory.WATER_CREATURE);
                result.remove(MobCategory.AMBIENT);
            }
        }
    }
    //?}
}
