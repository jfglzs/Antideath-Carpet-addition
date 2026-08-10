package io.github.jfglzs.aca.mixin.rule.mobSpawnOptimization;

import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerAccessor {
    @Accessor("MAGIC_NUMBER")
    static int ASA$getMagicNumber() {
        return 0;
    }
}

