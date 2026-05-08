package io.github.jfglzs.aca.mixin.rule.betterPlayerCommand;

import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerActionPack.class)
public abstract class EntityPlayerActionPack_Mixin {
    @Shadow
    @Final
    private ServerPlayer player;

    @Shadow
    public abstract void drop(int selectedSlot, boolean dropAll);

    @Inject(
            method = "drop",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void drop_Inject(int selectedSlot, boolean dropAll, CallbackInfo ci) {
        if (selectedSlot == -3 || selectedSlot == -4) {
            dropAllEnderChestItems();
            if (selectedSlot == -4) {drop(-2, dropAll);}
            ci.cancel();
        }
    }

    @Unique
    private void dropAllEnderChestItems() {
        var inventory = this.player.getEnderChestInventory();
        for (int i = inventory.getContainerSize(); i >= 0; --i) {
            dropItemFromEnderChest(i);
        }
    }

    @Unique
    private void dropItemFromEnderChest(int slot) {
        var inventory = this.player.getEnderChestInventory();
        if (!inventory.getItem(slot).isEmpty()) {

            this.player.drop(inventory.removeItem(slot, inventory.getItem(slot).getCount()), false, false);
        }
    }
}
