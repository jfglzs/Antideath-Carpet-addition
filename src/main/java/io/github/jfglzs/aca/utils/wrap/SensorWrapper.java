package io.github.jfglzs.aca.utils.wrap;

import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.utils.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class SensorWrapper<E extends LivingEntity> extends Sensor<E> {
    private Sensor<E> sensor;

    public static <E extends LivingEntity> Sensor<E> wrap(Sensor<E> sensor) {
        return new SensorWrapper<>(sensor);
    }

    private SensorWrapper(Sensor<E> sensor) {
        this.sensor = sensor;
    }

    @Override
    protected void doTick(ServerLevel level, E livingEntity) {
        if (AcaSetting.villagerOptimization && EntityUtils.canDisableAI(livingEntity)) {
            return;
        }
        this.sensor.tick(level, livingEntity);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return this.sensor.requires();
    }
}
