package io.github.jfglzs.aca.mixin.rule.fakePlaceOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherEntity.class)
public class WitherEntity_Mixin extends HostileEntity implements EntityAccessor {
    @Unique
    private int count = -1;

    protected WitherEntity_Mixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "tickMovement",
            at = @At("HEAD"),
            cancellable = true
    )
    public void tickMovement_Inject(CallbackInfo ci) {
        if (count > 70) ci.cancel();
    }


    @Inject(
            method = "mobTick",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void mobTick_Inject(ServerWorld world, CallbackInfo ci) {
        if (!AcaSetting.fakePeaceOptimization) return;

        if ((this.age + this.getId() % 13) % 200 == 0 || count == -1) {
            WitherEntity entity = (WitherEntity) ((Object) this);

            Box box = new Box(
                    entity.getPos().add(0.5, 0.5, 0.5),
                    entity.getPos().add(-0.5, -0.5, -0.5)
            );

            count = world.getEntitiesByType(
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
