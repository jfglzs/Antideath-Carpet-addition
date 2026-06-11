package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Pair;
import io.github.jfglzs.aca.utils.wrap.FullSuppressBehaviorWrapper;
//? if >= 1.21.5 {
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.core.Holder;
//?}
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Mixin(VillagerGoalPackages.class)
public abstract class VillagerGoalPackages_Mixin {
    //? if >= 1.21.5 {
    @Shadow
    private static boolean validateBedPoi(ServerLevel level, BlockPos blockPos) {
        return false;
    }
    //?}

    @WrapMethod(
            method = "getIdlePackage"
    )
    //? if >= 1.21.5 && < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getIdlePackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else if < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getIdlePackage(VillagerProfession holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else {
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getIdlePackage(float speedModifier, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    //?}
        //? if < 26.1 {
        /*return cullAll(original.call(holder, f), ImmutableList.of(99));
        *///?} else {
        return cullAll(original.call(speedModifier), ImmutableList.of(99));
        //?}
    }

    @WrapMethod(
            method = "getCorePackage"
    )
    //? if >= 1.21.5 {
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getCorePackage_Wrap(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    //?} else if < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getCorePackage_Wrap(VillagerProfession holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?}
        return ImmutableList.of(
                Pair.of(0, FullSuppressBehaviorWrapper.wrap(new Swim(0.8F))),
                Pair.of(0, FullSuppressBehaviorWrapper.wrap(InteractWithDoor.create())),
                Pair.of(0, FullSuppressBehaviorWrapper.wrap(new LookAtTargetSink(45, 90))),

                //刷铁相关AI
                Pair.of(0, new VillagerPanicTrigger()),
                //刷铁相关AI
                Pair.of(0, WakeUp.create()),
                //刷铁相关AI
                Pair.of(0, ReactToBell.create()),

                Pair.of(0, FullSuppressBehaviorWrapper.wrap(SetRaidStatus.create())),
                //? if >= 1.21.5 {
                Pair.of(0, FullSuppressBehaviorWrapper.wrap(ValidateNearbyPoi.create(holder.value().heldJobSite(), MemoryModuleType.JOB_SITE))),
                Pair.of(0, FullSuppressBehaviorWrapper.wrap(ValidateNearbyPoi.create(holder.value().acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE))),
                //?} else {
                /*Pair.of(0, FullSuppressBehaviorWrapper.wrap(ValidateNearbyPoi.create(holder.heldJobSite(), MemoryModuleType.JOB_SITE))),
                Pair.of(0, FullSuppressBehaviorWrapper.wrap(ValidateNearbyPoi.create(holder.acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE))),
                *///?}
                Pair.of(1, FullSuppressBehaviorWrapper.wrap(new MoveToTargetSink())),
                Pair.of(2, FullSuppressBehaviorWrapper.wrap(PoiCompetitorScan.create())),
                Pair.of(3, FullSuppressBehaviorWrapper.wrap(new LookAndFollowTradingPlayerSink(f))),
                new Pair[] {
                        //? if >= 1.21.5 {
                        Pair.of(5, FullSuppressBehaviorWrapper.wrap(GoToWantedItem.create(f, false, 4))),
                        Pair.of(6, FullSuppressBehaviorWrapper.wrap(AcquirePoi.create(holder.value().acquirableJobSite(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty(), (serverLevel, blockPos) -> true))),
                        //?} else {
                        /*Pair.of(5, FullSuppressBehaviorWrapper.wrap(GoToWantedItem.create(f, false, 4))),
                        Pair.of(0, FullSuppressBehaviorWrapper.wrap(ValidateNearbyPoi.create(holder.acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE))),
                        *///?}
                        Pair.of(7, FullSuppressBehaviorWrapper.wrap(new GoToPotentialJobSite(f))),
                        Pair.of(8, FullSuppressBehaviorWrapper.wrap(YieldJobSite.create(f))),

                        //刷铁相关AI
                        //? if >= 1.21.5 {
                        Pair.of(10, FullSuppressBehaviorWrapper.wrap(AcquirePoi.create((holderx) -> holderx.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte) 14), VillagerGoalPackages_Mixin::validateBedPoi))),
                        //?} else {
                        /*Pair.of(10, FullSuppressBehaviorWrapper.wrap(AcquirePoi.create((holderx) -> holderx.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte) 14)))),
                        *///?}

                        Pair.of(10, FullSuppressBehaviorWrapper.wrap(AcquirePoi.create((holderx) -> holderx.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14)))),
                        Pair.of(10, FullSuppressBehaviorWrapper.wrap(AssignProfessionFromJobSite.create())),

                        //刷铁相关AI
                        Pair.of(10, ResetProfession.create())
                }
        );
    }

    @WrapMethod(
            method = "getPreRaidPackage"
    )
    //? if >= 1.21.5 && < 26.1  {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPreRaidPackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else if < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPreRaidPackage(VillagerProfession holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else {
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPreRaidPackage(float speedModifier, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    //?}
        //? if < 26.1 {
        /*return cullAll(original.call(holder, f), null);
        *///?}else {
        return cullAll(original.call(speedModifier), null);
        //?}
    }

    @WrapMethod(
            method = "getRaidPackage"
    )
    //? if >= 1.21.5 && < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRaidPackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else if < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRaidPackage(VillagerProfession holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else {
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRaidPackage(float speedModifier, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    //?}
        //? if < 26.1 {
        /*return cullAll(original.call(holder, f), null);
        *///?}else {
        return cullAll(original.call(speedModifier), null);
        //?}
    }

    @WrapMethod(
            method = "getPanicPackage"
    )
    //? if >= 1.21.5 && < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPanicPackage(Holder<VillagerProfession> holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else if < 26.1 {
    /*private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPanicPackage(VillagerProfession holder, float f, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    *///?} else {
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPanicPackage(float speedModifier, Operation<ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>>> original) {
    //?}
        //? if < 26.1 {
        /*return cullAll(original.call(holder, f), ImmutableList.of(0));
        *///?} else {
        return cullAll(original.call(speedModifier), ImmutableList.of(0));
        //?}
    }

    @Unique
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> cullAll(
            ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> pairs,
            ImmutableList<Integer> except
    ) {
        List<Pair<Integer, ? extends BehaviorControl<? super Villager>>> list = new ArrayList<>();
        ImmutableList<Integer> excepts = except == null ? ImmutableList.of() : except;

        pairs.forEach(
                pair -> {
                    var first = pair.getFirst();
                    var second = pair.getSecond();

                    if (excepts.contains(first)) {
                        list.add(Pair.of(first, second));
                    }
                    else {
                        list.add(Pair.of(first, FullSuppressBehaviorWrapper.wrap(second)));
                    }
                }
        );
        return ImmutableList.copyOf(list);
    }
}
