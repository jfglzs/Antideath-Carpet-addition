package io.jfglzs.ad_carpet_addition;


import carpet.settings.Rule;

//import carpet.api.settings.Rule;
import static carpet.api.settings.RuleCategory.*;


public class AcaSetting {
    public static final String Default_FALSE = "false";
    public static final String ACA = "ACA";

    @SuppressWarnings("removal")
    @Rule(desc = "NoMiningSlowDown",
          category = {ACA, SURVIVAL})
    public static boolean noMiningSlowDown = false;

    @SuppressWarnings("removal")
    @Rule(desc = "anvilNeverDamageByFalling",
            category = {ACA, SURVIVAL})
    public static boolean anvilNeverDamageByFalling = false;

    @SuppressWarnings("removal")
    @Rule(desc = "beaconLagOptimization",
            category = {ACA, OPTIMIZATION})
    public static boolean beaconLagOptimization = false;

    @SuppressWarnings("removal")
    @Rule(desc = "ItemFrameAlwaysStayAttach",
            category = {ACA, OPTIMIZATION})
    public static boolean ItemFrameAlwaysStayAttach = false;

    @SuppressWarnings("removal")
    @Rule(desc = "enableFastOpCommand",
            category = {ACA, COMMAND})
    public static boolean enableFastOpCommand = false;

    @SuppressWarnings("removal")
    @Rule(desc = "itemDespawnImmediately",
            extra = "Warning!!!!!!: May cause something important items to be lost.",
            category = {ACA, SURVIVAL})
    public static boolean itemDespawnImmediately = false;

    @SuppressWarnings("removal")
    @Rule(desc = "itemNeverDespawn",
            extra = "item Never Despawn",
            category = {ACA, SURVIVAL})
    public static boolean itemNeverDespawn = false;

}

