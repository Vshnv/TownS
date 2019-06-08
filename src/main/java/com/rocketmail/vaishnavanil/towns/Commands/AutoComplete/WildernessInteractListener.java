package com.rocketmail.vaishnavanil.towns.Commands.AutoComplete;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class WildernessInteractListener implements Listener {

    @EventHandler
    public void onWildernessInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(block == null) return;
        if(block.getType().equals(Material.AIR)) return;
        if(block.getWorld().getName().equals("world")){
            if(!TownS.g().isClaimed(block.getLocation().getChunk())){
                if(!player.hasPermission("towns.buildwild")){
                    event.setCancelled(true);
                    Format.CmdErrFrmt.use().a(player, "This Area does not belong to you yet!");
                }
            }
        }
    }

}
