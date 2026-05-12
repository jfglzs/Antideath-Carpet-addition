package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.mojang.datafixers.util.Pair;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import io.github.jfglzs.aca.utils.EntityUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(AcquirePoi.class)
public class AcquirePoi_Mixin {
    @Unique
    private static final Int2ObjectOpenHashMap<Path> CACHED_PATH = new Int2ObjectOpenHashMap<>();

    @Inject(
            method = "findPathToPois",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;createPath(Ljava/util/Set;I)Lnet/minecraft/world/level/pathfinder/Path;",
                    shift = At.Shift.AFTER
            )
    )
    private static void findPathToPoi_Inject(Mob mob, Set<Pair<Holder<PoiType>, BlockPos>> set, CallbackInfoReturnable<Path> cir) {
        if (AcaSetting.villagerOptimization && EntityUtils.canDisableAI(mob)) {
            CACHED_PATH.put(mob.getId(), cir.getReturnValue());
        }
    }


    @Inject(
            method = "findPathToPois",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void findPathToPoi_Cache(Mob mob, Set<Pair<Holder<PoiType>, BlockPos>> set, CallbackInfoReturnable<Path> cir) {
        if (
                AcaSetting.villagerOptimization && EntityUtils.canDisableAI(mob) && (mob.tickCount + mob.getId() % 807) % 400 != 0
        ) {
            Path path = CACHED_PATH.get(mob.getId());
            if (path != null) {
                cir.setReturnValue(path);
            }
        }
    }
}
