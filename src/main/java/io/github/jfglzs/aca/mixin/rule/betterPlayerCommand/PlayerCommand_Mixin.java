package io.github.jfglzs.aca.mixin.rule.betterPlayerCommand;

import carpet.commands.PlayerCommand;
import carpet.helpers.EntityPlayerActionPack;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(PlayerCommand.class)
public abstract class PlayerCommand_Mixin {
    @Shadow
    private static Command<CommandSourceStack> manipulation(Consumer<EntityPlayerActionPack> action) {
        return null;
    }

    @WrapOperation(
            method = "makeDropCommand",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;executes(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
                    ordinal = 0
            )
    )
    private static ArgumentBuilder makeDropCommand(LiteralArgumentBuilder instance,
                                                   Command command,
                                                   Operation<ArgumentBuilder> original
    ) {
        ArgumentBuilder builder = original.call(instance, command);
//        boolean bl = actionName.equals("dropStack");
        return builder.then(Commands.literal("enderChest")
                             .executes(manipulation(ap -> ap.drop(-3, true))))
                       .then(Commands.literal("withEnderChest")
                             .executes(manipulation(ap -> ap.drop(-4, true)))
                       );
    }
}
