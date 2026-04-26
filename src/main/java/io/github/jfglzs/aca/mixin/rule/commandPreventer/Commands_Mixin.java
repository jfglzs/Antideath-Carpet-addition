package io.github.jfglzs.aca.mixin.rule.commandPreventer;

import carpet.utils.Messenger;
import com.mojang.brigadier.ParseResults;
import io.github.jfglzs.aca.ACAEntry;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.jfglzs.aca.AcaSetting.commandPreventerPreventOP;

@Mixin(Commands.class)
public abstract class Commands_Mixin {
    @Inject(
            method = "performCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/commands/Commands;executeCommandInContext(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/function/Consumer;)V"
            ),
            cancellable = true
    )
    public void execute_Inject(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
        if (ConfigUtils.toBoolean(AcaSetting.enableCommandPreventer)) {
            if (AcaSetting.enableCommandPreventerPrefix && AcaSetting.config.CommandPreventPrefixList.stream().anyMatch(command::startsWith))
                preventCommand(ci, command, parseResults);
            if (AcaSetting.enableCommandPreventerWhiteList && AcaSetting.config.CommandPreventWhiteList.contains(command))
                preventCommand(ci, command, parseResults);
            else if (AcaSetting.enableCommandPreventerBlackList && AcaSetting.config.CommandPreventWhiteList.contains(command))
                preventCommand(ci, command, parseResults);
        }
    }

    @Unique
    private void preventCommand(CallbackInfo ci, String command, ParseResults<CommandSourceStack> results) {
        ServerPlayer player = results.getContext().getSource().getPlayer();
        if (!commandPreventerPreventOP && player != null && player.getServer().getPlayer().isOperator(player.getGameProfile()))
            return;
        results.getContext().getSource().send(
                Messenger.c("r [Command Preventer] Command: %s had prevented ".formatted(command)), true
        );
        ACAEntry.LOGGER.info("[Command Preventer] Prevented Command: {}", command);
        ci.cancel();
    }
}
