package io.github.jfglzs.aca.mixin.rule.flippinToTemOfUndying;

import carpet.CarpetSettings;
import carpet.helpers.BlockRotator;
import com.google.common.util.concurrent.RateLimiter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpet.helpers.BlockRotator.flipBlock;
import static io.github.jfglzs.aca.AcaSetting.flippinToTemOfUndying;

@Mixin(BlockRotator.class)
public abstract class BlockRotator_Mixin {
    private static final RateLimiter limiter = RateLimiter.create(10);

    @Inject(
            method = "flippinEligibility",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void flippinEligibility_Inject(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (flippinToTemOfUndying && entity instanceof Player player) {
            if (player.getMainHandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = "flipBlockWithCactus",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void flipBlockWithCactus_Inject(BlockState state, Level world, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<Boolean> cir) {
        if (!flippinToTemOfUndying || !player.getMainHandItem().getItem().equals(Items.TOTEM_OF_UNDYING))
            return;
        if (!limiter.tryAcquire()) return;

        CarpetSettings.impendingFillSkipUpdates.set(true);
        boolean retval = flipBlock(state, world, player, hand, hit);
        CarpetSettings.impendingFillSkipUpdates.set(false);
        cir.setReturnValue(retval);

    }
}
