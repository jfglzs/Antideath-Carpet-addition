package io.github.jfglzs.aca.mixin.rule.minecartOptimization;

import io.github.jfglzs.aca.accessors.AbstractMinecartAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_Mixin extends Entity {
    @Unique
    private boolean aca$canDisable = false;

    public LivingEntity_Mixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void applyEffectsFromBlocks() {
        super.applyEffectsFromBlocks();
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void tick(CallbackInfo ci) {
        this.aca$canDisable =
                this.getVehicle() instanceof AbstractMinecartAccessor accessor &&
                accessor.aca$canDisable();
    }

    @SuppressWarnings("all")
    @Inject(
            method = {"applyEffectsFromBlocks"},
            at = @At("HEAD")
    )
    private void applyEffectsFromBlocks_Inject(CallbackInfo ci) {
        if (aca$canDisable) {
            ci.cancel();
        }
    }

    @Inject(
            method = "pushEntities",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pushEntities(CallbackInfo ci) {
        if (aca$canDisable) {
            ci.cancel();
        }
    }

    @Inject(
            method = "travel",
            at = @At("HEAD"),
            cancellable = true
    )
    private void travel(CallbackInfo ci) {
        if (aca$canDisable) {
            ci.cancel();
        }
    }
}
