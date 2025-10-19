package io.jfglzs.ad_carpet_addition.command;

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

    private static int addPrefix(CommandContext<ServerCommandSource> context)
    {
        String cmd = StringArgumentType.getString(context,"cmd");
        ConfigUtils.addToConfig(cmd,3);
        context.getSource().sendFeedback(() -> Text.of("added " + cmd + " to prefixlist"),false);
        return 0;
    }

    private static int removePrefix(CommandContext<ServerCommandSource> context)
    {
        String cmd = StringArgumentType.getString(context,"cmd");
        ConfigUtils.removeConfig(cmd,3);
        context.getSource().sendFeedback(() -> Text.of("removed " + cmd + " from prefixlist"),false);
        return 0;
    }

    private static int listPrefix(CommandContext<ServerCommandSource> context)
    {
        context.getSource().sendFeedback(() -> Text.of(String.valueOf(AcaSetting.config.CommandPreventPrefixList)),false);
        return 0;
    }

    private static int addBlackList(CommandContext<ServerCommandSource> context)
    {
        String cmd = StringArgumentType.getString(context,"cmd");
        ConfigUtils.addToConfig(cmd,2);
        context.getSource().sendFeedback(() -> Text.of("added " + cmd+ " to blacklist"),false);
        return 0;
    }

    private static int removeBlackList(CommandContext<ServerCommandSource> context)
    {
        String cmd = StringArgumentType.getString(context,"cmd");
        ConfigUtils.removeConfig(cmd,2);
        context.getSource().sendFeedback(() -> Text.of("removed " + cmd + " from whitelist"),false);
        return 0;
    }

    private static int listBlackList(CommandContext<ServerCommandSource> context)
    {
        context.getSource().sendFeedback(() -> Text.of(String.valueOf(AcaSetting.config.CommandPreventBlackList)),false);
        return 0;
    }

    private static int addWhiteList(CommandContext<ServerCommandSource> context)
    {
        String cmd = StringArgumentType.getString(context,"cmd");
        ConfigUtils.addToConfig(cmd,1);
        context.getSource().sendFeedback(() -> Text.of("added " + cmd + " to whitelist"),false);
        return 0;
    }

    private static int removeWhiteList(CommandContext<ServerCommandSource> context)
    {
        String cmd = StringArgumentType.getString(context,"cmd");
        ConfigUtils.removeConfig(cmd,1);
        context.getSource().sendFeedback(() -> Text.of("removed " + cmd + " from whitelist"),false);
        return 0;
    }

    private static int listWhiteList(CommandContext<ServerCommandSource> context)
    {
        context.getSource().sendFeedback(() -> Text.of(String.valueOf(AcaSetting.config.CommandPreventWhiteList)),false);
        return 0;
    }

    private static int reload(CommandContext<ServerCommandSource> context)
    {
        ConfigUtils.loadConfigFile();
        context.getSource().sendFeedback(() -> Text.of("Config reloaded"),false);
        return 0;
    }
}
