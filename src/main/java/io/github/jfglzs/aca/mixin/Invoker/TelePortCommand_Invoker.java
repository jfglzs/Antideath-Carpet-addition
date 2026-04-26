package io.github.jfglzs.aca.mixin.Invoker;


import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(TeleportCommand.class)
public interface TelePortCommand_Invoker {
    @Invoker("teleportToEntity")
    static int executeInvoker(CommandSourceStack source, Collection<Player> targets, Entity destination) {
        return 0;
    }
}
