package io.jfglzs.ad_carpet_addition.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.jfglzs.ad_carpet_addition.AcaSetting;
import io.jfglzs.ad_carpet_addition.ConfigBean;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static io.jfglzs.ad_carpet_addition.AcaExtension.LOGGER;
import static io.jfglzs.ad_carpet_addition.AcaSetting.configDirectory;

public class ConfigUtils
{
    public static final String File_NAME = "antideath-carpet-addition.json";
    public static final Gson gson = new Gson();

    public static String load()
    {
        try
        {
            return Files.readString(configDirectory.resolve(File_NAME), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            LOGGER.error(String.valueOf(e));
            return "Failed";
        }
    }

    public static boolean create()
    {
        try
        {
            Files.createFile(configDirectory.resolve(File_NAME));
            return true;
        }
        catch (IOException e)
        {
            LOGGER.error(String.valueOf(e));
            return false;
        }
    }

    public static boolean init()
    {
        File file = configDirectory.toFile();
        if (!file.exists())
        {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean exists()
    {
        return Files.exists(configDirectory.resolve(File_NAME));
    }

    public static boolean write(String content)
    {
        try
        {
            Files.write(configDirectory.resolve(File_NAME), content.getBytes());
        }
        catch (IOException e)
        {
            LOGGER.error(String.valueOf(e));
            return false;
        }
        return true;
    }

    public static void loadConfigFile()
    {
        if (!ConfigUtils.exists())
        {
            if (!ConfigUtils.create())
            {
                LOGGER.error("Fail to create config file");
                return;
            }
            if (!ConfigUtils.write("""
                    {
                      "CommandPreventWhiteList": [],
                      "CommandPreventBlackList": [],
                      "CommandPreventPrefixList": []
                    }"""))
            {
                LOGGER.error("Fail to write config file");
                return;
            }
        }

        try
        {
            String content = ConfigUtils.load();
            if (content.equals("Failed")) return;
            AcaSetting.config = gson.fromJson(content, ConfigBean.class);
        }
        catch (JsonSyntaxException e)
        {
            LOGGER.error(String.valueOf(e));
        }
        finally
        {
            if (AcaSetting.config == null)
            {
                AcaSetting.config = new ConfigBean();
            }
        }
    }

    public static void saveConfig()
    {
        if (!ConfigUtils.write(gson.toJson(AcaSetting.config)))
        {
            LOGGER.error("Failed to save config");
        }
    }

    public static void addToConfig(String context, int index)
    {
        switch (index)
        {
            case 1 -> AcaSetting.config.CommandPreventWhiteList.add(context);
            case 2 -> AcaSetting.config.CommandPreventBlackList.add(context);
            case 3 -> AcaSetting.config.CommandPreventPrefixList.add(context);
            default -> LOGGER.warn("Invalid index");
        }
    }

    public static void removeConfig(String context, int index)
    {
        switch (index)
        {
            case 1 -> AcaSetting.config.CommandPreventWhiteList.remove(context);
            case 2 -> AcaSetting.config.CommandPreventBlackList.remove(context);
            case 3 -> AcaSetting.config.CommandPreventPrefixList.remove(context);
            default -> LOGGER.warn("Invalid index");
        }
    }
}
