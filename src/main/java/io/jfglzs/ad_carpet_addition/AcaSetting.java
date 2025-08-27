package io.jfglzs.ad_carpet_addition;


import carpet.settings.Rule;

//import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.SURVIVAL;

public class AcaSetting {
    public static final String Default_FALSE = "false";
//    public static Path configDirectory = FabricLoader.getInstance().getConfigDir().resolve("aca");
    public static final String ACA = "ACA";

    @Rule(desc = "NoMiningSlowDown",
          extra = "NoMiningSlowDown ",
          category = {ACA, SURVIVAL})
    public static boolean noMiningSlowDown = false;

    @Rule(desc = "anvilNeverDamageByFalling",
            category = {ACA, SURVIVAL})
    public static boolean anvilNeverDamageByFalling = false;
}
