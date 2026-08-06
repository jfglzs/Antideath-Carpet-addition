package io.github.jfglzs.aca.mixin.rule.hardDecoratedPot;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlock_Mixin {
    @Inject(
            method = "onProjectileHit",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile, CallbackInfo ci) {
        if (AcaSetting.hardDecoratedBlock) {
            ci.cancel();
        }
    }
}
