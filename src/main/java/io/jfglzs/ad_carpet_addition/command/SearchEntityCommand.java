package io.jfglzs.ad_carpet_addition.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableEntitySearchCommand;
import static io.jfglzs.ad_carpet_addition.AcaSetting.entitySearchCommandEnableXaeroMapSupport;

public class SearchEntityCommand
{
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("entitysearch")
                      .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableEntitySearchCommand))
                        .then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> execute(context.getSource(), EntityArgumentType.getEntities(context, "targets")))));
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets)
    {
        for(Entity entity : targets) sendFeedback(source, entity);
        return targets.size();
    }

    private static void sendFeedback(ServerCommandSource source, Entity entity)
    {
        if (entitySearchCommandEnableXaeroMapSupport)
        {
            source.sendFeedback(() -> Text.of(String.format("xaero-waypoint:%s:1:%d:%d:%d:2:false:0:External", getEntityName(entity), (int) entity.getX(), (int) entity.getY(), (int) entity.getZ())),true);
        }
        else
        {
            source.sendFeedback(() -> Text.of(String.format("Entity name: %s Pos: X: %f Y: %f Z: %f" , getEntityName(entity) , entity.getX(),  entity.getY(), entity.getZ())), true);
        }
    }

    private static String getEntityName(Entity entity)
    {
        String entityInfo = String.valueOf(entity);
        return entityInfo.split("Entity")[0];
    }
}
