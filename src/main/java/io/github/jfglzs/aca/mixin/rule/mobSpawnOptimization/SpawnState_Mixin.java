package io.github.jfglzs.aca.mixin.rule.mobSpawnOptimization;

import io.github.jfglzs.aca.accessors.SpawnStateAccessor;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if = 1.21.1 {
//import io.github.jfglzs.aca.mixin.rule.mobSpawnOptimization.NaturalSpawnerAccessor;
//import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
//import org.spongepowered.asm.mixin.Final;
//?}

@Mixin(NaturalSpawner.SpawnState.class)
public abstract class SpawnState_Mixin implements SpawnStateAccessor {
    //? if > 1.21.1 {
    @Shadow
    protected abstract boolean canSpawnForCategoryGlobal(MobCategory category);
    //?}

    //? if = 1.21.1 {
    //@Shadow
    //@Final
    //private int spawnableChunkCount;
    //
    //@Shadow
    //@Final
    //private Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
    //?}

    @Override
    public boolean ACA$canSpawnForCategoryGlobal(MobCategory category) {
        //? if > 1.21.1 {
        return this.canSpawnForCategoryGlobal(category);
        //?} else {
        //int i = category.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawnerAccessor.ASA$getMagicNumber();
        //return this.mobCategoryCounts.getInt(category) < i
        //?}
    }

}
