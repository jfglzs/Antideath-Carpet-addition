package io.github.jfglzs.aca.mixin.rule.boatOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.IVehicleAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class Entity_Mixin {
    @Inject(
            method = "move",
            at = @At("HEAD"),
            cancellable = true
    )
    public void move(MoverType moverType, Vec3 delta, CallbackInfo ci) {
        if (AcaSetting.boatOptimization && this instanceof IVehicleAccessor boat && boat.aca$getRideCount() > 10) {
            ci.cancel();
        }
    }

    @Inject(
            method = "baseTick",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void baseTick(CallbackInfo ci) {
        if (AcaSetting.boatOptimization && this instanceof IVehicleAccessor boat && boat.aca$getRideCount() > 10) {
            ci.cancel();
        }
    }
}
