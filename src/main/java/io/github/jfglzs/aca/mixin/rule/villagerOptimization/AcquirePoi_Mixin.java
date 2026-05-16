package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import io.github.jfglzs.aca.utils.EntityUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.npc.Villager;
import org.apache.commons.lang3.mutable.MutableLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Mixin(AcquirePoi.class)
public class AcquirePoi_Mixin {
//TODO POI缓存

    //? if >= 26.1 {
    /*@Inject(
            method = "lambda$create$3",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/village/poi/PoiManager;findAllClosestFirstWithType(Ljava/util/function/Predicate;Ljava/util/function/Predicate;Lnet/minecraft/core/BlockPos;ILnet/minecraft/world/entity/ai/village/poi/PoiManager$Occupancy;)Ljava/util/stream/Stream;"),
            cancellable = true
    )
    *///?} else {
    @Inject(
            method = "method_46885",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/village/poi/PoiManager;findAllClosestFirstWithType(Ljava/util/function/Predicate;Ljava/util/function/Predicate;Lnet/minecraft/core/BlockPos;ILnet/minecraft/world/entity/ai/village/poi/PoiManager$Occupancy;)Ljava/util/stream/Stream;"),
            cancellable = true
    )
    //?}
    //? if > 1.21.1 && < 1.21.8 {
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
            CallbackInfoReturnable<Boolean> cir
            ) {
            //?} else if = 1.21.1 || = 1.21 {
        /*private static void collect(
                boolean bl,
                MutableLong mutableLong,
                Long2ObjectMap long2ObjectMap,
                Predicate predicate,
                MemoryAccessor memoryAccessor,
                Optional optional,
                ServerLevel serverLevel,
                PathfinderMob pathfinderMob,
                long l,
                CallbackInfoReturnable<Boolean> cir
        ){
            *///?} else if >= 1.21.8 {
    /*private static void collect(
            boolean bl,
            MutableLong mutableLong,
            Long2ObjectMap long2ObjectMap,
            Predicate predicate,
            BiPredicate biPredicate,
            MemoryAccessor memoryAccessor,
            Optional optional,
            ServerLevel serverLevel,
            PathfinderMob pathfinderMob,
            long l,
            CallbackInfoReturnable<Boolean> cir
    ){
        *///?}
        if (
                AcaSetting.villagerOptimization
                && pathfinderMob instanceof VillagerAccessor villager
                && villager.aca$canDisableAI()
        ) {
            if ((pathfinderMob.tickCount + pathfinderMob.getId() % 807) % 1609 != 0) {
                cir.setReturnValue(true);
            }
        }
    }
}
