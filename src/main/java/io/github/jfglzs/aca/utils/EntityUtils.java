package io.github.jfglzs.aca.utils;

import io.github.jfglzs.aca.accessors.IVillagerAccessor;
import io.github.jfglzs.aca.utils.wrap.Cache;
import io.netty.util.collection.LongObjectHashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityUtils {
    public static final LongObjectHashMap<Cache<Direction>> ATTACHABLES = new LongObjectHashMap<>();

    public static Vec3 getEntityPos(Entity entity) {
        return entity.position();
    }

    public static boolean shouldSkip(Entity entity) {
        return entity instanceof IVillagerAccessor villager && villager.aca$canDisableAI();
    }

    public static long pack(int x, int y, int z) {
        long lx = x & 0x7FFFFFFL;        // 27 bit
        long ly = (y + 64) & 0x1FFL;     // 9 bit (-64 - 320)
        long lz = z & 0x7FFFFFFL;        // 27 bit

        return lx | (ly << 27) | (lz << 36);
    }
}
