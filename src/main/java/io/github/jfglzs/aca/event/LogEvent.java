package io.github.jfglzs.aca.event;

import net.minecraft.server.MinecraftServer;

public class LogEvent extends Event<MinecraftServer> {
    public static final LogEvent event = new LogEvent();

    private LogEvent() {
    }
}
