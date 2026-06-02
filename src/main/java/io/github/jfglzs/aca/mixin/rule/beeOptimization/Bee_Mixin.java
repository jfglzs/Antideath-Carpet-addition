package io.github.jfglzs.aca.mixin.rule.beeOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.accessors.GoalAccessor;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
//~ if >= 1.21.11 '.animal.Bee' -> '.animal.bee.Bee' {
import net.minecraft.world.entity.animal.bee.Bee;
//~}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.class)
public class Bee_Mixin {
    @Unique
    private boolean[] aca$list = new boolean[] {
            false,  // 0: BeeAttackGoal
            true,   // 1: BeeEnterHiveGoal
            false,  // 2: BreedGoal
            false,  // 3: TemptGoal
            true,   // 4: ValidateHiveGoal
            true,   // 5: ValidateFlowerGoal
            false,  // 6: BeePollinateGoal
            false,  // 7: FollowParentGoal
            true,   // 8: BeeLocateHiveGoal
            true,   // 9: BeeGoToHiveGoal
            true,   // 10: BeeGoToKnownFlowerGoal
            false,  // 11: BeeGrowCropGoal
            false,  // 12: BeeWanderGoal
            false,  // 13: FloatGoal
            false,  // 14: BeeHurtByOtherGoal
            false,  // 15: BeeBecomeAngryTargetGoal
            false   // 16: ResetUniversalAngerTargetGoal
    };

    @Unique
    private int aca$count = 0;

    @WrapOperation(
            method = "registerGoals",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V")
    )
    protected void registerGoals(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {
        ((GoalAccessor) goal).aca$setAccessible(aca$list[aca$count]);
        aca$count++;
        original.call(instance, priority, goal);
    }
}
