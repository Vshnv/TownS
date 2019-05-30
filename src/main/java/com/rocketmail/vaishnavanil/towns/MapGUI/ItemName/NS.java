package com.rocketmail.vaishnavanil.towns.MapGUI.ItemName;

import org.bukkit.ChatColor;

public interface NS {
    String getModifications(String input);

    default String c(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}
