package io.github.jfglzs.aca.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.jfglzs.aca.AcaSetting;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

import static net.minecraft.commands.Commands.literal;

public class SearchEntityCommand {
    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argument = literal("entitysearch")
                .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, AcaSetting.enableEntitySearchCommand))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context ->
                                execute(
                                        context.getSource(), EntityArgument.getEntities(context, "targets")
                                )
                        )
                );
        dispatcher.register(argument);
    }

    private static int execute(CommandSourceStack source, Collection<? extends Entity> targets) {
        targets.forEach(entity -> sendFeedback(source, entity));
        return targets.size();
    }

    private static void sendFeedback(CommandSourceStack source, Entity entity) {
        if (AcaSetting.entitySearchXaeroMapSupport) {
            source.sendSuccess(() -> Component.nullToEmpty(String.format("xaero-waypoint:%s:1:%d:%d:%d:2:false:0:External", entity.getDisplayName().getString(), (int) entity.getX(), (int) entity.getY(), (int) entity.getZ())), true);
        } else {
            source.sendSuccess(() -> Component.nullToEmpty(String.format("%s pos: \n X: %f \n Y: %f \nZ: %f \n", entity.getDisplayName().getString(), entity.getX(), entity.getY(), entity.getZ())), true);
        }
    }
}
