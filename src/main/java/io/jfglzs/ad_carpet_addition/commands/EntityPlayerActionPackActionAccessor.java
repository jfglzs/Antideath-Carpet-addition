package io.jfglzs.ad_carpet_addition.commands;

import carpet.helpers.EntityPlayerActionPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityPlayerActionPack.Action.class)
public interface EntityPlayerActionPackActionAccessor
{
    @Invoker("<init>")
    static EntityPlayerActionPack.Action invokeConstructor(
            int limit, int interval, int offset , boolean continuous
    )
    {
        throw new RuntimeException();
    }
}


//TIS carpet addition https://github.com/TISUnion/Carpet-TIS-Addition Fallen_Breath