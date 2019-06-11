package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class ClaimPistonRestrict implements Listener {


    @EventHandler
    public void onPistExt(BlockPistonExtendEvent e){
        Chunk c = e.getBlock().getLocation().getChunk();
        Claim claim = null;
        if(TownS.g().isClaimed(c)){
            claim = TownS.g().getClaim(c);
        }
        for (Block b : e.getBlocks()) {
            if(claim != null){
                if(TownS.g().isClaimed(b.getLocation().getChunk())){
                    Claim t_c = TownS.g().getClaim(b.getLocation().getChunk());
                    if(claim.getOwnerID() != t_c.getOwnerID()){
                        e.setCancelled(true);
                        return;
                    }
                }else{
                    e.setCancelled(true);
                }
            }else{
                if(TownS.g().isClaimed(b.getLocation().getChunk())){e.setCancelled(true);}
            }
        }

    }
    @EventHandler
    public void onPistRetr(BlockPistonRetractEvent e){
        Chunk c = e.getBlock().getLocation().getChunk();
        Claim claim = null;
        if(TownS.g().isClaimed(c)){
            claim = TownS.g().getClaim(c);
        }
        for (Block b : e.getBlocks()) {
            if(claim != null){
                if(TownS.g().isClaimed(b.getLocation().getChunk())){
                    Claim t_c = TownS.g().getClaim(b.getLocation().getChunk());
                    if(claim.getOwnerID() != t_c.getOwnerID()){
                        e.setCancelled(true);
                        return;
                    }
                }else{
                    e.setCancelled(true);
                }
            }else{
                if(TownS.g().isClaimed(b.getLocation().getChunk()))e.setCancelled(true);
            }
        }
    }

}
