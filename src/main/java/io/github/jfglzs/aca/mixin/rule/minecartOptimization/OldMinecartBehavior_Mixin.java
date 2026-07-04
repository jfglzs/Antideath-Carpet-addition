package io.github.jfglzs.aca.mixin.rule.minecartOptimization;

//? if >= 1.21.4 {
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.AbstractMinecartAccessor;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OldMinecartBehavior.class)
public abstract class OldMinecartBehavior_Mixin extends MinecartBehavior {
    protected OldMinecartBehavior_Mixin(AbstractMinecart minecart) {
        super(minecart);
    }

    @Inject(
            method = "pushAndPickupEntities",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pushAndPickupEntities(CallbackInfoReturnable<Boolean> cir){
        if (this.minecart instanceof AbstractMinecartAccessor accessor && accessor.aca$canDisable()) {
            cir.setReturnValue(false);
        }
    }
}
//?}