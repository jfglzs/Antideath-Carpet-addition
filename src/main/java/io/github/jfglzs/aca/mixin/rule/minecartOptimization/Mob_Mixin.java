package io.github.jfglzs.aca.mixin.rule.minecartOptimization;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jfglzs.aca.accessors.AbstractMinecartAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class Mob_Mixin extends LivingEntity {
    protected Mob_Mixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
            method = "serverAiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;tick()V")
    )
    private void serverAiStep(GoalSelector selector, Operation<Void> original) {
        if (this.getVehicle() instanceof AbstractMinecartAccessor accessor && accessor.aca$canDisable()) return;
        original.call(selector);
    }
}
