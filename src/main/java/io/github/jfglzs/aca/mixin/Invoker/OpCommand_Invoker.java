package io.github.jfglzs.aca.mixin.Invoker;

import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.OpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(OpCommand.class)
public interface OpCommand_Invoker {
    @Invoker("opPlayers")
    static int invokeOp(CommandSourceStack source, Collection<GameProfile> targets) {
        return 0;
    }
}