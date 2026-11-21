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
    public static ConfigBean config = new ConfigBean();

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean beaconLagOptimization = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean ItemFrameAlwaysStayAttach = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemDespawnImmediately = false;

    @Rule(categories = {ACA, SURVIVAL}, validators = GreaterThanZeroValidator.class)
    public static int beaconRange = 0;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean itemNeverDespawn = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean flippinToTemOfUndying = false;

    @Rule(categories = {ACA, COMMAND}, options = {"1", "2", "3", "4", "true", "false"})
    public static String enablePlayerLockCommand = FALSE;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean villagerOptimization = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean villagerOptimizationEjectSupport = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean boatOptimization = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean mcdrPrefixCompatible = false;

    @Rule(categories = {ACA, SURVIVAL})
    public static boolean neverDropLeashBySpectator = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean multiThreadWorldSave = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean disableSystemReportRecordHardware = false;

    @Rule(categories = {ACA, OPTIMIZATION})
    public static boolean justInTimeLoggers = false;

    @Rule(categories = {ACA, OPTIMIZATION}, options = {"1", "50", "500", "1000"},strict = false)
    public static int justInTimeLoggersLeastUpdateFreq = 1000;
}

