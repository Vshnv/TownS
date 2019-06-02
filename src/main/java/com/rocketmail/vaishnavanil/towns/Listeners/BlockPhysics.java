package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.swing.*;

import static java.lang.System.out;

public class BlockPhysics implements Listener {
    @EventHandler
    public void onWaterSpread(BlockFromToEvent e){



       if(TownS.g().isClaimed(e.getToBlock().getLocation().getChunk())){

           Claim TO = TownS.g().getClaim(e.getToBlock().getLocation().getChunk());
           Claim FROM = null;
           if (TownS.g().isClaimed(e.getBlock().getLocation().getChunk())) {
               FROM = TownS.g().getClaim(e.getBlock().getLocation().getChunk());
           }
           if (FROM == null || FROM.getTown() != TO.getTown() || FROM.getOwnerID() != TO.getOwnerID()) {
               e.setCancelled(true);
           }

       }else if(TownS.g().isClaimed(e.getBlock().getLocation().getChunk())){
           Claim FROM = TownS.g().getClaim(e.getBlock().getLocation().getChunk());
           Claim TO = null;
           if (TownS.g().isClaimed(e.getToBlock().getLocation().getChunk())) {
               TO = TownS.g().getClaim(e.getToBlock().getLocation().getChunk());
           }
           if (TO == null || TO.getTown() != FROM.getTown() || TO.getOwnerID() != FROM.getOwnerID()) {
               e.setCancelled(true);
           }
       }else{
           return;
       }
    }
}
