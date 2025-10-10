package io.jfglzs.ad_carpet_addition;

//import carpet.settings.Rule;
import carpet.api.settings.Rule;
import static carpet.api.settings.RuleCategory.*;

public class AcaSetting
{
    public static final String ACA = "ACA";

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean noMiningSlowDown = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean anvilNeverDamageByFalling = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean beaconLagOptimization = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean ItemFrameAlwaysStayAttach = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableFastOpCommand = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableEntitySearchCommand = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableSpecTPCommand = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean entitySearchCommandEnableXaeroMapSupport = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemDespawnImmediately = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemNeverDespawn = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean flippinToTemOfUndying = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean endermanNeverGetAngryByPlayer = false;

}

