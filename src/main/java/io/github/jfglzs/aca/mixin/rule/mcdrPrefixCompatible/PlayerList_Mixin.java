package io.github.jfglzs.aca.mixin.rule.mcdrPrefixCompatible;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerList.class)
public class PlayerList_Mixin {
    @ModifyArgs(
            method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;logChatMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType$Bound;Ljava/lang/String;)V")
    )
    private void broadcastChatMessage(Args args, @Local(argsOnly = true) ServerPlayer sender) {
        if (AcaSetting.mcdrPrefixCompatible) {
            ChatType.Bound bound = ChatType.bind(ChatType.CHAT, sender.level().registryAccess(), sender.getName());
            args.set(1, bound);
        }
    }
}
