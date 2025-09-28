package io.jfglzs.ad_carpet_addition.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableEntitySearchCommand;
import static io.jfglzs.ad_carpet_addition.AcaSetting.enableFastOpCommand;
import static net.minecraft.server.command.CommandManager.literal;

public class SearchEntityCommand {

    static List<Entity> entityList = new ArrayList<>();

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
//        LiteralArgumentBuilder<ServerCommandSource> argument = literal("entity_search")
        dispatcher.register(CommandManager.literal("entitysearch")
                    .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableEntitySearchCommand))
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                            .executes((context) -> execute((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets")))));
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets) {
        List<Entity> nearby = new ArrayList<>();

        for(Entity entity : targets) {
            ServerPlayerEntity playerEntity = source.getPlayer();
            if (playerEntity == null) return -1;
            Box box = playerEntity.getBoundingBox().expand(160);
            sendFeedback(source, entity);
        }

        entityList.clear();
        return targets.size();
    }

    private static void sendFeedback(ServerCommandSource source, Entity entity){
        List<Float> entityXYZ = new ArrayList<>();
            entityXYZ.add((float) entity.getX());
            entityXYZ.add((float) entity.getY());
            entityXYZ.add((float) entity.getZ());
            source.sendFeedback(() -> Text.of(String.format("Entity name: %s pos: %s" , entity.getName() , entityXYZ)), true);
    }
}
