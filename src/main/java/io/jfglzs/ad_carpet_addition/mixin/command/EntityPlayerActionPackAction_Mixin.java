package io.jfglzs.ad_carpet_addition.mixin.command;

import carpet.helpers.EntityPlayerActionPack;
import io.jfglzs.ad_carpet_addition.commands.IEntityPlayerActionPackAction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerActionPack.Action.class)
public abstract class EntityPlayerActionPackAction_Mixin implements IEntityPlayerActionPackAction{
    @Unique
    private static long WorldTime = -1;

    @Override
    public void setWorldTime(long WorldTime)
    {
        this.WorldTime = WorldTime;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcarpet/helpers/EntityPlayerActionPack$ActionType;execute(Lnet/minecraft/server/network/ServerPlayerEntity;Lcarpet/helpers/EntityPlayerActionPack$Action;)Z",
                    remap = true
            ),
            remap = false
    )
    private void autoActionWhenWorldTimeIsMatched(EntityPlayerActionPack actionPack, EntityPlayerActionPack.ActionType type, CallbackInfoReturnable<Boolean> cir) {
        if (this.WorldTime > -1) {
            ServerWorld world = ((EntityPlayerActionPackAccessor) actionPack).getPlayer().getServerWorld();
            long worldTimeCurrent = world.getTimeOfDay();
            long WorldTime1 = worldTimeCurrent % 24000;
            System.out.println(WorldTime);
            System.out.println(WorldTime1);
            if (WorldTime == worldTimeCurrent % 24000) {
                System.out.println("Matched");
                EntityPlayerActionPackActionTypeAccessor typeAccessor = ((EntityPlayerActionPackActionTypeAccessor) (Object) type);
                ServerPlayerEntity player = ((EntityPlayerActionPackAccessor) actionPack).getPlayer();
                EntityPlayerActionPack.Action self = (EntityPlayerActionPack.Action) (Object) this;

                typeAccessor.invokeExecute(player, self);
                typeAccessor.invokeInactiveTick(player, self);
                System.out.println("action completed");
            }
        }
    }
}
//TIS carpet addition https://github.com/TISUnion/Carpet-TIS-Addition Fallen_Breath