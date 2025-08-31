package io.jfglzs.ad_carpet_addition.mixin.Invoker.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.OpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(OpCommand.class)
public interface OpCommandInvoker {
    @Invoker("op")
    static int invokeOp(ServerCommandSource source, Collection<GameProfile> targets) throws CommandSyntaxException {
        return 0;
    }
}