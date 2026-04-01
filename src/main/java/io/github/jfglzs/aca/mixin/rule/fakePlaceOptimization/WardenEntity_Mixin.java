package io.github.jfglzs.aca.mixin.rule.fakePlaceOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WardenEntity.class)
public class WardenEntity_Mixin extends HostileEntity implements EntityAccessor {
    @Unique
    private int count = -1;

    protected WardenEntity_Mixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "mobTick",
            at = @At("HEAD")
    )
    protected void mobTick_Inject(ServerWorld world, CallbackInfo ci) {
        if (!AcaSetting.fakePeaceOptimization) return;

        if ((this.age + this.getId() % 13) % 200 == 0 || count == -1) {
            WardenEntity entity = (WardenEntity) ((Object) this);

            Box box = new Box(
                    entity.getPos().add(0.5, 0.5, 0.5),
                    entity.getPos().add(-0.5, -0.5, -0.5)
            );

            System.out.println(count = world.getEntitiesByType(
                    EntityType.WARDEN,
                    box,
                    e -> true
            ).size());
        }
    }

    @Override
    public int aca$getCount() {
        return count;
    }
}
