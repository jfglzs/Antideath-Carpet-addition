package io.jfglzs.ad_carpet_addition.commands;

import carpet.helpers.EntityPlayerActionPack;

public class PlayerActionPackHelper
{
    public static EntityPlayerActionPack.Action worldTime(int WorldTime)
    {
        EntityPlayerActionPack.Action action = EntityPlayerActionPack.Action.once();
        ((IEntityPlayerActionPackAction)action).setWorldTime(WorldTime);
        return action;
    }
}

//TIS carpet addition https://github.com/TISUnion/Carpet-TIS-Addition Fallen_Breath