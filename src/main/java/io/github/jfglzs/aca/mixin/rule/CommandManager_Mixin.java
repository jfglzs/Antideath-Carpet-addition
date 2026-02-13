package io.github.jfglzs.aca.mixin.rule;

import carpet.utils.Messenger;
import com.mojang.brigadier.ParseResults;
import io.github.jfglzs.aca.AcaExtension;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.ConfigUtils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.aca.AcaSetting.commandPreventerPreventOP;

@Mixin(CommandManager.class)
public class CommandManager_Mixin {
    @Inject(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/command/CommandManager;callWithContext(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/function/Consumer;)V"
            ),
            cancellable = true
    )
    public void executeInject(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfo ci) {
        if (ConfigUtils.toBoolean(AcaSetting.enableCommandPreventer)) {
            if (!(AcaSetting.enableCommandPreventerPrefix && AcaSetting.config.CommandPreventPrefixList.stream().anyMatch(command::startsWith))) return;
            if (!(AcaSetting.enableCommandPreventerWhiteList && AcaSetting.config.CommandPreventWhiteList.contains(command))) return;
            else if (!(AcaSetting.enableCommandPreventerBlackList && AcaSetting.config.CommandPreventWhiteList.contains(command))) return;
            preventCommand(ci, command, parseResults);
        }
    }

    @Unique
    private void preventCommand(CallbackInfo ci, String command, ParseResults<ServerCommandSource> parseResults) {
        ServerPlayerEntity p = parseResults.getContext().getSource().getPlayer();
        if (!commandPreventerPreventOP && p != null && p.getServer().getPlayerManager().isOperator(p.getGameProfile())) return;
        parseResults.getContext().getSource().sendFeedback(() -> Messenger.c("r [Command Preventer] Command: %s had prevented by Command Preventer".formatted(command)) ,true);
        AcaExtension.LOGGER.info("[Command Preventer] Prevented Command: {}", command);
        ci.cancel();
    }
}
