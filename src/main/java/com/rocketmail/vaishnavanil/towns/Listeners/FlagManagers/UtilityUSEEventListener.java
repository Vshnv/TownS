package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.Listeners.Constraints;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UtilityUSEEventListener implements Listener {
    @EventHandler
    public void onButtonHit(PlayerInteractEvent e){
        try {
            if (e.getClickedBlock() == null) return;
        }catch (Exception exceptIONlol){
            return;
        }
        if(!TownS.g().isClaimed(e.getClickedBlock().getLocation().getChunk()))return;
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){

           Claim claim = TownS.g().getClaim(e.getClickedBlock().getLocation().getChunk());
           if(claim.hasFlag(Flag.USE))return;
           if(claim.getOwnerID() == e.getPlayer().getUniqueId())return;
           if(claim.getTown().hasPermission("Allow.useALL",e.getPlayer()))return;
           if(TownS.g().getTown(e.getPlayer())!=null) {
               if (claim.getTown() == TownS.g().getTown(e.getPlayer())) return;
           }
            if(Constraints.Usable.isRestricted(e.getClickedBlock().getType())){
                e.setCancelled(true);
            }
        }
    }
}
