package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class ExplodeEventListener implements Listener {

    @EventHandler
    public void onExplosionByEntity(EntityExplodeEvent event) {
        if (TownS.g().isClaimed(event.getEntity().getLocation().getChunk())) {
            Claim claim = TownS.g().getClaim(event.getEntity().getLocation().getChunk());
            if (!claim.hasFlag(Flag.EXPLOSION)) {
                event.setYield(0);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onKnownCauseExplode(EntityExplodeEvent event) {
        //List<BlockState> blockStateList = new ArrayList<>();
        List<Block> ToR = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (TownS.g().isClaimed(block.getLocation().getChunk())) {
                //event.setCancelled(true);
                if(TownS.g().getClaim(block.getChunk()).hasFlag(Flag.EXPLOSION))continue;
                ToR.add(block);
            }else{
                ToR.add(block);
            }
        }
        event.blockList().removeAll(ToR);
    }

    @EventHandler
    public void onUnknownCauseExplode(BlockExplodeEvent event) {
        //List<BlockState> blockStateList = new ArrayList<>();
        List<Block> ToR = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (TownS.g().isClaimed(block.getLocation().getChunk())) {
               //event.setCancelled(true);
                if(TownS.g().getClaim(block.getLocation().getChunk()).hasFlag(Flag.EXPLOSION))continue;
                ToR.add(block);
            }else{
                ToR.add(block);
            }
        }
        event.blockList().removeAll(ToR);
    }

}
