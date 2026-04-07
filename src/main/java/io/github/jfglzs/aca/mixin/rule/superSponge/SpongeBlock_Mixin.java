package io.github.jfglzs.aca.mixin.rule.superSponge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.block.SpongeBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Mixin(SpongeBlock.class)
public class SpongeBlock_Mixin {
    @WrapOperation(
            method = "absorbWater",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;iterateRecursively(Lnet/minecraft/util/math/BlockPos;IILjava/util/function/BiConsumer;Ljava/util/function/Function;)I")
    )
    private int iterateRecursively_Wrap(BlockPos blockPos, int maxDepth, int maxIterations, BiConsumer biConsumer, Function function, Operation<Integer> original) {
        int customMaxDep = AcaSetting.superSponge ? 192 : maxDepth;
        int customMaxIterations = AcaSetting.superSponge ? 2080 : maxIterations;
        return original.call(blockPos, customMaxDep, customMaxIterations, biConsumer, function);
    }
}
