package com.rocketmail.vaishnavanil.towns.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TitleSender {
    INSTANCE;

    public void sendTitle(Player player, String title, String subTitle){
        if(title != null)
            title = ChatColor.translateAlternateColorCodes('&', title);
        if(subTitle != null)
            subTitle = ChatColor.translateAlternateColorCodes('&', subTitle);
        player.sendTitle(title, subTitle, 10, 40, 20);
    }

    public void sendTitle(Player player, String title){
        if(title != null)
            title = ChatColor.translateAlternateColorCodes('&', title);
        player.sendTitle(title, null, 10, 40, 20);
    }

}
