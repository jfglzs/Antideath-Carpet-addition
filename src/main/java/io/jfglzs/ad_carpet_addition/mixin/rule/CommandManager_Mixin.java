package io.jfglzs.ad_carpet_addition.mixin.rule;

import carpet.utils.Messenger;
import com.mojang.brigadier.ParseResults;
import io.jfglzs.ad_carpet_addition.AcaExtension;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.jfglzs.ad_carpet_addition.AcaSetting.CommandPreventerPreventOP;

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
        if (AcaSetting.enableCommandPreventer)
        {
            if (AcaSetting.enableCommandPreventerPrefix && AcaSetting.config.CommandPreventPrefixList.stream().anyMatch(command::startsWith)) {preventCommand(ci, command, parseResults);}
            if (AcaSetting.enableCommandPreventerWhiteList && AcaSetting.config.CommandPreventWhiteList.stream().noneMatch(command::equals)) {preventCommand(ci, command, parseResults);}
            else if (AcaSetting.enableCommandPreventerBlackList && AcaSetting.config.CommandPreventWhiteList.stream().anyMatch(command::equals)) {preventCommand(ci, command, parseResults);}
        }
    }

    @Unique
    private void preventCommand(CallbackInfo ci, String command, ParseResults<ServerCommandSource> parseResults)
    {
        ServerPlayerEntity p = parseResults.getContext().getSource().getPlayer();
        if (!CommandPreventerPreventOP && p != null && p.getServer().getPlayerManager().isOperator(p.getGameProfile())) return;
        parseResults.getContext().getSource().sendFeedback(() -> Messenger.c("r Cant execute Command: %s because %s is in Command Preventer List".formatted(command, command)) ,true);
        AcaExtension.LOGGER.info("[Command Preventer] Prevented Command: {}", command);
        ci.cancel();
    }
}
