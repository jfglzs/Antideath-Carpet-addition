package io.github.jfglzs.aca.logger.entities;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import com.google.common.collect.Iterables;
import io.github.jfglzs.aca.event.onLogging;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityLogger extends AbstractHUDLogger {
    public static final EntityLogger INSTANCE;

    static {
        try {
            INSTANCE = new EntityLogger(Loggers.class.getField("__entities"), "entities", " ", null, true);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected EntityLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        onLogging.event.register(this::updateHUD);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__entities) {
            StringBuilder builder = new StringBuilder();
            builder.append("g ");
            for (ServerLevel level : server.getAllLevels()) {
                int size = Iterables.size(level.getAllEntities());
                String levelName = getDimension(level.dimension());
                builder.append(levelName).append(": ").append(size).append(" ");
            }

            LoggerRegistry.getLogger(this.NAME).log(() -> new Component[] {Messenger.c(builder.toString())});
        }
    }

    private String getDimension(ResourceKey<Level> key) {
        return switch (key.identifier().toString()) {
            case "minecraft:overworld" -> "OW";
            case "minecraft:the_nether" -> "NE";
            case "minecraft:the_end" -> "EN";
            default -> key.identifier().getPath();
        };
    }
}

