package io.jfglzs.ad_carpet_addition;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashSet;
import java.util.Set;

public class ConfigBean
{
    public final Set<String> CommandPreventWhiteList = new HashSet<>();
    public final Set<String> CommandPreventBlackList = new HashSet<>();
    public final Set<String> CommandPreventPrefixList = new HashSet<>();
}
