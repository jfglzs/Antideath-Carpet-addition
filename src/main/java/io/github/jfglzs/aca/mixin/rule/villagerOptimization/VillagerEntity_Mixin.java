package io.github.jfglzs.aca.mixin.rule.villagerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerEntity_Mixin implements VillagerAccessor {
    @Unique
    private int count = -1;
    @Unique
    private int countGolem = 0;

    @Inject(
            method = "mobTick",
            at = @At("HEAD")
    )
    protected void mobTick_Inject(ServerWorld world, CallbackInfo ci) {
        if (AcaSetting.villagerOptimization) {
            VillagerEntity entity = (VillagerEntity) ((Object) this);
            boolean bl = entity.isSleeping();

            if ((entity.age + entity.getId() % 807) % 400 == 0 && !bl || count == -1 && !bl) {

                Box box = new Box(
                        entity.getPos().add(0.5, 0.5, 0.5),
                        entity.getPos().add(-0.5, -0.5, -0.5)
                );

                count = world.getEntitiesByType(
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
            method = "summonGolem",
            at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")
    )
    private void foreach_Inject(ServerWorld world, long time, int requiredCount, CallbackInfo ci) {
        countGolem++;
    }
}
