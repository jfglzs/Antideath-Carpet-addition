package io.github.jfglzs.aca.mixin.rule.fakePlaceOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public class WitherBoss_Mixin extends Monster implements EntityAccessor {
    @Unique
    private int count = -1;

    protected WitherBoss_Mixin(EntityType<? extends Monster> entityType, ServerLevel world) {
        super(entityType, world);
    }

    @Inject(
            method = "aiStep",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tickMovement_Inject(CallbackInfo ci) {
        if (count > 70) ci.cancel();
    }


    @Inject(
            method = "customServerAiStep",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void mobTick_Inject(ServerLevel world, CallbackInfo ci) {
        if (!AcaSetting.fakePeaceOptimization) return;

        if ((this.tickCount + this.getId() % 13) % 200 == 0 || count == -1) {
            WitherBoss entity = (WitherBoss) ((Object) this);

            AABB box = new AABB(
                    entity.position().add(0.5, 0.5, 0.5),
                    entity.position().add(-0.5, -0.5, -0.5)
            );

            count = world.getEntities(
                    EntityType.WITHER,
                    box,
                    e -> true
            ).size();
        }

        if (count > 70) ci.cancel();
    }

    @Override
    public int aca$getCount() {
        return count;
    }
}
