package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobManagingEventListener implements Listener {
    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if(!TownS.g().isClaimed(e.getEntity().getLocation().getChunk()))return;

        Claim claim = TownS.g().getClaim(e.getEntity().getLocation().getChunk());

        if(claim.hasFlag(Flag.MOBS)){

            if(ConfigManager.get.isAllowed(e.getEntityType()))return;
            e.setCancelled(true);

        }
    }
}
