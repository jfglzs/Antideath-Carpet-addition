package io.jfglzs.ad_carpet_addition.mixin.Invoker.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(TeleportCommand.class)
public interface TelePortCommand_Invoker
{
    @Invoker("execute")
    static int executeInvoker(ServerCommandSource source, Collection<? extends Entity> targets, Entity destination) throws CommandSyntaxException {return 0;}

}
