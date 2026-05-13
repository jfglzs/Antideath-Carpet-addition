package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Pair;
import io.github.jfglzs.aca.utils.warp.BehaviorWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(VillagerGoalPackages.class)
public abstract class VillagerGoalPackages_Mixin {
    @Shadow
    private static boolean validateBedPoi(ServerLevel level, BlockPos blockPos) {
        return false;
    }

    @WrapMethod(
            method = "getCorePackage"
    )
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getCorePackage_Wrap(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {

        return ImmutableList.of(
                Pair.of(0, BehaviorWrapper.Wrap(new Swim(0.8F))),
                Pair.of(0, BehaviorWrapper.Wrap(InteractWithDoor.create())),
                Pair.of(0, BehaviorWrapper.Wrap(new LookAtTargetSink(45, 90))),

                //刷铁相关AI
                Pair.of(0, new VillagerPanicTrigger()),
                //刷铁相关AI
                Pair.of(0, WakeUp.create()),
                //刷铁相关AI
                Pair.of(0, ReactToBell.create()),

                Pair.of(0, BehaviorWrapper.Wrap(SetRaidStatus.create())),
                Pair.of(0, BehaviorWrapper.Wrap(ValidateNearbyPoi.create(holder.value().heldJobSite(), MemoryModuleType.JOB_SITE))),
                Pair.of(0, BehaviorWrapper.Wrap(ValidateNearbyPoi.create(holder.value().acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE))),
                Pair.of(1, BehaviorWrapper.Wrap(new MoveToTargetSink())), Pair.of(2, PoiCompetitorScan.create()),
                Pair.of(3, BehaviorWrapper.Wrap(new LookAndFollowTradingPlayerSink(f))),
                new Pair[] {
                        Pair.of(5, BehaviorWrapper.Wrap(GoToWantedItem.create(f, false, 4))), Pair.of(6, AcquirePoi.create(holder.value().acquirableJobSite(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty(), (serverLevel, blockPos) -> true)),
                        Pair.of(7, BehaviorWrapper.Wrap(new GoToPotentialJobSite(f))),
                        Pair.of(8, BehaviorWrapper.Wrap(YieldJobSite.create(f))),

                        //刷铁相关AI
                        Pair.of(10, AcquirePoi.create((holderx) -> holderx.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte) 14), VillagerGoalPackages_Mixin::validateBedPoi)),
                        Pair.of(10, BehaviorWrapper.Wrap(AcquirePoi.create((holderx) -> holderx.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14)))),
                        Pair.of(10, BehaviorWrapper.Wrap(AssignProfessionFromJobSite.create())),

                        //刷铁相关AI
                        Pair.of(10, ResetProfession.create())
                }
        );
    }

    @WrapMethod(
            method = "getIdlePackage"
    )
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getIdlePackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
        return cullAll(original.call(holder, f), ImmutableList.of(99));
    }

    @WrapMethod(
            method = "getPreRaidPackage"
    )
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPreRaidPackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
        return cullAll(original.call(holder, f), null);
    }

    @WrapMethod(
            method = "getRaidPackage"
    )
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRaidPackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
        return cullAll(original.call(holder, f),null);
    }

    @WrapMethod(
            method = "getPanicPackage"
    )
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPanicPackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
        return cullAll(original.call(holder, f), ImmutableList.of(0));
    }

    @Unique
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> cullAll(
            ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> pairs,
            ImmutableList<Integer> except
    ) {
        List<Pair<Integer, ? extends BehaviorControl<? super Villager>>> list = new ArrayList<>();
        pairs.forEach(
                pair -> {
                    var first = pair.getFirst();
                    var second = pair.getSecond();
                    if (except != null && except.contains(first)) {
                        list.add(new Pair<>(first, second));
                    } else {
                        list.add(new Pair<>(first, BehaviorWrapper.Wrap(second)));
                    }
                }
        );
        return ImmutableList.copyOf(list);
    }
}
