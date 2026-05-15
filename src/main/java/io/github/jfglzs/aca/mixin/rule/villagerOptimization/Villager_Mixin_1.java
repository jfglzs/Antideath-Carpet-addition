package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.mixin.Invoker.SensorType_Invoker;
import io.github.jfglzs.aca.utils.wrap.SensorWrapper;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.*;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.world.entity.ai.sensing.SensorType.*;

@Mixin(Villager.class)
public class Villager_Mixin_1 {
    @Unique
    private static final ReferenceOpenHashSet<SensorType<?>> SENSORS = new ReferenceOpenHashSet<>(Set.of(
            SECONDARY_POIS,
            VILLAGER_BABIES,
            NEAREST_PLAYERS,
            NEAREST_ITEMS,
            HURT_BY
    ));

    //? if < 26.1 {
    /*@WrapOperation(
            method = "brainProvider",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/Brain;provider(Ljava/util/Collection;Ljava/util/Collection;)Lnet/minecraft/world/entity/ai/Brain$Provider;")
    )
    protected Brain.Provider<Villager> brainProvider_Wrap(
            final Collection<? extends MemoryModuleType<?>> MEMORY_TYPES,
            final Collection<? extends SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES,
            Operation<Brain.Provider<Villager>> original
    ) {
    *///?} else {
    @WrapOperation(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/Brain;provider(Ljava/util/Collection;Lnet/minecraft/world/entity/ai/Brain$ActivitySupplier;)Lnet/minecraft/world/entity/ai/Brain$Provider;")
    )

    private static Brain.Provider<Villager> brainProvider_Wrap(
            final Collection<? extends SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES,
            final Brain.ActivitySupplier<Villager> ACTIVITIES,
            Operation<Brain.Provider<Villager>> original
    ) {
    //?}
        final var WRAPPED_SENSORS = SENSOR_TYPES
                .stream()
                .filter(SENSORS::contains)
                .map(type -> SensorWrapper.wrap(type.create()))
                .map(sensor -> SensorType_Invoker.SensorType(() -> sensor))
                .collect(ImmutableList.toImmutableList());
        //? if < 26.1 {
         /*return original.call(MEMORY_TYPES, WRAPPED_SENSORS);
        *///?} else {
        return original.call(WRAPPED_SENSORS, ACTIVITIES);
        //?}
    }
}
