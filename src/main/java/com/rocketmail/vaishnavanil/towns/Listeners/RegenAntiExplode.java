package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class RegenAntiExplode implements Listener {


    @EventHandler
    public void EEX(EntityExplodeEvent e){
        if(TownS.g().isRegening(e.getLocation().getChunk())){
            e.setCancelled(true);
        }
        List<Block> toremove = new ArrayList<>();
        for(Block b:e.blockList()){
            if(TownS.g().isRegening(b.getLocation().getChunk())){
                toremove.add(b);
            }
        }
        if(!toremove.isEmpty())e.blockList().removeAll(toremove);
    }

    @EventHandler
    public void OBE(BlockExplodeEvent e){
        if(TownS.g().isRegening(e.getBlock().getLocation().getChunk())){
            e.setCancelled(true);
        }
        List<Block> toremove = new ArrayList<>();
        for(Block b:e.blockList()){
            if(TownS.g().isRegening(b.getLocation().getChunk())){
                toremove.add(b);
            }
        }
        if(!toremove.isEmpty())e.blockList().removeAll(toremove);
    }


}
