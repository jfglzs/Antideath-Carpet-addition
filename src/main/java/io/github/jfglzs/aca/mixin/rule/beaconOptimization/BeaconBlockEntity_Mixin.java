package io.github.jfglzs.aca.mixin.rule.beaconOptimization;


import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntity_Mixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 10)
    )
    private static int modifyColorChangeInterval(int constant) {
        return AcaSetting.beaconLagOptimization ? constant / 10 : constant;
    }

    @ModifyVariable(
            method = "applyEffects",
            at = @At("STORE")
    )
    private static double modifyRange(double range) {
        return AcaSetting.beaconRange == 0 ? range : AcaSetting.beaconRange;
    }
}
