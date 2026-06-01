package io.github.jfglzs.aca.mixin.carpet;

import carpet.commands.PlayerCommand;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerCommand.class)
public interface PlayerCommand_Invoker {
    @Invoker("getPlayer")
    static ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) {
        return null;
    }
}
