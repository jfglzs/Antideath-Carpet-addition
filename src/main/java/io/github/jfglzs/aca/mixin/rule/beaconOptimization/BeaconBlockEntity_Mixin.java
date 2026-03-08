package io.github.jfglzs.aca.mixin.rule.beaconOptimization;


import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntity_Mixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(longValue = 80L)
    )
    private static long modifyCheckInterval(long constant) {
        return AcaSetting.beaconLagOptimization ? constant * 2 : constant;
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 10)
    )
    private static int modifyColorChangeInterval(int constant) {
        return AcaSetting.beaconLagOptimization ? constant / 10 : constant;
    }

    @ModifyVariable(
            method = "applyPlayerEffects",
            at = @At("STORE"),
            ordinal = 0
    )
    private static double modifyRange(double d) {
        return AcaSetting.beaconRange == 0 ? d : AcaSetting.beaconRange;
    }
}
