package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class ExplodeEventListener implements Listener {

    @EventHandler
    public void onExplosionByEntity(EntityExplodeEvent event){
        if(TownS.g().isClaimed(event.getEntity().getChunk())){
            Claim claim = TownS.g().getClaim(event.getEntity().getChunk());
            if(!claim.hasFlag(Flag.EXPLOSION)){
                event.setYield(0);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event){
        //List<BlockState> blockStateList = new ArrayList<>();
        for(Block block: event.blockList()){
            if(TownS.g().isClaimed(block.getChunk())){
                event.setCancelled(true);
                return;
            }
        }
    }

}
