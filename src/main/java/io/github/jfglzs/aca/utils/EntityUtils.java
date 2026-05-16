package io.github.jfglzs.aca.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityUtils {
    public static Vec3 getEntityPos(Entity entity) {
        return entity.position();
    }

}
