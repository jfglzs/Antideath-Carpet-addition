package io.jfglzs.ad_carpet_addition.mixin.rule;

import com.mojang.brigadier.ParseResults;
import io.jfglzs.ad_carpet_addition.AcaExtension;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManager_Mixin
{
    @Inject(
            method = "execute",
            at = @At("HEAD"),
            cancellable = true
    )
    public void executeInject(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfo ci)
    {
        String message = "Prevented Command: {}";

        if (AcaSetting.enableCommandPreventer)
        {
            if (AcaSetting.enableCommandPreventerPrefix && AcaSetting.config.CommandPreventPrefixList.stream().anyMatch(command::startsWith))
            {
                AcaExtension.LOGGER.info(message, command);
                ci.cancel();
            }

            if (AcaSetting.enableCommandPreventerWhiteList && AcaSetting.config.CommandPreventWhiteList.stream().anyMatch(command::equals))
            {
                ci.cancel();
                AcaExtension.LOGGER.info(message, command);
            }
            else if (AcaSetting.enableCommandPreventerBlackList && AcaSetting.config.CommandPreventWhiteList.stream().anyMatch(command::equals))
            {
                AcaExtension.LOGGER.info(message, command);
                ci.cancel();
            }
        }
    }
}
