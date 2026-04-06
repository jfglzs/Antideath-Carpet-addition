package io.github.jfglzs.aca;

import carpet.api.settings.Rule;
import io.github.jfglzs.aca.utils.config.ConfigBean;
import io.github.jfglzs.aca.utils.validator.GreaterThanZeroValidator;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

import static carpet.api.settings.RuleCategory.*;

public class AcaSetting {
    public static final Path dir = FabricLoader.getInstance().getConfigDir().resolve("antideath-carpet-addition");
    public static final String FALSE = "false";
    public static final String ACA = "ACA";
    public static ConfigBean config;


    @Rule(categories = {ACA, SURVIVAL})
    public static boolean noMiningSlowDown = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean anvilNeverDamageByFalling = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean softAnvil = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean beaconLagOptimization = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean ItemFrameAlwaysStayAttach = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean entitySearchCommandEnableXaeroMapSupport = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemDespawnImmediately = false;

    @Rule(categories = {ACA, SURVIVAL}, validators = GreaterThanZeroValidator.class)
    public static int beaconRange = 0;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemNeverDespawn = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean flippinToTemOfUndying = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean endermanNeverGetAngryByPlayer = false;

    //Command:
    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false"})
    public static String enableCommandPreventer = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false"})
    public static String enableFastOpCommand = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false"})
    public static String enableEntitySearchCommand = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false"})
    public static String enableSpecTPCommand = FALSE;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableCommandPreventerWhiteList = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableCommandPreventerBlackList = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableCommandPreventerPrefix = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean commandPreventerPreventOP = true;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean fakePeaceOptimization = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean villagerOptimization = false;

    @Rule(categories = {ACA, SURVIVAL}, strict = false, options = {"0", "10", "100"}, validators = GreaterThanZeroValidator.class)
    public static int itemPickUpRange = 0;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean superSponge = false;
}

