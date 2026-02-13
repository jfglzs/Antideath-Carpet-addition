package xyz.antideath.aca.mixin.rule;


import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static xyz.antideath.aca.AcaSetting.beaconLagOptimization;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntity_Mixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(longValue = 80L)
    )
    private static long modifyCheckInterval(long constant)
    {
        return beaconLagOptimization ? constant * 4 : constant;
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 10))
    private static int modifyColorChangeInterval(int constant) {
        return beaconLagOptimization ? constant / 10 : constant;
    }
}
