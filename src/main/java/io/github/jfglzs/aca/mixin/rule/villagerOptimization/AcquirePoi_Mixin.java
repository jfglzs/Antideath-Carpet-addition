package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import io.github.jfglzs.aca.utils.EntityUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Mixin(AcquirePoi.class)
public class AcquirePoi_Mixin {
    @Inject(
            method = "method_46885",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;"),
            cancellable = true
    )
    private static void collect(
            boolean bl,
            MutableLong mutableLong,
            Long2ObjectMap long2ObjectMap,
            Predicate predicate,
            BiPredicate biPredicate,
            MemoryAccessor memoryAccessor,
            Optional<Byte> optional,
            ServerLevel serverLevel,
            PathfinderMob pathfinderMob,
            long l,
            CallbackInfoReturnable<Boolean> cir,

            @Local PoiManager poiManager
            ) {
        if (pathfinderMob instanceof Villager villager) {
            var vl = ((VillagerAccessor) villager);
            if (!AcaSetting.villagerOptimization || !vl.aca$canDisableAI()) return;

            vl.aca$addPOIRequest();
            if ((vl.aca$getPOIRequests() + villager.getId() % 807) % 400 != 0) {
                Path path = vl.aca$getPath();
                if (path != null && path.canReach()) {
                    BlockPos blockPos = path.getTarget();
                    poiManager.getType(blockPos).ifPresent((holder) -> {
                        poiManager.take(predicate, (holderx, blockPos2) -> blockPos2.equals(blockPos), blockPos, 1);
                        memoryAccessor.set(GlobalPos.of(serverLevel.dimension(), blockPos));
                        optional.ifPresent((byte_) -> serverLevel.broadcastEntityEvent(pathfinderMob, byte_));
                        long2ObjectMap.clear();
                        DebugPackets.sendPoiTicketCountPacket(serverLevel, blockPos);
                    });
                } else {
                    return;
                }
                cir.setReturnValue(true);
            }
        }
    }

    @WrapMethod(
            method = "findPathToPois"
    )
    private static Path findPathToPois(Mob mob, Set<Pair<Holder<PoiType>, BlockPos>> set, Operation<Path> original) {
        var path = original.call(mob, set);
        if (path != null && mob instanceof Villager villager && EntityUtils.canDisableAI(villager)) {
            ((VillagerAccessor) villager).aca$setPath(path);
        }
        return path;
    }
}
