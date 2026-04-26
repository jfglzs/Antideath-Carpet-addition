package io.github.jfglzs.aca.logger;

import carpet.logging.HUDLogger;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;

public abstract class AbstractHUDLogger extends HUDLogger {
    public final String name;

    protected AbstractHUDLogger(Field acceleratorField, String logName, String def, String[] options, boolean isStrict) {
        super(acceleratorField, logName, def, options, isStrict);
        this.name = logName;
    }

    public abstract void updateHUD(MinecraftServer server);
}
