package io.jfglzs.ad_carpet_addition.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import io.jfglzs.ad_carpet_addition.utils.ConfigUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableCommandPreventer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PreventCommand
{
    private static final String COMMAND_PREVENTER = "[Command Preventer] ";

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        LiteralArgumentBuilder<ServerCommandSource> argument = literal("preventcmd")
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

    private static int addWhiteList(CommandContext<ServerCommandSource> context)
    {
        return addOrRemoveFromList(context, 1, false, COMMAND_PREVENTER + "Added %s to whitelist");
    }

    private static int addBlackList(CommandContext<ServerCommandSource> context)
    {
        return addOrRemoveFromList(context, 2, false, COMMAND_PREVENTER + "Added %s to blacklist");
    }

    private static int addPrefix(CommandContext<ServerCommandSource> context)
    {
        return addOrRemoveFromList(context, 3, false, COMMAND_PREVENTER + "Added %s to prefixlist");
    }

    private static int removeWhiteList(CommandContext<ServerCommandSource> context)
    {
        return addOrRemoveFromList(context, 1, true, COMMAND_PREVENTER + "Removed %s from whitelist");
    }

    private static int removeBlackList(CommandContext<ServerCommandSource> context)
    {
        return addOrRemoveFromList(context, 2, true, COMMAND_PREVENTER + "Removed %s from blacklist");
    }

    private static int removePrefix(CommandContext<ServerCommandSource> context)
    {
        return addOrRemoveFromList(context, 3, true, COMMAND_PREVENTER + "Removed %s from prefixlist");
    }

    private static int listPrefix(CommandContext<ServerCommandSource> context)
    {
        context.getSource().sendFeedback(() -> Text.of(COMMAND_PREVENTER + "Prefixlist:" + AcaSetting.config.CommandPreventPrefixList),false);
        return 0;
    }

    private static int listWhiteList(CommandContext<ServerCommandSource> context)
    {
        context.getSource().sendFeedback(() -> Text.of(COMMAND_PREVENTER + "WhiteList:" + AcaSetting.config.CommandPreventWhiteList),false);
        return 0;
    }

    private static int listBlackList(CommandContext<ServerCommandSource> context)
    {
        context.getSource().sendFeedback(() -> Text.of(COMMAND_PREVENTER + "BlackList:" + AcaSetting.config.CommandPreventBlackList),false);
        return 0;
    }

    private static int reload(CommandContext<ServerCommandSource> context)
    {
        ConfigUtils.loadConfigFile();
        context.getSource().sendFeedback(() -> Text.of(COMMAND_PREVENTER + "Config reloaded"),false);
        return 0;
    }

    private static int addOrRemoveFromList(CommandContext<ServerCommandSource> context, int index, boolean isRemove, String feedback)
    {
        String cmd = StringArgumentType.getString(context,"cmd");

        if (isRemove)
        {
            ConfigUtils.removeConfig(cmd, index);
            context.getSource().sendFeedback(() -> Text.of(feedback.formatted(cmd)),false);
            return 0;
        }

        ConfigUtils.addToConfig(cmd, index);
        context.getSource().sendFeedback(() -> Text.of(feedback.formatted(cmd)),false);
        return 0;
    }
}
