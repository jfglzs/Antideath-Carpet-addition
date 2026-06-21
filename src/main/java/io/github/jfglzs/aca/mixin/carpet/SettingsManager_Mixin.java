package io.github.jfglzs.aca.mixin.carpet;

import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.ACAEntry;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public class SettingsManager_Mixin {
    @Inject(
            method = "listAllSettings",
            at = @At(
                    value = "INVOKE",
                    target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;"
            ),
            remap = false
    )
    private void listAllSettings(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        SettingsManager manager = (SettingsManager) (Object) this;
        if (CarpetServer.settingsManager == manager) {
            Messenger.m(source, "g " + ACAEntry.FANCY_NAME + " Version: " + ACAEntry.MOD_VER + "(RuleAmount: " + ACAEntry.ruleAmount + ")");
        }
    }
}
