package io.jfglzs.ad_carpet_addition.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class MobRiderCommand {
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> argument = literal("mobrider")
                .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, AcaSetting.mobRiderCommand))
                .executes(MobRiderCommand::addToSitOnEntitySet);
        dispatcher.register(argument);
    }

    private static int addToSitOnEntitySet(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        AcaSetting.sitOnEntitySet.add(player);
        player.sendMessage(Text.of(Messenger.c("y [Mob Rider] 右键单击即可乘坐任何生物")));
        return 0;
    }
}