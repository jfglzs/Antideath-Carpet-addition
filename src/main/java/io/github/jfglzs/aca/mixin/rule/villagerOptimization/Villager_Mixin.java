package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class Villager_Mixin implements VillagerAccessor {
    @Unique
    private int count = -1;
    @Unique
    private int countGolem = 0;

    @Inject(
            method = "customServerAiStep",
            at = @At("HEAD")
    )
    protected void mobTick_Inject(ServerLevel world, CallbackInfo ci) {
        if (AcaSetting.villagerOptimization) {
            Villager entity = (Villager) ((Object) this);
            boolean bl = entity.isSleeping();

            if ((entity.tickCount + entity.getId() % 807) % 400 == 0 && !bl || count == -1 && !bl) {

                AABB box = new AABB(
                        entity.position().add(0.5, 0.5, 0.5),
                        entity.position().add(-0.5, -0.5, -0.5)
                );

                count = world.getEntities(
                        EntityType.VILLAGER,
                        box,
                        e -> true
                ).size();
            }
        }
    }

    @Override
    public boolean aca$canDisableAI() {
        return count == 3 && countGolem >= 1;
    }

    @Inject(
            method = "spawnGolemIfNeeded",
            at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")
    )
    private void foreach_Inject(ServerLevel world, long time, int requiredCount, CallbackInfo ci) {
        countGolem++;
    }
}
