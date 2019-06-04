package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        TownS.g().addTownPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        TownS.g().removeTownPlayer(event.getPlayer());
    }
}
