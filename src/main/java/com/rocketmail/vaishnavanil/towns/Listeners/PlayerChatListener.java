package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Towns.TownPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){

        Player player = event.getPlayer();
        TownPlayer tplayer = TownS.g().getTownPlayer(player);
        if(tplayer.townChatActive()){

            if(!TownS.g().hasTown(player)){
                tplayer.toggleTownChat();
                return;
            }
            event.setCancelled(true);
            sendTownMessage(player, event.getMessage());
        }

    }

    public void sendTownMessage(Player player, String town_message){
        if(!TownS.g().hasTown(player)){
            return;
        }
        String  colorTownChatPermission = "townchat.color";
        Town player_town = TownS.g().getTown(player);
        String chatPrefix = ChatColor.translateAlternateColorCodes('&', "&8[&6#town_name#&8]&f "+player.getName()+" &8&l» &e");
        String chatMessage = ChatColor.translateAlternateColorCodes('&', town_message);
        if(!player.hasPermission(colorTownChatPermission)){ chatMessage = ChatColor.stripColor(chatMessage); }
        chatPrefix = chatPrefix.replaceFirst("#town_name#", player_town.getName());
        for(Player town_player: Bukkit.getOnlinePlayers()){
            if(TownS.g().hasTown(town_player)){
                if(TownS.g().getTown(town_player).equals(player_town)){
                    town_player.sendMessage(chatPrefix+chatMessage);
                }
            }
        }
    }

}
