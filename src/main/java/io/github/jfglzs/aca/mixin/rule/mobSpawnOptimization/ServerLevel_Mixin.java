package io.github.jfglzs.aca.mixin.rule.mobSpawnOptimization;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerLevel.class)
public abstract class ServerLevel_Mixin {
    @Shadow
    public abstract ServerLevel getLevel();

    @ModifyConstant(
            method = "tickChunk",
            constant = @Constant(intValue = 0, ordinal = 0)
    )
    public int tickChunk(int constant) {
        boolean isOverWorld = this.getLevel().dimension() == Level.OVERWORLD;
        return AcaSetting.mobSpawnOptimization && !isOverWorld ? Integer.MAX_VALUE : constant;
    }
}
