package io.jfglzs.ad_carpet_addition.mixin.command;

import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerActionPack.class)
public interface EntityPlayerActionPackAccessor
{
    @Accessor
    ServerPlayerEntity getPlayer();
}
