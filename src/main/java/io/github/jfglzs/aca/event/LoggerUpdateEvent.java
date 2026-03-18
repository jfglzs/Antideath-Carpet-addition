package io.github.jfglzs.aca.event;

import net.minecraft.server.MinecraftServer;

public class LoggerUpdateEvent extends Event<MinecraftServer> {
    public static final LoggerUpdateEvent event = new LoggerUpdateEvent();

    private LoggerUpdateEvent() {
    }
}
