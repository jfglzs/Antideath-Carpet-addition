package io.jfglzs.ad_carpet_addition;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import carpet.api.settings.Rule;
import com.google.common.util.concurrent.RateLimiter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;

import static carpet.api.settings.RuleCategory.*;

public class AcaSetting {
    public static ConfigBean config;
    public static Set<PlayerEntity> sitOnEntitySet = new HashSet<>();
    public static Set<PlayerEntity> getDownPlayerSet = new HashSet<>();
    public static Map<PlayerEntity, RateLimiter> sneakCooldownMap = new HashMap<>();
    public static final Path configDirectory = FabricLoader.getInstance().getConfigDir().resolve("antideath-carpet-addition");
    public static final String FALSE = "false";
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
    public static boolean entitySearchCommandEnableXaeroMapSupport = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemDespawnImmediately = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemNeverDespawn = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean flippinToTemOfUndying = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean endermanNeverGetAngryByPlayer = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean doubleClickShiftGetDown = false;

    //Command:
    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false" })
    public static String enableCommandPreventer = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false" })
    public static String enableFastOpCommand = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false" })
    public static String enableEntitySearchCommand = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false" })
    public static String enableSpecTPCommand = FALSE;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false" })
    public static boolean mobRiderCommand = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableCommandPreventerWhiteList = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableCommandPreventerBlackList = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean enableCommandPreventerPrefix = false;

    @Rule(categories = {ACA, COMMAND})
    public static boolean commandPreventerPreventOP = true;
}

