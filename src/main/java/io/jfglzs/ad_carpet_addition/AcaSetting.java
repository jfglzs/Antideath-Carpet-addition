package io.jfglzs.ad_carpet_addition;


import carpet.settings.Rule;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

import static carpet.api.settings.RuleCategory.SURVIVAL;

public class AcaSetting {
    public static final String Default_FALSE = "false";
    public static Path configDirectory = FabricLoader.getInstance().getConfigDir().resolve("antideath-carpet-addition");
    public static final String ACA = "ACA";

    @Rule(
            desc = "No mining slow down",
            extra = "It can prevent the mining speed limit when you are mining blocks while having negative status effects such as Mining Fatigue. It is recommended to enable this rule when using AntiDeath Carpet Addition.",
            category = {ACA, SURVIVAL}
    )
    public static boolean noMiningSlowDown = false;
}
