package com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore;

import org.bukkit.ChatColor;

import java.util.List;

public interface LS {
    List<String> getModifications(List<String> msg);
    default String c(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}