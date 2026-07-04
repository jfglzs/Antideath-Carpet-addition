package io.github.jfglzs.aca.mixin.rule.minecartOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.AbstractMinecartAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class Entity_Mixin {
    @Inject(
            method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addPassenger(Lnet/minecraft/world/entity/Entity;)V")
    )
    public final void startRiding(Entity entity, boolean force, boolean sendEventAndTriggers, CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.minecartOptimization && entity instanceof AbstractMinecartAccessor minecart) {
            minecart.aca$setCanDisable(((Entity) (Object) this) instanceof WitherSkeleton);
        }
    }

    @Inject(
            method = "collide",
            at = @At("HEAD"),
            cancellable = true
    )
    public void collide(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
        if (this instanceof AbstractMinecartAccessor accessor && accessor.aca$canDisable()) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }
}
