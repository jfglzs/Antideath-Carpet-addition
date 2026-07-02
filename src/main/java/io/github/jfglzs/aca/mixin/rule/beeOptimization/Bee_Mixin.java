package io.github.jfglzs.aca.mixin.rule.beeOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.accessors.GoalAccessor;
import io.github.jfglzs.aca.accessors.GoalSelectorAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
//~ if >= 1.21.11 '.animal.Bee' -> '.animal.bee.Bee' {
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.bee.Bee;
//~}
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class Bee_Mixin extends Animal {
    @Unique
    private boolean[] aca$list;

    @Unique
    private int aca$count = 0;

    protected Bee_Mixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Unique
    private boolean aca$ensureInit() {
        if (aca$list == null) {
            this.aca$list = new boolean[] {
                    true,  // 0: BeeAttackGoal
                    false, // 1: BeeEnterHiveGoal
                    false,  // 2: BreedGoal
                    true,  // 3: TemptGoal
                    false, // 4: ValidateHiveGoal
                    false, // 5: ValidateFlowerGoal
                    true,  // 6: BeePollinateGoal
                    true,  // 7: FollowParentGoal
                    false, // 8: BeeLocateHiveGoal
                    false, // 9: BeeGoToHiveGoal
                    false, // 10: BeeGoToKnownFlowerGoal
                    true,  // 11: BeeGrowCropGoal
                    true,  // 12: BeeWanderGoal
                    true,  // 13: FloatGoal
                    true,  // 14: BeeHurtByOtherGoal
                    true,  // 15: BeeBecomeAngryTargetGoal
                    true   // 16: ResetUniversalAngerTargetGoal
            };
        }
        return true;
    }

    @Inject(
            method = "registerGoals",
            at = @At("HEAD")
    )
    private void registerGoals(CallbackInfo ci) {
        ((GoalSelectorAccessor) this.goalSelector).aca$setEntity(this);
        ((GoalSelectorAccessor) this.targetSelector).aca$setEntity(this);
    }

    @WrapOperation(
            method = "registerGoals",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V")
    )
    protected void registerGoals(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {
        if (this.aca$ensureInit()) {
            ((GoalAccessor) goal).aca$setAccessible(this.aca$list[aca$count]);
            aca$count++;
            original.call(instance, priority, goal);
        }
    }
}
