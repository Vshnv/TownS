package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
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

    @EventHandler
    public void onFireSpread(BlockSpreadEvent e){
        if(e.getBlock().getType() != Material.FIRE)return;
        if(!TownS.g().isClaimed(e.getBlock().getLocation().getChunk()))return;
        Claim claim = TownS.g().getClaim(e.getBlock().getLocation().getChunk());
        if(claim.hasFlag(Flag.FIRE_PROTECTION)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e){
        if(e.getBlock().getType() != Material.FIRE)return;
        if(!TownS.g().isClaimed(e.getBlock().getLocation().getChunk()))return;
        Claim claim = TownS.g().getClaim(e.getBlock().getLocation().getChunk());
        if(claim.hasFlag(Flag.FIRE_PROTECTION)){
            e.setCancelled(true);
        }
    }
}
