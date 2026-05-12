package io.github.jfglzs.aca.utils;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.VillagerAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.Vec3;

public class EntityUtils {
    public static Vec3 getEntityPos(Entity entity) {
        return entity.position();
    }

    public static boolean canDisableAI(Object entity) {
        return entity instanceof Villager villager && ((VillagerAccessor) villager).aca$canDisableAI();
    }
}
