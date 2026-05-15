package io.github.jfglzs.aca.mixin.rule.superSponge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(SpongeBlock.class)
public class SpongeBlock_Mixin {
    @WrapOperation(
            method = "removeWaterBreadthFirstSearch",
            //? if > 1.21.1 {
            //at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;breadthFirstTraversal(Lnet/minecraft/core/BlockPos;IILjava/util/function/BiConsumer;Ljava/util/function/Function;)I")
            //?} else {
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;breadthFirstTraversal(Lnet/minecraft/core/BlockPos;IILjava/util/function/BiConsumer;Ljava/util/function/Predicate;)I")
            //?}
    )
    //? if > 1.21.1 {
    //private int iterateRecursively_Wrap(BlockPos blockPos, int maxDepth, int maxIterations, BiConsumer biConsumer, Function fp, Operation<Integer> original) {
    //?} else {
        private int iterateRecursively_Wrap(BlockPos blockPos, int maxDepth, int maxIterations, BiConsumer biConsumer, Predicate fp, Operation<Integer> original) {
    //?}
        int customMaxDep = AcaSetting.superSponge ? 192 : maxDepth;
        int customMaxIterations = AcaSetting.superSponge ? 2080 : maxIterations;
        return original.call(blockPos, customMaxDep, customMaxIterations, biConsumer, fp);
    }
}
