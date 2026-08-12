package io.github.jfglzs.aca.command;

import carpet.utils.CommandHelper;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.commands.Commands.literal;

public class HatCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = literal("hat")
                .requires((source) -> CommandHelper.canUseCommand(source, AcaSetting.enableHatCommand))
                .executes(context -> {
                    if (context.getSource().getPlayer() instanceof ServerPlayer player) {
                        Inventory inventory = player.getInventory();
                        if (inventory.getItem(39).isEmpty()) {
                            ItemStack handItem = player.getMainHandItem().copyAndClear();
                            inventory.setItem(39, handItem);
                        }
                    }
                    return Command.SINGLE_SUCCESS;
                });

        dispatcher.register(command);
    }
}
