package io.github.jfglzs.aca.mixin.rule;


import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static io.github.jfglzs.aca.AcaSetting.beaconLagOptimization;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntity_Mixin extends BlockEntity {
    public BeaconBlockEntity_Mixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(longValue = 80L)
    )
    private static long modifyCheckInterval(long constant) {
        return beaconLagOptimization ? constant * 2 : constant;
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 10)
    )
    private static int modifyColorChangeInterval(int constant) {
        return beaconLagOptimization ? constant / 10 : constant;
    }
}
