package io.github.jfglzs.aca.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import net.minecraft.commands.CommandSourceStack;

import static io.github.jfglzs.aca.AcaSetting.enableCommandPreventer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;


public class PreventCommand {
    private static final String COMMAND_PREVENTER = "g [Command Preventer] ";

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argument = literal("preventcmd")
                .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableCommandPreventer))
                .then(literal("whitelist")
                        .then(literal("add")
                                .then(argument("cmd", StringArgumentType.word())
                                        .executes(PreventCommand::addWhiteList)
                                )
                        )
                        .then(literal("remove")
                                .then(argument("cmd", StringArgumentType.word())
                                        .executes(PreventCommand::removeWhiteList)
                                )
                        )
                        .then(literal("list")
                                .executes(PreventCommand::listWhiteList)
                        )
                )
                .then(literal("blacklist")
                        .then(literal("add")
                                .then(argument("cmd", StringArgumentType.word())
                                        .executes(PreventCommand::addBlackList)
                                )
                        )
                        .then(literal("remove")
                                .then(argument("cmd", StringArgumentType.word())
                                        .executes(PreventCommand::removeBlackList)
                                )
                        )
                        .then(literal("list")
                                .executes(PreventCommand::listBlackList)
                        )
                )
                .then(literal("prefix")
                        .then(literal("add")
                                .then(argument("cmd", StringArgumentType.word())
                                        .executes(PreventCommand::addPrefix)
                                )
                        )
                        .then(literal("remove")
                                .then(argument("cmd", StringArgumentType.word())
                                        .executes(PreventCommand::removePrefix)
                                )
                        )
                        .then(literal("list")
                                .executes(PreventCommand::listPrefix)
                        )
                )
                .then(literal("reload")
                        .executes(PreventCommand::reload)
                );

        dispatcher.register(argument);
    }

    private static int addWhiteList(CommandContext<CommandSourceStack> context) {
        return addOrRemoveFromList(context, 1, false, "Added %s to whitelist");
    }

    private static int addBlackList(CommandContext<CommandSourceStack> context) {
        return addOrRemoveFromList(context, 2, false, "Added %s to blacklist");
    }

    private static int addPrefix(CommandContext<CommandSourceStack> context) {
        return addOrRemoveFromList(context, 3, false, "Added %s to prefixlist");
    }

    private static int removeWhiteList(CommandContext<CommandSourceStack> context) {
        return addOrRemoveFromList(context, 1, true, "Removed %s from whitelist");
    }

    private static int removeBlackList(CommandContext<CommandSourceStack> context) {
        return addOrRemoveFromList(context, 2, true, "Removed %s from blacklist");
    }

    private static int removePrefix(CommandContext<CommandSourceStack> context) {
        return addOrRemoveFromList(context, 3, true, "Removed %s from prefixlist");
    }

    private static int listPrefix(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Messenger.c(COMMAND_PREVENTER + "Prefixlist: " + AcaSetting.config.CommandPreventPrefixList), true);
        return 0;
    }

    private static int listWhiteList(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Messenger.c(COMMAND_PREVENTER + "WhiteList: " + AcaSetting.config.CommandPreventWhiteList), true);
        return 0;
    }

    private static int listBlackList(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Messenger.c(COMMAND_PREVENTER + "BlackList: " + AcaSetting.config.CommandPreventBlackList), true);
        return 0;
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        ConfigUtils.loadConfigFile();
        context.getSource().sendSuccess(() -> Messenger.c(COMMAND_PREVENTER + "Config reloaded"), true);
        return 0;
    }

    private static int addOrRemoveFromList(CommandContext<CommandSourceStack> context, int index, boolean isRemove, String feedback) {
        String cmd = StringArgumentType.getString(context, "cmd");
        if (isRemove) {
            ConfigUtils.removeConfig(cmd, index);
            context.getSource().sendSuccess(() -> Messenger.c(COMMAND_PREVENTER + feedback.formatted(cmd)), true);
        } else {
            ConfigUtils.addToConfig(cmd, index);
            context.getSource().sendSuccess(() -> Messenger.c(COMMAND_PREVENTER + feedback.formatted(cmd)), true);
        }
        return 0;
    }
}
