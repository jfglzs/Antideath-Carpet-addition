package io.jfglzs.ad_carpet_addition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class RuleTranslator
{
    public static Map<String, String> getTranslationFromResourcePath(String lang)
    {
        InputStream langFile = RuleTranslator.class.getClassLoader().getResourceAsStream("assets/antideath-carpet-addition/lang/%s.json".formatted(lang));
        if (langFile == null)
        {
            return Collections.emptyMap();
        }
        String jsonData;
        try
        {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            return Collections.emptyMap();
        }
        Gson gson = new GsonBuilder().setLenient().create();
        return gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
    }
}