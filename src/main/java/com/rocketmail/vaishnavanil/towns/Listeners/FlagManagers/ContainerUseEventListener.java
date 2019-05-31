package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.Listeners.Constraints;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ContainerUseEventListener implements Listener {
    @EventHandler
    public void onContainerUse(PlayerInteractEvent e){
        if(e.getAction() != Action.PHYSICAL || e.getAction() != Action.RIGHT_CLICK_BLOCK)return;
        if(!TownS.g().isClaimed(e.getClickedBlock().getLocation().getChunk()))
        if(Constraints.Container.isRestricted(e.getClickedBlock().getType())){
            Claim claim = TownS.g().getClaim(e.getClickedBlock().getLocation().getChunk());
                if(claim.hasFlag(Flag.CONTAINER)){
                   if(!claim.getTown().belongs(e.getPlayer())){
                       e.setCancelled(true);
                       return;
                   }
                   if(claim.getTown().hasPermission("ContainerALL",e.getPlayer())){
                       return;
                   }
                }else{
                    if(!claim.getTown().belongs(e.getPlayer())){
                        if(claim.canUseContainer(e.getPlayer()))return;
                        e.setCancelled(true);
                        return;
                    }
                }

        }
    }
}
