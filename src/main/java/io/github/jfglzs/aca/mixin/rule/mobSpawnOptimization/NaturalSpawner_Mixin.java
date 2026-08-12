package io.github.jfglzs.aca.mixin.rule.mobSpawnOptimization;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.SpawnStateAccessor;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(NaturalSpawner.class)
public class NaturalSpawner_Mixin {
    //? if > 1.21.1 {
    @Inject(
            method = "getFilteredSpawningCategories",
            at = @At(value = "RETURN")
    )
            //? if < 26.2 {
    private static void getFilteredSpawningCategories(NaturalSpawner.SpawnState state, boolean spawnFriendlies, boolean spawnEnemies, boolean spawnPersistent, CallbackInfoReturnable<List<MobCategory>> cir, @Local List<MobCategory> spawningCategories) {
        //?} else {
        // private static void getFilteredSpawningCategories(final NaturalSpawner.SpawnState state, final boolean spawnEnemies, final boolean spawnPersistent, CallbackInfoReturnable<List<MobCategory>> cir, @Local List<MobCategory> spawningCategories) {
        //?}
        if (AcaSetting.mobSpawnOptimization) {
            if (spawningCategories.isEmpty()) return;

            if (!spawnPersistent && ((SpawnStateAccessor) state).ACA$canSpawnForCategoryGlobal(MobCategory.MONSTER)) {
                spawningCategories.clear();
            }
        }
    }
    //?}
}
