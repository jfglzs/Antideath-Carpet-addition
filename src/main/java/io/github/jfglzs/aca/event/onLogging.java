package io.github.jfglzs.aca.event;

import net.minecraft.server.MinecraftServer;

public class onLogging extends Event<MinecraftServer> {
    public static final onLogging event = new onLogging();

    private onLogging() {
    }
}
