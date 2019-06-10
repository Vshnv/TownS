package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player  = event.getPlayer();
        if(TownS.g().hasTown(player)){
            Town town = TownS.g().getTown(player);
            Location spawnloc = town.getWarpPoint(town, "spawn");
            if(spawnloc != null){
                event.setRespawnLocation(spawnloc);
            }
        }
    }

}
