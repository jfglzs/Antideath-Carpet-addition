package io.github.jfglzs.aca.utils;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.IVillagerAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityUtils {
    public static Vec3 getEntityPos(Entity entity) {
        return entity.position();
    }

    public static boolean shouldSkip(Entity entity) {
        return AcaSetting.villagerOptimization
                && entity instanceof IVillagerAccessor villager
                && villager.aca$canDisableAI();
    }
}
