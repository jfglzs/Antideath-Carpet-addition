package io.github.jfglzs.aca.mixin.rule.multiThreadWorldSave;

//? if > 1.21.1 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.MinecraftServerAccessor;
import io.github.jfglzs.aca.utils.ThreadUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(SerializableChunkData.class)
public class SerializableChunkData_Mixin {
    @Unique private static final Object aca$LOCK = new Object();

    @WrapOperation(
            method = "copyOf",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/ChunkAccess;getBlockEntitiesPos()Ljava/util/Set;",
                    ordinal = 1
            )
    )
    private static Set<BlockPos> getBlockEntitiesPos(ChunkAccess instance, Operation<Set<BlockPos>> original,
                                                     @Local(ordinal = 1) List<CompoundTag> be,
                                                     @Local(argsOnly = true) ChunkAccess chunk,
                                                     @Local(argsOnly = true) ServerLevel level) {
        Set<BlockPos> result = original.call(instance);

        if (AcaSetting.multiThreadWorldSave) {
            List<BlockPos> blockPos = new ArrayList<>(result);
            ThreadUtils.parallel(blockPos, 200, bp -> {
                for (BlockPos pos : bp) {
                    CompoundTag tag = chunk.getBlockEntityNbtForSaving(pos, level.registryAccess());
                    synchronized (aca$LOCK) {
                        if (tag != null) {
                            be.add(tag);
                        }
                    }
                }
            }, ((MinecraftServerAccessor) level.getServer()).asa$getExecutor(), false);
            return new HashSet<>(0);
        }

        return result;
    }

}
//?} else {
//@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.aca.utils.DummyClass.class)
//public class SerializableChunkData_Mixin {}
//?}
