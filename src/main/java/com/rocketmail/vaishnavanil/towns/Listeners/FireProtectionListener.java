package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireProtectionListener implements Listener {

    @EventHandler
    public void onEntityCombust(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(TownS.g().isClaimed(player.getChunk())){
                Claim claim = TownS.g().getClaim(player.getChunk());
                if(claim.hasFlag(Flag.FIRE_PROTECTION)){
                    String cause = event.getCause().toString();
                    if(cause.equals("FIRE_TICK") || cause.equals("LAVA") || cause.equals("FIRE")){
                        event.setCancelled(true);
                    }
                }
            }

        }
    }
}
