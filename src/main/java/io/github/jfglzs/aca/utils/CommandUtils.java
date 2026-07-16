package io.github.jfglzs.aca.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.jfglzs.aca.ACAEntry;
import io.github.jfglzs.aca.ACAServer;
import net.minecraft.commands.CommandSourceStack;

public class CommandUtils {
    public static void runCommand(String command, CommandSourceStack source) {
        CommandDispatcher<CommandSourceStack> dispatcher = ACAServer.MINECRAFT_SERVER.getCommands().getDispatcher();
        CommandSourceStack sourceStack = source == null ? ACAServer.MINECRAFT_SERVER.createCommandSourceStack() : source;
        try {
            dispatcher.execute(dispatcher.parse(command, sourceStack));
        }
        catch (CommandSyntaxException e) {
            ACAEntry.LOGGER.error("Error while executing {}", command, e);
        }
    }
}
