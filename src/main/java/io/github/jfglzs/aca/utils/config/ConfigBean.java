package io.github.jfglzs.aca.utils.config;

import java.util.*;

public class ConfigBean {
    public final Set<String> CommandPreventWhiteList = new HashSet<>();
    public final Set<String> CommandPreventBlackList = new HashSet<>();
    public final Set<String> CommandPreventPrefixList = new HashSet<>();
    public final Map<UUID, List<UUID>> privateFakesAndOwners = new HashMap<>();
}
