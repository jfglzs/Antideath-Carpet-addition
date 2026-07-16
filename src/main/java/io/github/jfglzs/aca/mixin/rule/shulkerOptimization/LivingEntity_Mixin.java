package io.github.jfglzs.aca.mixin.rule.shulkerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.LivingEntityAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_Mixin implements LivingEntityAccessor {
    @Unique
    private boolean aca$pushed = false;
    @Unique
    private int aca$pushedCount = 0;

    @Inject(
            method = "pushEntities",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void pushEntities(CallbackInfo ci) {
        if (AcaSetting.shulkerOptimization) {
            if (this.aca$pushed) {
                this.aca$pushedCount++;
                if (this.aca$pushedCount % 5 == 1) {
                    ci.cancel();
                    this.aca$pushed = false;
                }
            }
        }
    }

    @Override
    public void aca$setPushed(boolean pushed) {
        this.aca$pushed = pushed;
    }
}
