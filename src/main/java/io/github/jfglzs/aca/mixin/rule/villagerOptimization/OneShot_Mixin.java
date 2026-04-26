package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OneShot.class)
public class OneShot_Mixin<E extends LivingEntity> {
    @Inject(
            method = "tryStart",
            at = @At("HEAD"),
            cancellable = true
    )
    public final void tryStarting_Inject(ServerLevel world, E entity, long time, CallbackInfoReturnable<Boolean> cir) {
        if (
                entity instanceof Villager villager
                        && AcaSetting.villagerOptimization
                        && ((VillagerAccessor) villager).aca$canDisableAI()
                        && !((entity.tickCount + entity.getId()) % 40 == 0)
        ) {
            cir.setReturnValue(false);
        }
    }
}
