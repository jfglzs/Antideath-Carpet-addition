package io.jfglzs.ad_carpet_addition.mixin;


import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.jfglzs.ad_carpet_addition.AcaSetting.ItemFrameAlwaysStayAtach;

@Mixin(ItemFrameEntity.class)
public class itemFrameAlwaysStayAttached {
    @Inject(method = "canStayAttached" , at = @At("HEAD"), cancellable = true)
    private void injectCanStayAttached(CallbackInfoReturnable<Boolean> cir) {
        if (ItemFrameAlwaysStayAtach){
            cir.setReturnValue(true);
        }
    }
}
