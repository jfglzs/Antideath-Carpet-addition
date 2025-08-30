package io.jfglzs.ad_carpet_addition.mixin.command;

import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityPlayerActionPack.ActionType.class)
public interface EntityPlayerActionPackActionTypeAccessor
{
    @Invoker
    boolean invokeExecute(ServerPlayerEntity player, EntityPlayerActionPack.Action action);

    @Invoker
    void invokeInactiveTick(ServerPlayerEntity player, EntityPlayerActionPack.Action action);
}
//TIS carpet addition https://github.com/TISUnion/Carpet-TIS-Addition Fallen_Breath