package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.Listeners.Constraints;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class TownRestricter implements Listener {

    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        if (!TownS.g().isClaimed(e.getBlock().getLocation().getChunk())) return;
            Player placer = e.getPlayer();
            Town PTown = TownS.g().getTown(placer);
            Claim ChunkClaim = TownS.g().getClaim(e.getBlock().getLocation().getChunk());
            if (ChunkClaim.hasFlag(Flag.EDIT) && PTown == ChunkClaim.getTown()) {
                if(ChunkClaim.getOwner() != placer)if(Constraints.Container.isRestricted(e.getBlock().getType())){
                    if(!ChunkClaim.canUseContainer(placer)){
                        e.setCancelled(true);
                    }
                }
                return;
            }


            e.setCancelled(!ChunkClaim.canBuild(placer));

    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
            if(!TownS.g().isClaimed(e.getBlock().getLocation().getChunk()))return;
                Player placer = e.getPlayer();
                Town PTown = TownS.g().getTown(placer);
                Claim ChunkClaim = TownS.g().getClaim(e.getBlock().getLocation().getChunk());
        if (ChunkClaim.hasFlag(Flag.EDIT) && PTown == ChunkClaim.getTown()) {
            return;
        }


        e.setCancelled(!ChunkClaim.canBuild(placer));


    }




}
