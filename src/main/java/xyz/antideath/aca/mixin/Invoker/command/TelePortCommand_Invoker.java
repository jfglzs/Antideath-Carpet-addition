package xyz.antideath.aca.mixin.Invoker.command;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(TeleportCommand.class)
public interface TelePortCommand_Invoker {
    @Invoker("execute")
    static int executeInvoker(ServerCommandSource source, Collection<? extends Entity> targets, Entity destination) {return 0;}

}
