package io.github.jfglzs.aca.mixin.rule.beeOptimization;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.GoalAccessor;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.bee.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GoalSelector.class)
public class GoalSelector_Mixin {
    @WrapWithCondition(
            method = "tickRunningGoals",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/WrappedGoal;tick()V")
    )
    public boolean tickRunningGoals_Mixin(WrappedGoal instance) {
        var goal = ((GoalAccessor) instance.getGoal());
        return !AcaSetting.beeOptimization || !(goal.aca$getEntity() instanceof Bee && goal.aca$getAccessiblie());
    }

    @WrapWithCondition(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/WrappedGoal;start()V")
    )
    public boolean tick(WrappedGoal instance) {
        var goal = ((GoalAccessor) instance.getGoal());
        return !AcaSetting.beeOptimization || !(goal.aca$getEntity() instanceof Bee && goal.aca$getAccessiblie());
    }
}
