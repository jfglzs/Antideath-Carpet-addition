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
    @Inject(method = "execute",at = @At("HEAD"), cancellable = true)
    public void executeInject(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfo ci)
    {
        String message = "Prevented Command: {}";

        if (AcaSetting.enableCommandPreventer)
        {
            if (AcaSetting.enableCommandPreventerPrefix)
            {
                for (String prefix : AcaSetting.config.CommandPreventPrefixList)
                {
                    if (command.startsWith(prefix))
                    {
                        AcaExtension.LOGGER.info(message, command);
                        ci.cancel();
                    }
                }
            }

            if (AcaSetting.enableCommandPreventerWhiteList)
            {
                for (String prefix : AcaSetting.config.CommandPreventWhiteList)
                {
                    AcaExtension.LOGGER.info(message, command);
                    if (!command.equals(prefix)) ci.cancel();
                }
            }
            else if (AcaSetting.enableCommandPreventerBlackList)
            {
                for (String prefix : AcaSetting.config.CommandPreventBlackList)
                {
                    AcaExtension.LOGGER.info(message, command);
                    if (command.equals(prefix)) ci.cancel();
                }
            }
        }
    }
}
