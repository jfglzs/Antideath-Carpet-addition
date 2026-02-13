package io.github.jfglzs.aca.mixin.rule;

import carpet.CarpetSettings;
import carpet.helpers.BlockRotator;
import com.google.common.util.concurrent.RateLimiter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpet.helpers.BlockRotator.flipBlock;
import static io.github.jfglzs.aca.AcaSetting.flippinToTemOfUndying;

@Mixin(BlockRotator.class)
public class BlockRotator_Mixin {
    private static RateLimiter limiter = RateLimiter.create(10);

    @Inject(
            method = "flippinEligibility",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void flippinEligibilityInject(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (flippinToTemOfUndying && entity instanceof PlayerEntity player) {
            if (player.getMainHandStack().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = "flipBlockWithCactus",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void flipBlockWithCactus(BlockState state, World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<Boolean> cir) {
        if (!player.getAbilities().allowModifyWorld || !flippinToTemOfUndying || !player.getMainHandStack().getItem().equals(Items.TOTEM_OF_UNDYING)) return;
        if (!limiter.tryAcquire()) return;

        CarpetSettings.impendingFillSkipUpdates.set(true);
        boolean retval = flipBlock(state, world, player, hand, hit);
        CarpetSettings.impendingFillSkipUpdates.set(false);
        cir.setReturnValue(retval);

    }
}
