package io.github.jfglzs.aca.mixin.rule.beeOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.GoalAccessor;
import io.github.jfglzs.aca.accessors.GoalSelectorAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.bee.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GoalSelector.class)
public class GoalSelector_Mixin implements GoalSelectorAccessor {
    private Entity aca$entity;

    @Override
    public void aca$setEntity(Entity entity) {
        this.aca$entity = entity;
    }

    @WrapOperation(
            method = "tickRunningGoals",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/WrappedGoal;isRunning()Z")
    )
    public boolean isRunning(WrappedGoal wrappedGoal, Operation<Boolean> original) {
        if (AcaSetting.beeOptimization) {
            GoalAccessor goal = (GoalAccessor) wrappedGoal.getGoal();
            if (this.aca$entity instanceof Bee && goal.aca$getAccessiblie()) {
                return false;
            }
        }
        return original.call(wrappedGoal);
    }
}
