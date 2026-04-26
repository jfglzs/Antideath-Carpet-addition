package io.github.jfglzs.aca.mixin.rule.ItemFrameAlwaysStayAttach;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrame_Mixin {
    @Inject(
            method = "survives",
            at = @At("HEAD"),
            cancellable = true
    )
    private void CanStayAttached_Inject(CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.ItemFrameAlwaysStayAttach) {
            cir.setReturnValue(true);
        }
    }
}
